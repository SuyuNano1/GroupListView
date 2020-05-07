package me.nano.grouplist;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

public class CompatUtil {
	
	public static void setBackground(View v,Drawable drawable){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			v.setBackground(drawable);
		} else {
			v.setBackgroundDrawable(drawable);
		}
	}
	
	public static void setBottomMargin(View v,int bottom){
		MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams() ;
		lp.bottomMargin = bottom ;
		v.setLayoutParams(lp) ;
	}
}
