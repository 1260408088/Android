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
	 * 更新版本的状态码
	 */
	protected static final int UPDATA_VERSION = 100;

	/**
	 * 进入应用程序主界面的状态码
	 */
	protected static final int ENTER_HOME = 101;

	/**
	 * url地址出错
	 */
	protected static final int URL_ERROR = 102;
	protected static final int IO_ERROR = 103;
	protected static final int JSON_ERROR =104;
	
	/**
	 * 弹出对话框显示下载进度
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
    	//判断是更新还是直接进入
    	switch (msg.what) {
    	
		case UPDATA_VERSION:
			//弹出对话框，提示用户更新（对话框）
			showUpdateDialog();
			break;
		case ENTER_HOME:
			//直接进入主界面，acticvity跳转过程
			enterHome();
			
			break;
		case URL_ERROR:
			//URL路径出错，通过吐司打印出来
			Toastutils.show(getApplicationContext(),"URL路径异常");
			enterHome();
			break;
		case IO_ERROR:
			Toastutils.show(getApplicationContext(),"读取路径异常");
			enterHome();
			break;
		case JSON_ERROR:
			Toastutils.show(getApplicationContext(),"JSON路径异常");
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
        
        //初始化UI
        ininUI();
        Log.i(tag, "1");
        //初始化数据
        initData();
        Log.i(tag, "2");
        //初始化动画
        initAnimation();
        Log.i(tag, "3");
        //数据库初始化
        initDB();
        Log.i(tag, "4");
        //初始化快捷方式
        if(!SpUtil.getBoolean(getApplicationContext(), ConstantValue.HAS_SHOWCUT, false)){
        	initShortCut();
        }
        Log.i(tag, "5");
    }
       
    
    
	private void initShortCut(){
		//1.给Intent维护图标，名称
		Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		//维护图标
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, 
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
		//名称
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "黑马卫士 ");
		
		//2.点击快捷方式后跳转到activity
		//2.1维护开启的意图对象
		Intent shortCutIntent = new Intent("android.intent.action.Home");
		shortCutIntent.addCategory("android.intent.category.DEFAULT");
		
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);
		//3.发送广播
		sendBroadcast(intent);
		SpUtil.putBoolean(getApplicationContext(), ConstantValue.HAS_SHOWCUT, true);
	}
     


	private void initDB(){
		//归属地数据库拷贝
		initAdressDB("address.db");
		initAdressDB("commonnum.db");
		initAdressDB("antivirus.db");
	}



	/**
	 * 拷贝数据库至files文件夹下
	 * @param dbName 数据库名称
	 */
	private void initAdressDB(String dbName) {
		 //1.在files文件夹下创建同名dbName数据库的过程
		File filesDir = getFilesDir();
		File file = new File(filesDir,dbName);
		if(file.exists()){
			return;
		}
		InputStream inputStream = null;
		FileOutputStream fos = null;
		//获取第三方资产目录下的文件
		try {
			inputStream = getAssets().open(dbName);
			//将内容写到指定的文件夹的文件中
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
				//将流关闭
				inputStream.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
	}



	/**
	 * 设置初始化动画
	 */
	private void initAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(3000);
		//将整个布局文件都设置成透明效果
		rl_root.setAnimation(alphaAnimation);
		
		
	}



	/**
     *弹出对话框，提示用户进行更新 
	 */
	protected void showUpdateDialog(){
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("升级提醒");
		//设置描述内容
		builder.setMessage(mVersionDes);
		builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			//下载apk，apk连接地址，downloadUrl
				downloadApk();
			  //3展示进度条
			  progressDialog.show();
			}
			
		});
		builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			
			}
		});
		
		//点击取消的事件监听 
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				//即使用户点击取消，也需要让其进入应用
				enterHome();
				dialog.dismiss();
			}
		});
		builder.show();
	}



	/**
	 *使用xutils下载更新后的apk 
	 */
	protected void downloadApk() {
		//1，判断sd卡是否可用，是否挂载
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//2,获取sd路径
			String path=Environment.getExternalStorageDirectory().getAbsolutePath()+
		    File.separator+"mobilesafe74.apk";
			//3,发送请求，获取apk,并放置到指定路径
			HttpUtils httpUtils = new HttpUtils();
			//4,发送请求，传递参数(下载地址，下载应用放置的位置，)
			httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
				
				
				
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					//下载成功（下载过后放置在sd卡中）
					Log.i(tag,"下载成功");
					File file=responseInfo.result;
					
					//提示用户安装
					installApk(file);
					
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Log.i(tag, "下载失败");
					//下载失败
				}
				
				@Override
				public void onStart() {
					//刚刚开始下载方法
					Log.i(tag,"刚刚开始下载");
					super.onStart();
				}
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					    
					    //可以在这写一个进度条对话框，用来显示下载进度(待完善)****************
                        Log.i(tag, "下载中.......");
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

	
	/**安装对应apk
	 * @param file 安装文件
	 */
	
	protected void installApk(File file) {
	       //系统应用界面，原码，安装apk入口
		   //开启系统级别的应用使用intent来开启
		   Intent intent = new Intent();
		   intent.setAction("android.intent.action.VIEW");
		   intent.addCategory("android.intent.category.DEFAULT");
		  /* intent.setData(Uri.fromFile(file));
		   //设置安装的类型
		   intent.setType("application/vnd.android.package-archive");*/
		   intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		   startActivityForResult(intent, 0);
		   
	}



	/**
	 * 进入应用程序的主界面
	 */
	protected void enterHome() {
		 Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
		 startActivity(intent);
		 //在开启一个新的界面后，将导航界面关闭(导航界面只可见一次)
		 finish();
	}



	/**
	 * 获取数据方法
	 */
	private void initData() {
	    //1,应用版本名称
		tv_version_name.setText("当前版本名称："+getVersionName());
		//检测（本地版本号和服务器版本号对比）是否有更新，如果有更新，提示用户下载(member)
		//2，获取本地版本号
		mlocalVersionCode = getVersionCode();
		//3,获取服务器版本号（客户端发送请求，服务给响应 json ，xml）
		if(SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, true)){
			checkVersion();
		}else{
			
			//带延时的消息机制
			//mHandler.sendMessageDelayed(msg,4000);
			mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
		}
		
		
	}

	/**
	 * 检测版本号
	 */
	private void checkVersion() {
		
		new Thread(){ //第一种方式
		
		public void run(){
				//发送请求数据，参数则为请求json的连接地址
			//仅限于模拟器访问电脑tomcat
		    Message msg = new Message();
		    long startTime = System.currentTimeMillis();
			try {
				//1封装url地址
				URL url = new URL("http://192.168.1.137:8080/updata74.json");
			    //2开启一个连接
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				//3,设置常见请求参数（请求头）
			    //请求超时
				
				connection.setConnectTimeout(2000);
				//读取超时
				connection.setReadTimeout(2000);
				//获取请求成功响应码
				//默认就是get请求方式
				//connection.setRequestMethod("POST");
				if(connection.getResponseCode()==200){
					//5，以流的形式，将数据获取下来
					InputStream is = connection.getInputStream();
					//6 将流转换成字符串
					String json = StreamUtil.streamToString(is);
					Log.i(tag,json);
					System.out.println(json);
					//7 解析json
					JSONObject jsonObject = new JSONObject(json);
					
				    String versionName=jsonObject.getString("version_name");
				    mVersionDes = jsonObject.getString("description");
				    String versionCode=jsonObject.getString("version_code");
				    mDownloadUrl = jsonObject.getString("download_url");
				    
				    Log.i(tag,versionName);
				    Log.i(tag,mVersionDes);
				    Log.i(tag,versionCode);
				    Log.i(tag,mDownloadUrl);
				    //8,对比版本号（服务器版本号>本地版本号，提示用户更新）
					if(mlocalVersionCode<Integer.parseInt(versionCode)){
						//提示用户更新，弹出对话框（UI），消息机制
						msg.what=UPDATA_VERSION;
						
					}else{
						//进入应用程序主界面
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

					//指定睡眠时间，请求网络的时长超过4秒则不做处理
					//请求网络时间小于4秒，让其强制睡眠4秒
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
	 * 初始化UI方法 alt+shift+j
	 */
	private void ininUI() {
		  tv_version_name = (TextView) findViewById(R.id.tv_version_name);
		  rl_root = (RelativeLayout) findViewById(R.id.rl_root);
		  //设置一个对话框，
		  progressDialog = new ProgressDialog(this);
		  progressDialog.setIcon(R.drawable.ic_launcher);
		  progressDialog.setTitle("下载");
		  //2设置水平样式
		  progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	}
    
	
	private int getVersionCode(){
		//1，包管理者对象packageManager
		PackageManager pm = getPackageManager();
		//2,从包的管理者对象中，获取制定包名的基本信息（版本名称，版本号），传0代表获取基本信息
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			//3,获取版本名称
			return packageInfo.versionCode;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  0;
		
	}
	
	private String getVersionName(){
		//1，从包管理者对象packageManager
		PackageManager pm = getPackageManager();
		//2,从包的管理者对象中，获取指定的包名的基本信息，传0代表基本信息
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			
			return packageInfo.versionName;
		
		} catch (Exception e) {
			
		}
		return null;
		
	}
        //开启一个activity后，返回结果调用的方法
        @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			enterHome();
			super.onActivityResult(requestCode, resultCode, data);
		}
    
}
