package com.itheima.mobilesafe;

import java.util.List;

import com.itheima.mobilesafe.bean.BlackNumberinfo;
import com.itheima.mobilesafe.dao.BlackNumberDao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;


//1.adapter优化
//2.findviewbyId()优化
//3.分页显示优化（优化查询）
public class BlackNumberActivity extends Activity {
       private Button bt_add;
       private ListView lv_display;
       private BlackNumberDao mDao;
       private List<BlackNumberinfo> list;
       private int mode;
       private Myadapter myadapter;
       private boolean isload=false; 
       private int mCount;
       private Handler mHandler=new Handler(){
       

			public void handleMessage(android.os.Message msg) {
				if(myadapter==null){
					//创建adapter
    	    	    myadapter = new Myadapter();
    	    	    lv_display.setAdapter(myadapter); 
				}else{
					myadapter.notifyDataSetChanged();
				}
    	    	    
    	      };
       };
       //创建数据适配器的类
	   class Myadapter extends BaseAdapter{

		@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Object getItem(int position) {
				return list.get(position);
			}

			@Override
			public long getItemId(int position) {
				
				return position;
			}
			
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				//复用viewholder一
				ViewHolder viewholder=null;
				//1复用converttView
				if(convertView==null){
					convertView=View.inflate(getApplicationContext(), R.layout.listview_item_activity, null);
					//2减少findview的此次数
					
					//复用viewholder三
					viewholder=new ViewHolder();
					//复用viewholder四
					viewholder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
					viewholder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
					//垃圾桶
					viewholder.iv_del=(ImageView) convertView.findViewById(R.id.iv_del); 
					convertView.setTag(viewholder);
				}else{
					//复用viewholder五
					 viewholder = (ViewHolder) convertView.getTag();
				}
				//给listview添加一个布局
				
				     
				//给垃圾桶设置点击事件
				viewholder.iv_del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//1删除数据库的内容
						mDao.del(list.get(position).number);
						//2更新list集合
						list.remove(position);
						//3通知adapter更新数据
						if(myadapter!=null){
							myadapter.notifyDataSetChanged();
						}
					}
				});
				
				viewholder.tv_number.setText(list.get(position).number);
				//转换成int类型，进行switch判断
				int modes = Integer.parseInt(list.get(position).mode);
				switch (modes) {
				case 1:
					viewholder.tv_mode.setText("拦截电话");
					break;
				case 2:
					viewholder.tv_mode.setText("拦截短信");
					break;
				case 3:
					viewholder.tv_mode.setText("拦截所有");
					break;
				}
				return convertView;
			}
	   }
	   
	    
	   //复用viewholder二
	   static class ViewHolder{
		   TextView tv_number;
		   TextView tv_mode;
		   ImageView iv_del;
	   }
	   
	   
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	setContentView(R.layout.activity_black_number);
    	super.onCreate(savedInstanceState);
    	intiUI();
    	initData();
    }
    //初始化数据
	private void initData() {
		//查询放入子线程
		new Thread(){

			public void run(){
				//1获取操作 黑名单数据库的的对象
				mDao = BlackNumberDao.getInstance(getApplicationContext());
				//2查询部分数据
				list = mDao.find(0);
				mCount = mDao.getcount();
//				for (BlackNumberinfo s : list) {
//					 String mode =s.mode;
//					 String number =s.number;
//					 System.out.println("mode :"+mode+"  "+"number :"+number);
//				}
				//告知adapter可以去加载list了
				mHandler.sendEmptyMessage(0);
			};
		}.start();
		
	}
        
	private void intiUI() {  
		//找到所需要的控件
		bt_add = (Button) findViewById(R.id.bt_add);
		lv_display = (ListView) findViewById(R.id.lv_display);
		//按钮点击，弹出对话框
		bt_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			       showDialog();
			}
		});
		//监听listview的滚动状态
		lv_display.setOnScrollListener(new OnScrollListener() {
			//滚动过程中，状态发生变换调用 的方法
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//OnScrollListener.SCROLL_STATE_FLING 飞速滚动
				//OnScrollListener.SCROLL_STATE_IDLE  空闲状态
				//OnScrollListener.SCROLL_STATE_TOUCH_SCROLL  拿手触摸着去滚动
				//容错处理
				if(list!=null){
				//条件一：滚动到停止状态
				//条件二：滚动到最后一个条目可见（最后一个条目的索引>=适配器里的条目总数-1）
				if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&lv_display.getLastVisiblePosition()>=list.size()-1
						&&!isload){
					/*isload防止重复加载的变量
					 * 如果正在加载isload会变成true，之后再改为false
					 * 下一次加载的时候判断isload的变量，是否为false，如果为true就需要把上一次加载完成，然后改为false
					 * 再去加载
					 */
					
					if(mCount>list.size()){
					//加载下一页数据
					//查询放入子线程
					new Thread(){
						public void run(){
							//1获取操作 黑名单数据库的的对象
							mDao = BlackNumberDao.getInstance(getApplicationContext());
							//2查询部分数据
							List<BlackNumberinfo> morelist = mDao.find(list.size());
							//3将morelist添加到list中
							list.addAll(morelist);
							//告知adapter可以去加载list了
							mHandler.sendEmptyMessage(0);
						};
					}.start();
					
				}
				}
			}
			}
			//滚动过程中调用的方法 
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
	}
	
	private void showDialog(){
			Builder builder = new AlertDialog.Builder(this);
			final AlertDialog dialog = builder.create();
			View view = View.inflate(getApplicationContext(), R.layout.dialog_insert_blacknumber, null);
			//兼容低版本为了消除边距
			dialog.setView(view,0,0,0,0);
			dialog.show();
			//取消和确定按钮
			Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
			Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
			//按钮组和editText
			RadioGroup rg_select = (RadioGroup) view.findViewById(R.id.rg_select);
			final EditText et_addnumber = (EditText) view.findViewById(R.id.et_addnumber);
			rg_select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					   switch (checkedId) {
					   		case R.id.rb_number:
					   			mode=1;
					   			break;
					   		case  R.id.rb_sms:
					   			mode=2;
					   			break;
					   		case  R.id.rb_all:
					   			mode=3;
					   			break;
					}
				}
			});
			
			bt_submit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					//1获取框中的电话号码
					String number = et_addnumber.getText().toString();  
					if(!TextUtils.isEmpty(number)){
					//2数据库中插入当前拦截的号码
					mDao.insert(number, mode+"");
				    //3数据库和集合保持同步 （1重新读取一遍数据库,2手动向集合添加一个对象（插入数据库构建的对象））
					BlackNumberinfo blackNumberinfo = new BlackNumberinfo();
					blackNumberinfo.number=number;
					blackNumberinfo.mode=mode+"";
					//添加到集合的最上层
					list.add(0,blackNumberinfo);
					//让adapter更新数据
					if(myadapter!=null){
					   myadapter.notifyDataSetChanged();
					}
					}else{
						Toastutils.show(getApplicationContext(), "请输入拦截号码");
					}
					dialog.dismiss();
					
				}
			});
			bt_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
	}

}
