package com.nano.grouplist.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import me.nano.grouplist.CommonItemView;
import me.nano.grouplist.GroupListView;

public class MainActivity extends AppCompatActivity {

	GroupListView mListView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		mListView = findViewById(R.id.activity_main_grouplist) ;
		// 这个必须要写到前面
		// 这个不会覆盖 SwitcherView 的默认点击事件
		mListView.setOnItemClickListener(new GroupListView.OnItemClickListener(){
			@Override
			public void onItemClick(CommonItemView itemView,View view) {
				showToast(itemView.getTitle().getText().toString());
			}
		}) ;
		
		mListView.newGroupView("网络")
			.setItemDividerEnable(true)
			.newSwitcherItemView("使用2G/3G/4G网络播放",false)
				.setOnCheckedChangeListener(new OnCheckedChangeListener(){
					@Override
					public void onCheckedChanged(CompoundButton button,boolean isChecked) {
						showToast(isChecked ? "已开启2G/3G/4G网络播放" : "已关闭2G/3G/4G网络播放") ;
					}
				})
				.setOnItemClickListener(new GroupListView.OnItemClickListener(){
					@Override
					public void onItemClick(CommonItemView itemView,View view) {
						itemView.getParent().removeFrom(mListView) ;
					}
				}) .commit()
			.newSwitcherItemView("使用2G/3G/4G网络下载",false).commit()
			.newSwitcherItemView("Wifi状态下自动播放视频",true).commit()
			.newSwitcherItemView("Wifi状态下自动播放音频",true).commit()
			.addTo(mListView) 
		.newGroupView("缓存")
			.setItemDividerEnable(true)
			.newTextItemView("设置缓存上限","200MB").commit()
			.newTextItemView("清除音乐缓存","超过缓存上限自动清除","197.5MB")
				.setOnItemClickListener(new GroupListView.OnItemClickListener(){
					@Override
					public void onItemClick(CommonItemView itemView,View view) {
						itemView.setSubtitle(itemView.getSubtitle().getText().length() == 0 
							? "超过缓存上限自动清楚" : null) ;
					}
				})
				.commit()
			.newTextItemView("清除图片与歌词缓存","35MB").commit()
			.newTextItemView("清除视频缓存","144MB").commit()
			.addTo(mListView)
		.newGroupView("播放和下载")
			.setItemDividerEnable(true)
			.newTextItemView("在线播放音质","极高").commit()
			.newTextItemView("下载音质","极高").commit()
			.newTextItemView("边听边下","未开启").commit()
			.newTextItemView("下载目录","储存卡1").commit()
			.addTo(mListView) 
		 .newGroupView("关于")
			.setItemDividerEnable(true)
		 	.newItemView("关于App")
				.setIcon(R.drawable.ic_app)
				.commit()
		 	.newItemView("更多")
			.setOnItemClickListener(new GroupListView.OnItemClickListener(){
					@Override
					public void onItemClick(CommonItemView itemView,View view) {
						itemView.getParent().removeFrom(mListView) ;
					}
				})
				.setVectorIcon(R.drawable.ic_svg_more)
				.commit()
			.addTo(mListView) ;
    }
    
	private void showToast(String text) {
		Toast.makeText(this,text,0).show() ;
	}
}
