package me.nano.grouplist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SwitcherItemView extends CommonItemView {
	
	private Switch mSwitch ;
	
	protected SwitcherItemView(Context context,GroupView parent,String title,String subtitle,Drawable icon){
		super(context,parent,title,subtitle,icon,null) ;
		mSwitch = new Switch(context) ;
		mSwitch.setLayoutParams(new LayoutParams(
			LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT
		)) ;
		super.setAccessoryCustomView(mSwitch) ;
		setOnItemClickListener(new GroupListView.OnItemClickListener(){
			@Override
			public void onItemClick(CommonItemView itemView,View view) {
				mSwitch.toggle() ;
			}
		}) ;
	}

	@Override
	public CommonItemView setAccessoryCustomView(View accessoryView) {
		throw new UnsupportedOperationException() ;
	}
	
	public SwitcherItemView setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener l){
		mSwitch.setOnCheckedChangeListener(l) ;
		return this ;
	}
	
	public Switch getSwitch(){
		return mSwitch ;
	}
	
	public SwitcherItemView setChecked(boolean checked){
		mSwitch.setChecked(checked) ;
		return this ;
	}
	
	public boolean isChecked(){
		return mSwitch.isChecked() ;
	}
	
}
