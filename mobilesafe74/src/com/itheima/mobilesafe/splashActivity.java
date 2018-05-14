package com.itheima.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.mobilesafe.utils.SpUtil;
import com.itheima.mobilesafe.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class splashActivity extends Activity {

    protected static final String tag = "splashActivity";
 
	/**
	 * ���°汾��״̬��
	 */
	protected static final int UPDATA_VERSION = 100;

	/**
	 * ����Ӧ�ó����������״̬��
	 */
	protected static final int ENTER_HOME = 101;

	/**
	 * url��ַ����
	 */
	protected static final int URL_ERROR = 102;
	protected static final int IO_ERROR = 103;
	protected static final int JSON_ERROR =104;
	
	/**
	 * �����Ի�����ʾ���ؽ���
	 * 
	 */
	protected static final int PROGRESS=105;
	
	private TextView tv_version_name;
	private int mlocalVersionCode;
	private String mVersionDes;
	private String mDownloadUrl;
	private RelativeLayout rl_root;
	
    private Handler mHandler=new Handler(){
   
    public void handleMessage(Message msg) {
    	//�ж��Ǹ��»���ֱ�ӽ���
    	switch (msg.what) {
    	
		case UPDATA_VERSION:
			//�����Ի�����ʾ�û����£��Ի���
			showUpdateDialog();
			break;
		case ENTER_HOME:
			//ֱ�ӽ��������棬acticvity��ת����
			enterHome();
			
			break;
		case URL_ERROR:
			//URL·������ͨ����˾��ӡ����
			Toastutils.show(getApplicationContext(),"URL·���쳣");
			enterHome();
			break;
		case IO_ERROR:
			Toastutils.show(getApplicationContext(),"��ȡ·���쳣");
			enterHome();
			break;
		case JSON_ERROR:
			Toastutils.show(getApplicationContext(),"JSON·���쳣");
			enterHome();
			break;
		case PROGRESS:
			showUpdateDialog();
			break;
		
		
		}
    	
    }
    	
    };

	private ProgressDialog progressDialog;

	

	


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        //��ʼ��UI
        ininUI();
        Log.i(tag, "1");
        //��ʼ������
        initData();
        Log.i(tag, "2");
        //��ʼ������
        initAnimation();
        Log.i(tag, "3");
        //���ݿ��ʼ��
        initDB();
        Log.i(tag, "4");
        //��ʼ����ݷ�ʽ
        if(!SpUtil.getBoolean(getApplicationContext(), ConstantValue.HAS_SHOWCUT, false)){
        	initShortCut();
        }
        Log.i(tag, "5");
    }
       
    
    
	private void initShortCut(){
		//1.��Intentά��ͼ�꣬����
		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		//ά��ͼ��
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, 
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		//����
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "������ʿ ");
		
		//2.�����ݷ�ʽ����ת��activity
		//2.1ά����������ͼ����
		Intent shortCutIntent = new Intent("android.intent.action.Home");
		shortCutIntent.addCategory("android.intent.category.DEFAULT");
		
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);
		//3.���͹㲥
		sendBroadcast(intent);
		SpUtil.putBoolean(getApplicationContext(), ConstantValue.HAS_SHOWCUT, true);
	}
     


	private void initDB(){
		//���������ݿ⿽��
		initAdressDB("address.db");
		initAdressDB("commonnum.db");
		initAdressDB("antivirus.db");
	}



	/**
	 * �������ݿ���files�ļ�����
	 * @param dbName ���ݿ�����
	 */
	private void initAdressDB(String dbName) {
		 //1.��files�ļ����´���ͬ��dbName���ݿ�Ĺ���
		File filesDir = getFilesDir();
		File file = new File(filesDir,dbName);
		if(file.exists()){
			return;
		}
		InputStream inputStream = null;
		FileOutputStream fos = null;
		//��ȡ�������ʲ�Ŀ¼�µ��ļ�
		try {
			inputStream = getAssets().open(dbName);
			//������д��ָ�����ļ��е��ļ���
			fos = new FileOutputStream(file);
			byte[] bs=new byte[1024];
			int tmp=-1;
			while((tmp=inputStream.read(bs))!=-1){
				  fos.write(bs,0,tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(inputStream!=null&&fos!=null)
				//�����ر�
				inputStream.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
	}



	/**
	 * ���ó�ʼ������
	 */
	private void initAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(3000);
		//�����������ļ������ó�͸��Ч��
		rl_root.setAnimation(alphaAnimation);
		
		
	}



	/**
     *�����Ի�����ʾ�û����и��� 
	 */
	protected void showUpdateDialog(){
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("��������");
		//������������
		builder.setMessage(mVersionDes);
		builder.setPositiveButton("��������", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			//����apk��apk���ӵ�ַ��downloadUrl
				downloadApk();
			  //3չʾ������
			  progressDialog.show();
			}
			
		});
		builder.setNegativeButton("�Ժ���˵", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			
			}
		});
		
		//���ȡ�����¼����� 
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				//��ʹ�û����ȡ����Ҳ��Ҫ�������Ӧ��
				enterHome();
				dialog.dismiss();
			}
		});
		builder.show();
	}



	/**
	 *ʹ��xutils���ظ��º��apk 
	 */
	protected void downloadApk() {
		//1���ж�sd���Ƿ���ã��Ƿ����
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//2,��ȡsd·��
			String path=Environment.getExternalStorageDirectory().getAbsolutePath()+
		    File.separator+"mobilesafe74.apk";
			//3,�������󣬻�ȡapk,�����õ�ָ��·��
			HttpUtils httpUtils = new HttpUtils();
			//4,�������󣬴��ݲ���(���ص�ַ������Ӧ�÷��õ�λ�ã�)
			httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
				
				
				
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					//���سɹ������ع��������sd���У�
					Log.i(tag,"���سɹ�");
					File file=responseInfo.result;
					
					//��ʾ�û���װ
					installApk(file);
					
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Log.i(tag, "����ʧ��");
					//����ʧ��
				}
				
				@Override
				public void onStart() {
					//�ոտ�ʼ���ط���
					Log.i(tag,"�ոտ�ʼ����");
					super.onStart();
				}
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					    
					    //��������дһ���������Ի���������ʾ���ؽ���(������)****************
                        Log.i(tag, "������.......");
                        Log.i(tag, "total"+total);
                        Log.i(tag, "current"+current);
                        progressDialog.setMax((int) total);
                        progressDialog.setProgress((int) current);
					    super.onLoading(total, current, isUploading);
				}
			});
			
		}
	}

               class longBean{
	                  long Total;
	                  long current;
               }

	
	/**��װ��Ӧapk
	 * @param file ��װ�ļ�
	 */
	
	protected void installApk(File file) {
	       //ϵͳӦ�ý��棬ԭ�룬��װapk���
		   //����ϵͳ�����Ӧ��ʹ��intent������
		   Intent intent = new Intent();
		   intent.setAction("android.intent.action.VIEW");
		   intent.addCategory("android.intent.category.DEFAULT");
		  /* intent.setData(Uri.fromFile(file));
		   //���ð�װ������
		   intent.setType("application/vnd.android.package-archive");*/
		   intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		   startActivityForResult(intent, 0);
		   
	}



	/**
	 * ����Ӧ�ó����������
	 */
	protected void enterHome() {
		 Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
		 startActivity(intent);
		 //�ڿ���һ���µĽ���󣬽���������ر�(��������ֻ�ɼ�һ��)
		 finish();
	}



	/**
	 * ��ȡ���ݷ���
	 */
	private void initData() {
	    //1,Ӧ�ð汾����
		tv_version_name.setText("��ǰ�汾���ƣ�"+getVersionName());
		//��⣨���ذ汾�źͷ������汾�ŶԱȣ��Ƿ��и��£�����и��£���ʾ�û�����(member)
		//2����ȡ���ذ汾��
		mlocalVersionCode = getVersionCode();
		//3,��ȡ�������汾�ţ��ͻ��˷������󣬷������Ӧ json ��xml��
		if(SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, true)){
			checkVersion();
		}else{
			
			//����ʱ����Ϣ����
			//mHandler.sendMessageDelayed(msg,4000);
			mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
		}
		
		
	}

	/**
	 * ���汾��
	 */
	private void checkVersion() {
		
		new Thread(){ //��һ�ַ�ʽ
		
		public void run(){
				//�����������ݣ�������Ϊ����json�����ӵ�ַ
			//������ģ�������ʵ���tomcat
		    Message msg = new Message();
		    long startTime = System.currentTimeMillis();
			try {
				//1��װurl��ַ
				URL url = new URL("http://192.168.1.137:8080/updata74.json");
			    //2����һ������
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				//3,���ó����������������ͷ��
			    //����ʱ
				
				connection.setConnectTimeout(2000);
				//��ȡ��ʱ
				connection.setReadTimeout(2000);
				//��ȡ����ɹ���Ӧ��
				//Ĭ�Ͼ���get����ʽ
				//connection.setRequestMethod("POST");
				if(connection.getResponseCode()==200){
					//5����������ʽ�������ݻ�ȡ����
					InputStream is = connection.getInputStream();
					//6 ����ת�����ַ���
					String json = StreamUtil.streamToString(is);
					Log.i(tag,json);
					System.out.println(json);
					//7 ����json
					JSONObject jsonObject = new JSONObject(json);
					
				    String versionName=jsonObject.getString("version_name");
				    mVersionDes = jsonObject.getString("description");
				    String versionCode=jsonObject.getString("version_code");
				    mDownloadUrl = jsonObject.getString("download_url");
				    
				    Log.i(tag,versionName);
				    Log.i(tag,mVersionDes);
				    Log.i(tag,versionCode);
				    Log.i(tag,mDownloadUrl);
				    //8,�ԱȰ汾�ţ��������汾��>���ذ汾�ţ���ʾ�û����£�
					if(mlocalVersionCode<Integer.parseInt(versionCode)){
						//��ʾ�û����£������Ի���UI������Ϣ����
						msg.what=UPDATA_VERSION;
						
					}else{
						//����Ӧ�ó���������
						msg.what=ENTER_HOME;
					}
					
				}
			    } catch (MalformedURLException e) {
				e.printStackTrace();
				msg.what=URL_ERROR;
			    } catch (IOException e) {
			    	msg.what=IO_ERROR;
				e.printStackTrace();
		       } catch (JSONException e) {
					e.printStackTrace();
					msg.what=JSON_ERROR;
				}finally{

					//ָ��˯��ʱ�䣬���������ʱ������4����������
					//��������ʱ��С��4�룬����ǿ��˯��4��
					long endTime = System.currentTimeMillis();
					if((endTime-startTime)<4000){
						try {
							Thread.sleep(4000-(endTime-startTime));
						} catch (InterruptedException e) {
							
							e.printStackTrace();
						}
						//SystemClock.sleep(4000-(endTime-startTime));	
					}
					mHandler.sendMessage(msg);
				}
		};
		}.start();
		/*new Thread(new Runnable(){

			@Override
			public void run() {
				
				
			}});*/
		
	}



	/**
	 * ��ʼ��UI���� alt+shift+j
	 */
	private void ininUI() {
		  tv_version_name = (TextView) findViewById(R.id.tv_version_name);
		  rl_root = (RelativeLayout) findViewById(R.id.rl_root);
		  //����һ���Ի���
		  progressDialog = new ProgressDialog(this);
		  progressDialog.setIcon(R.drawable.ic_launcher);
		  progressDialog.setTitle("����");
		  //2����ˮƽ��ʽ
		  progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}
    
	
	private int getVersionCode(){
		//1���������߶���packageManager
		PackageManager pm = getPackageManager();
		//2,�Ӱ��Ĺ����߶����У���ȡ�ƶ������Ļ�����Ϣ���汾���ƣ��汾�ţ�����0�����ȡ������Ϣ
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			//3,��ȡ�汾����
			return packageInfo.versionCode;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  0;
		
	}
	
	private String getVersionName(){
		//1���Ӱ������߶���packageManager
		PackageManager pm = getPackageManager();
		//2,�Ӱ��Ĺ����߶����У���ȡָ���İ����Ļ�����Ϣ����0���������Ϣ
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			
			return packageInfo.versionName;
		
		} catch (Exception e) {
			
		}
		return null;
		
	}
        //����һ��activity�󣬷��ؽ�����õķ���
        @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			enterHome();
			super.onActivityResult(requestCode, resultCode, data);
		}
    
}
