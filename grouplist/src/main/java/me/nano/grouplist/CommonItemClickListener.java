package me.nano.grouplist;

import android.view.View.OnClickListener;
import android.view.View;
import android.support.annotation.NonNull;

class CommonItemClickListener implements OnClickListener {

	private Binder binder ;
	private CommonItemView itemView ;

	public CommonItemClickListener(@NonNull Binder binder,@NonNull CommonItemView itemView) {
		this.binder = binder;
		this.itemView = itemView;
	}

	public void setBinder(Binder binder) {
		this.binder = binder;
	}

	public Binder getBinder() {
		return binder;
	}
	
	@Override
	public void onClick(View v) {
		if(binder == null) return ;
		GroupListView.OnItemClickListener l = binder.getOnItemClickListener() ;
		if(l != null){
			l.onItemClick(itemView,v) ;
		}
	}
	
	public interface Binder {
		GroupListView.OnItemClickListener getOnItemClickListener() ;
	}
}
