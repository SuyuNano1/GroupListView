package me.nano.grouplist;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author nano1
 */
public class GroupListView extends LinearLayout implements CommonItemClickListener.Binder{

	private static final int DEFAULT_RIPPLE_COLOR = 0xC29AA4A5 ;

	private Drawable mGroupDivider ;
	private int mGroupDividerHeight ;
	private int mGroupSpace ;
	private int[] mGroupDividerMargins ;
	private int mItemBackgroundColor ;
	private int mItemRippleColor ;
	private ArrayList<GroupView> mGroupViews ;
	private boolean mGroupDividerEnable = true ;
	
	private OnItemClickListener mOnItemClickListener ;

	public GroupListView(Context context) {
		this(context,null) ;
	}

    public GroupListView(Context context,AttributeSet attrs) {
		this(context,attrs,0) ;
	}

    public GroupListView(Context context,AttributeSet attrs,int defStyleAttr) {
		super(context,attrs,defStyleAttr) ;
		setOrientation(VERTICAL) ;
		mGroupViews = new ArrayList<>() ;
		mGroupDividerMargins = new int[4] ;
		initAttrs(attrs,defStyleAttr) ;
	}

	private void initAttrs(AttributeSet attrs,int defStyleAttr) {
		if(attrs == null) {
			return ;
		}
		TypedArray ta = getContext().obtainStyledAttributes(attrs,R.styleable.GroupListView,defStyleAttr,0) ;
		mGroupDivider = ta.getDrawable(R.styleable.GroupListView_glv_group_divider) ;
		mGroupDividerHeight = ta.getDimensionPixelOffset(R.styleable.GroupListView_glv_group_divider_height,1) ;
		mGroupSpace = ta.getDimensionPixelOffset(R.styleable.GroupListView_glv_group_space,0) ;

		mItemRippleColor = ta.getColor(R.styleable.GroupListView_glv_item_rippleColor,DEFAULT_RIPPLE_COLOR) ;
		mItemBackgroundColor = ta.getColor(R.styleable.GroupListView_glv_item_backgroundColor,Color.TRANSPARENT) ;
		mGroupDividerEnable = ta.getBoolean(R.styleable.GroupListView_glv_group_dividerEnable,true) ;
		
		getMargins(
			mGroupDividerMargins,ta,
			R.styleable.GroupListView_glv_group_divider_marginLeft,
			R.styleable.GroupListView_glv_group_divider_marginTop,
			R.styleable.GroupListView_glv_group_divider_marginRight,
			R.styleable.GroupListView_glv_group_divider_marginBottom,
			0
		) ;
		
		ta.recycle() ;
	}

	private void getMargins(int[] margins,TypedArray ta,int l,int t,int r,int b,int def) {
		margins[0] = ta.getDimensionPixelSize(l,def) ;
		margins[1] = ta.getDimensionPixelSize(t,def) ;
		margins[2] = ta.getDimensionPixelSize(r,def) ;
		margins[3] = ta.getDimensionPixelSize(b,def) ;
	}

	private RippleDrawable createRippleDrawable(int backgroundColor,int rippleColor) {
		Drawable backgroundDrawable = new ColorDrawable(backgroundColor);
		Drawable maskDrawable = new ColorDrawable(Color.WHITE) ;
		RippleDrawable rp = new RippleDrawable(
			new ColorStateList(new int[][]{new int[]{}},new int[]{rippleColor}),
			backgroundDrawable,
			maskDrawable
		) ;
		return rp ;
	}

	public GroupListView setGroupDividerEnable(boolean groupDividerEnable) {
		this.mGroupDividerEnable = groupDividerEnable;
		return this ;
	}

	public boolean isGroupDividerEnable() {
		return mGroupDividerEnable;
	}
	
	public GroupView newGroupView() {
		return new GroupView(getContext()) ;
	}
	
	public GroupView newGroupView(int titleRes){
		return newGroupView(getContext().getString(titleRes)) ;
	}

	public GroupView newGroupView(CharSequence title) {
		return new GroupView(getContext()).setTitle(title) ;
	}
	
	public GroupListView setGroupDivider(Drawable groupDivider) {
		this.mGroupDivider = groupDivider;
		return this ;
	}

	public Drawable getGroupDivider() {
		return mGroupDivider;
	}

	public boolean isGroupListEmpty() {
		return mGroupViews.size() == 0 ;
	}

	public boolean groupDividerEnable() {
		return mGroupDividerEnable ;
	}

	public GroupListView setGroupSpace(int mGroupSpace) {
		this.mGroupSpace = mGroupSpace;
		return this ;
	}

	public int getGroupSpace() {
		return mGroupSpace;
	}
	
	public GroupListView setOnItemClickListener(OnItemClickListener l){
		this.mOnItemClickListener = l ;
		return this ;
	}

	@Override
	public GroupListView.OnItemClickListener getOnItemClickListener() {
		return mOnItemClickListener;
	}
	
	public int getGroupCount(){
		return mGroupViews.size() ;
	}
	
	public GroupView getGroupView(int index){
		return mGroupViews.get(index) ;
	}

	public int indexOfGroupView(GroupView group){
		return mGroupViews.indexOf(group) ;
	}
	
	/**
	 * 将 {@code group} 添加进数组中。
	 */
	protected void addGroupView(@NonNull GroupView group) {
		mGroupViews.add(group) ;
	}
	
	/**
	 * 将 {@code group} 从数组中删除。
	 */
	protected void removeGroupView(int index){
		mGroupViews.remove(index) ;
	}
	
	protected Drawable createItemBackground(){
		return createRippleDrawable(mItemBackgroundColor,mItemRippleColor) ;
	}
	
	@Nullable
	protected View createGroupDividerView() {
		return createGroupDividerView(mGroupDivider) ;
	}
	
	@Nullable
	protected View createGroupDividerView(@NonNull Drawable divider){
		return createDividerView(divider,mGroupDividerHeight,mGroupDividerMargins) ;
	} 

	@Nullable
	protected View createDividerView(@NonNull Drawable drawable,int heightPx,int... margins) {
		if(drawable == null || heightPx <= 0) return null ;
		View divider = new View(getContext()) ;
		CompatUtil.setBackground(divider,drawable) ;
		MarginLayoutParams lp = new MarginLayoutParams(
			MarginLayoutParams.MATCH_PARENT,
			heightPx
		) ;
		lp.leftMargin = margins[0] ;
		lp.topMargin = margins[1] ;
		lp.rightMargin = margins[2] ;
		lp.bottomMargin = margins[3] ;
		divider.setLayoutParams(lp) ;
		return divider; 
	}
	
	public interface OnItemClickListener {
		public void onItemClick(CommonItemView itemView,View view) ;
	}

}
