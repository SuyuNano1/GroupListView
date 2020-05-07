package me.nano.grouplist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Space;
import android.widget.TextView;
import java.util.ArrayList;
import me.nano.grouplist.GroupListView.OnItemClickListener;

/**
 * 用于装载 {@link CommonItemView} 类。
 *
 * 提供了各种 {@code newXXXItem(args...)} 方法来创建 {@code Item} 。
 * 
 * @author nano1
 * @see CommonItemView
 * @see SwitcherItemView
 * @see TextItemView
 */
public class GroupView implements CommonItemClickListener.Binder {

	private boolean mItemDividerEnable ;
	private TextView mTitleView ;
	private ArrayList<CommonItemView> mItemViews ;
	private View mTopDividerView ;
	private Drawable mTopDividerDrawable ;
	private Space mTopSpace ;
	private Space mBottomSpace ;
	private Context mContext ;
	private OnItemClickListener mOnItemClickListener ;

	public GroupView(Context context) {
		this.mContext = context; 
		this.mItemViews = new ArrayList<>() ;
	}

	public GroupView setTitle(CharSequence title) {
		if(mTitleView == null) {
			mTitleView = createTitleView(title) ;
		} else {
			mTitleView.setText(title) ;
		}
		return this ;
	}

	public String getTitle() {
		return mTitleView.getText().toString() ;
	}

	public TextView getTitleView() {
		return mTitleView ;
	}

	/**
	 * 可能要使用 {@link #addTo(GroupListView)} 才会自动创建默认的分割线视图，可以调用 
	 * {@link #setTopDividerView(View)} 来手动设置覆盖默认分割线。
	 */
	@Nullable
	public View getTopDividerView() {
		return mTopDividerView ;
	}

	public GroupView setTopDividerView(View v) {
		this.mTopDividerView = v ;
		return this ;
	}

	/**
	 * 在调用 {@link #addTo(GroupListView)} 方法时会替代默认的分割线样式。
	 */
	public GroupView setTopDividerView(Drawable topDivider) {
		this.mTopDividerDrawable = topDivider ;
		return this ;
	}

	/**
	 * 将当前 {@code GroupView} 添加列表视图中。
	 * 
	 * Note: 不要重复添加，不要复用GroupView。
	 */
	public GroupListView addTo(GroupListView listView) {
		if(!listView.isGroupListEmpty()) {	
			addSpaceAndDivider(listView) ;
		}
		if(mTitleView != null) {
			listView.addView(mTitleView) ;
		}
		int iMax = mItemViews.size() - 1;
		if(iMax >= 0) {
			int i = 0 ;
			while(true) {
				CommonItemView itemView = mItemViews.get(i) ;
				ItemLayout iv = itemView.getView() ;
				
				CompatUtil.setBackground(iv,listView.createItemBackground()) ;
				bindClickListener(itemView,listView) ;

				listView.addView(iv) ;
				if(i == iMax) {
					iv.hideBottomDivider() ;
					break ;
				}
				if(mItemDividerEnable) {
					iv.showBottomDivider() ;
				} else {
					iv.hideBottomDivider() ;
				}
				i ++ ;
			}
		}
		listView.addGroupView(this) ;
		return listView; 
	}

	/**
	 * 将当前 {@code GroupView} 从列表视图中删除。
	 *
	 * Note: 会清除当前所有 {@code ItemView} 与 {@code GroupListView} 相关的点击事件。
	 */
	public void removeFrom(GroupListView listView) {
		int index = listView.indexOfGroupView(this) ;
		if(index >= 0) {
			if(mTitleView != null && mTitleView.getParent() == listView) {
				listView.removeView(mTitleView) ;
			}
			if(mTopDividerView != null && mTopDividerView.getParent() == listView) {
				listView.removeView(mTopDividerView) ;
			}
			int groupCount = listView.getGroupCount() ;
			if(groupCount > 2) {
				if(index == 0) { // 如果删除的是第一个GroupView
					GroupView secondGroup = listView.getGroupView(1) ;
					removeView(secondGroup.getTopDividerView(),listView) ;
					removeView(secondGroup.mTopSpace,listView) ;
				} else if(index == groupCount - 1) { // 如果删除的是倒数第一个
					GroupView penultGroup = listView.getGroupView(groupCount - 2) ;
					removeView(penultGroup.mBottomSpace,listView) ;
				}
			}

			removeView(mTopSpace,listView) ;
			removeView(mBottomSpace,listView) ;
			this.mTopSpace = null ;
			this.mBottomSpace = null ;

			for(CommonItemView item : mItemViews) {
				unbindClickListener(item,listView) ;
				removeView(item.getView(),listView) ;
			}
			listView.removeGroupView(index) ;
		}
	}

	public CommonItemView newItemView(String title) {
		return new CommonItemView(mContext,this,title,null,null,null) ;
	}

	public CommonItemView newItemView(String title,String subtitle) {
		return new CommonItemView(mContext,this,title,subtitle,null,null) ;
	}

	public CommonItemView newItemView(String title,Drawable icon) {
		return new CommonItemView(mContext,this,title,null,icon,null) ;
	}

	public CommonItemView newItemView(String title,String subtitle,Drawable icon) {
		return new CommonItemView(mContext,this,title,subtitle,icon,null) ;
	}

	public SwitcherItemView newSwitcherItemView(String title) {
		return new SwitcherItemView(mContext,this,title,null,null) ;
	}

	public SwitcherItemView newSwitcherItemView(String title,boolean isChecked) {
		return new SwitcherItemView(mContext,this,title,null,null).setChecked(isChecked) ;
	}

	public SwitcherItemView newSwitcherItemView(String title,String subtitle,boolean isChecked) {
		return new SwitcherItemView(mContext,this,title,subtitle,null).setChecked(isChecked) ;
	}

	public TextItemView newTextItemView(String title,String accessoryText) {
		return new TextItemView(mContext,this,title,null,null).setAccessoryText(accessoryText) ;
	}

	public TextItemView newTextItemView(String title,String subtitle,String accessoryText) {
		return new TextItemView(mContext,this,title,subtitle,null).setAccessoryText(accessoryText) ;
	}

	/**
	 * 添加 {@code Item} 到当前 {@code Group} 中，注意要添加相同的 {@code Item} 。
	 *
	 * @throws IllegalStateException 参数 item 已经设置其他 group 或者已经被添加进其他 group 中。
	 */
	public GroupView addItem(CommonItemView item) {
		if(item.getParent() == null) {
			item.setParent(this) ;
		} else if(item.getParent() != this) {
			throw new IllegalStateException("The parent group of the parameter Item is" + 
				" not the same as the current parent group.") ;
		}
		mItemViews.add(item) ;
		return this ;
	}

	/**
	 * 设置是否开启 {@code CommonItemView} 之间的分割线。
	 */
	public GroupView setItemDividerEnable(boolean itemDividerEnable) {
		this.mItemDividerEnable = itemDividerEnable;
		return this ;
	}

	public boolean isItemDividerEnable() {
		return mItemDividerEnable;
	}

	/**
	 * 设置所有子 {@code CommonItemView} 的点击事件，如果有的 {@code CommonItemView} 设置了点击事件
	 * 则该点击事件并不会覆盖它。
	 */
	public GroupView setOnItemClickListener(OnItemClickListener l) {
		this.mOnItemClickListener = l ;
		return this ;
	}

	@Override
	public OnItemClickListener getOnItemClickListener() {
		return mOnItemClickListener;
	}

	/**
	 * 根据指定位置获得某个 {@link CommonItemView} 对象。
	 */
	public CommonItemView getItemView(int index) {
		return mItemViews.get(index) ;
	}

	/**
	 * 获取子 {@code CommonItemView} 数量。
	 */
	public int getItemViewCount() {
		return mItemViews.size() ;
	}

	/**
	 * 获得某个 {@link CommonItemView} 的位置。
	 */
	public int indexOfItemView(CommonItemView itemView) {
		return mItemViews.indexOf(itemView) ;
	}

	protected View getLastView() {
		if(!mItemViews.isEmpty()) {
			return mItemViews.get(mItemViews.size() - 1).getView() ;
		} else if(mTitleView != null) {
			return mTitleView ;
		} else {
			return mTopDividerView ;
		}
	}

	private TextView createTitleView(CharSequence title) {
		TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.group_title,null,false) ;
		tv.setText(title) ;
		return tv;
	}

	private void bindClickListener(@NonNull CommonItemView iv,@Nullable GroupListView listView) {
		CommonItemClickListener.Binder binder = iv ;
		if(binder.getOnItemClickListener() == null) {
			binder = getOnItemClickListener() != null ? this : listView ;
		}
		if(binder != null) {
			iv.setOnClickListener(new CommonItemClickListener(binder,iv)) ;
		}
	}

	/**
	 * 将 {@code item} 与 {@code grouplist} 解绑。
	 */
	private void unbindClickListener(@NonNull CommonItemView iv,@NonNull GroupListView listView) {
		if(iv.getOnClickListener().getBinder() == listView) {
			iv.setOnClickListener(null) ;
		}
	}

	/**
	 * 为group之间添加分割线和间距
	 */
	private void addSpaceAndDivider(GroupListView listView) {	
		float space = listView.getGroupSpace() ;
		int count = listView.getGroupCount() ;
		if(listView.groupDividerEnable()) {
			// 创建Group的分割线
			if(this.mTopDividerView == null) {
				this.mTopDividerView = mTopDividerDrawable != null 
					? listView.createGroupDividerView(mTopDividerDrawable)
					: listView.createGroupDividerView() ;
			} 
		}
		if(space > 0) {
			GroupView above = listView.getGroupView(count - 1) ;
			above.mBottomSpace = newSpace(space / 2f) ;
			listView.addView(above.mBottomSpace) ;
		}
		if(mTopDividerView != null) {
			listView.addView(mTopDividerView) ;
		}
		if(space > 0) {
			this.mTopSpace = newSpace(space / 2f) ;
			listView.addView(this.mTopSpace) ;
		}
	}

	private void removeView(View v,GroupListView listView) {
		if(v != null && v.getParent() == listView) {
			listView.removeView(v) ;
		}
	}

	private Space newSpace(float height) {
		Space space = new Space(mContext) ;
		space.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,(int)Math.floor(height))) ;
		return space ;
	}
}
