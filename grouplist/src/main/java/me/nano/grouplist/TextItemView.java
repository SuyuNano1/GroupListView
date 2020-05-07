package me.nano.grouplist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import android.view.LayoutInflater;

public class TextItemView extends CommonItemView {
	
	private TextView mAccessoryTextView ;
	protected TextItemView(Context context,GroupView parent,String title,String subtitle,Drawable icon){
		super(context,parent,title,subtitle,icon,null) ;
		mAccessoryTextView = (TextView) LayoutInflater.from(context).inflate(R.layout.accessory_text,null,false) ;
		super.setAccessoryCustomView(mAccessoryTextView) ;
	}

	@Override
	public CommonItemView setAccessoryCustomView(View accessoryView) {
		throw new UnsupportedOperationException() ;
	}
	
	public TextView getAccessoryTextView(){
		return mAccessoryTextView ;
	}
	
	public TextItemView setAccessoryText(CharSequence text){
		mAccessoryTextView.setText(text) ;
		return this ;
	}
}
