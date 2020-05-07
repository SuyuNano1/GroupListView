package me.nano.grouplist;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.view.View.OnClickListener;
import android.support.annotation.StringRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import me.nano.grouplist.GroupListView.OnItemClickListener;

/**
 * 公用的 {@code Item} 类，可以继承该类来实现各种类型的 {@code Item} 样式。
 * 
 * 该 {@code Item} 包含标题、副标题（在标题下方）、图标（左侧）、自定义右侧视图。
 * {@code CommonItemView} 类或者一些已知子类都不支持通过使用构造方法的方式创建对象，使用 
 * {@code GroupView.newXxxItemView} 的方式进行创建。
 *
 * @author nano1
 * @see SwitcherItemView
 * @see TextItemView
 */
public class CommonItemView<T extends CommonItemView> implements CommonItemClickListener.Binder {
	
	private GroupView mParent ;
	private ItemLayout mCommonItemView ;
	private TextView mTitleView ;
	private TextView mSubtitleView ;
	private ImageView mIconView ;
	private FrameLayout mAccessoryView ;
	private OnItemClickListener mOnItemClickListener ;
	private CommonItemClickListener mCommonItemClickListener ;
	
	private boolean isAdjustVerticalPadding ;
	
	protected CommonItemView(Context context,GroupView parent,String title,String subtitle){
		this(context,parent,title,subtitle,null,null) ;
	}
	
	protected CommonItemView(Context context,GroupView parent,String title,String subtitle,Drawable iconDrawable,View accessoryView){
		this.mCommonItemView = (ItemLayout) LayoutInflater.from(context).inflate(R.layout.item_common,null,false) ;
		this.mTitleView = findViewById(R.id.item_common_title) ;
		this.mSubtitleView = findViewById(R.id.item_common_subtitle) ;
		this.mAccessoryView = findViewById(R.id.item_common_accessoryView) ;
		this.mIconView = findViewById(R.id.item_common_icon) ;
		this.mParent = parent ;
		
		mTitleView.setText(title) ;
		if(iconDrawable != null){
			mIconView.setImageDrawable(iconDrawable) ;
		}else{
			mIconView.setVisibility(View.GONE) ;
		}
		
		setSubtitle(subtitle) ;
		
		if(accessoryView != null){
			mAccessoryView.addView(accessoryView) ;
		}
	}

	/**
	 * 自动调整上下内边距，根据是否显示副标题来进行调节。
	 */
	protected T adjustVerticalPadding(boolean subtitleVisible) {
		float subtitleSize = mSubtitleView.getTextSize() ;
		int paddingTop = mCommonItemView.getPaddingTop() ;
		int paddingBottom = mCommonItemView.getPaddingBottom() ;
		if(subtitleSize <= 0 || (isAdjustVerticalPadding == subtitleVisible) ) {
			return (T)this ;
		}
		isAdjustVerticalPadding = subtitleVisible ;
		int p = (int) Math.floor(subtitleSize / 2) ;
		if(paddingTop - p >= 0){
			paddingTop += subtitleVisible ? -p : p ;
		}
		if(paddingBottom - p >= 0){
			paddingBottom += subtitleVisible ? -p : p ;
		}
		mCommonItemView.setPadding(
			mCommonItemView.getPaddingLeft(),
			paddingTop,
			mCommonItemView.getPaddingRight(),
			paddingBottom
		);
		return (T) this ;
	}

	public void setParent(GroupView parent) {
		this.mParent = parent;
	}

	public GroupView getParent() {
		return mParent;
	}
	
	/**
	 * 将 {@code Item} 本身添加到当前 {@code parent} 中。
	 * 
	 * @throws IllegalStateException 当前 {@code parent} 为空，需要手动调用 {@link #setParent(GroupView)} 进行添加。
	 */
	public GroupView commit(){
		if(this.mParent == null){
			throw new IllegalStateException("Can't invoke commit() in the current state because " + 
				"the parent(GroupView) is null."); 
		}
		return mParent.addItem(this) ;
	}
	
	/**
	 * 设置点击事件，如果监听器不为 {@code null}，则会覆盖 {@link GroupView} 的监听事件。
	 */
	public T setOnItemClickListener(OnItemClickListener l){
		this.mOnItemClickListener = l ;
		return (T) this ;
	}
	
	@Override
	public GroupListView.OnItemClickListener getOnItemClickListener() {
		return this.mOnItemClickListener;
	}
	
	public T setTitle(CharSequence text){
		mTitleView.setText(text) ;
		return (T) this ;
	}
	
	public T setTitle(@StringRes int textRes){
		mTitleView.setText(textRes) ;
		return (T) this ;
	}
	
	/**
	 * 设置副标题。
	 *
	 * @param text 如果文本为 {@code null}，则隐藏副标题。
	 */
	public T setSubtitle(@Nullable CharSequence text){
		if(text == null){
			mSubtitleView.setVisibility(View.GONE) ;
			adjustVerticalPadding(false) ;
		}else{
			mSubtitleView.setVisibility(View.VISIBLE) ;
			adjustVerticalPadding(true) ;
		}
		mSubtitleView.setText(text) ;
		return (T) this ;
	}
	
	/**
	 * 为左侧图片控件设置矢量图标，必须是矢量图。
	 *
	 * @param res 矢量图的 resourceId
	 */
	public T setVectorIcon(int res){
		Context context = mIconView.getContext() ;
		VectorDrawableCompat d = VectorDrawableCompat.create(
			context.getResources(),
			res,
			context.getTheme()
		);
		return setIcon(d) ;
	}
	
	public T setIcon(int res){
		return setIcon(ContextCompat.getDrawable(mIconView.getContext(),res)) ;
	}
	
	public T setIcon(@Nullable Drawable icon){
		if(icon == null){
			mIconView.setVisibility(View.GONE) ;
		}else{
			mIconView.setVisibility(View.VISIBLE) ;
		}
		mIconView.setImageDrawable(icon) ;
		return (T)this;
	}
	
	/**
	 * 自定义右侧的视图，如果以前设置过右侧的视图，则替换。
	 */
	public T setAccessoryCustomView(View accessoryView){
		if(mAccessoryView.getChildCount() > 0){
			mAccessoryView.removeAllViews() ;
		}
		mAccessoryView.addView(accessoryView) ;
		return (T) this ;
	}
	
	public ItemLayout getView(){
		return mCommonItemView ;
	}
	
	public TextView getTitle(){
		return mTitleView ;
	}
	
	public TextView getSubtitle(){
		return mSubtitleView ;
	}
	
	public ImageView getIcon(){
		return mIconView ;
	}
	
	public FrameLayout getAccessoryLayout(){
		return mAccessoryView ;
	}
	
	@Nullable
	public View getAccessoryView(){
		if(mAccessoryView.getChildCount() <= 0) return null;
		return mAccessoryView.getChildAt(0) ;
	}
	
	protected <V extends View> V findViewById(int id){
		return mCommonItemView.findViewById(id);
	}
	
	protected void setOnClickListener(CommonItemClickListener l){
		this.mCommonItemClickListener = l ;
		getView().setOnClickListener(this.mCommonItemClickListener) ;
	}
	
	protected CommonItemClickListener getOnClickListener(){
		return mCommonItemClickListener ;
	}
}
