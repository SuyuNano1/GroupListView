package me.nano.grouplist;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.util.Log;

/**
 * 带下划线的布局。
 *
 * @author nano1
 */
public class ItemLayout extends RelativeLayout {

	private int mBottomDividerColor ;
	private int mBottomDividerLeft ;
	private int mBottomDividerRight ;
	private float mBottomDividerHeight ;
	private boolean mBottomDividerEnable ;
	private Paint mDividerPaint ;

	public ItemLayout(Context context) {
		this(context,null) ;
	}

    public ItemLayout(Context context,AttributeSet attrs) {
		this(context,attrs,0) ;
	}

    public ItemLayout(Context context,AttributeSet attrs,int defStyleAttr) {
		super(context,attrs,defStyleAttr) ;
		mDividerPaint = new Paint() ;
		
		TypedArray ta = getContext().obtainStyledAttributes(attrs,R.styleable.ItemLayout,defStyleAttr,0) ;
		mBottomDividerColor = ta.getColor(R.styleable.ItemLayout_glv_item_divider,Color.TRANSPARENT) ;
		mBottomDividerHeight = ta.getDimension(R.styleable.ItemLayout_glv_item_divider_height,0); 
		mBottomDividerLeft = ta.getDimensionPixelOffset(R.styleable.ItemLayout_glv_item_divider_left,0) ;
		mBottomDividerRight = ta.getDimensionPixelOffset(R.styleable.ItemLayout_glv_item_divider_right,0) ;
		ta.recycle() ;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		int height = getHeight() ;
		int width = getWidth() ;
		if(mBottomDividerHeight > 0 && mBottomDividerEnable) {
			mDividerPaint.setStrokeWidth(mBottomDividerHeight);
            mDividerPaint.setColor(mBottomDividerColor);
			float y = (float) height - mBottomDividerHeight / 2f ;
			canvas.drawLine(mBottomDividerLeft,y,width - mBottomDividerRight,y,mDividerPaint) ;
		}
	}

	public void setBottomDivider(int height,int color,int left,int right) {
		this.mBottomDividerHeight = height ;
		this.mBottomDividerColor = color ;
		this.mBottomDividerLeft = left ;
		this.mBottomDividerRight = right ;

		invalidate() ;
	}
	
	public void setBottomDividerLeft(int left) {
		this.mBottomDividerLeft = left;
		invalidate() ;
	}

	public int getBottomDividerLeft() {
		return mBottomDividerLeft;
	}

	public void setBottomDividerRight(int right) {
		this.mBottomDividerRight = right;
		invalidate() ;
	}

	public int getBottomDividerRight() {
		return mBottomDividerRight;
	}
	
	public void showBottomDivider(){
		this.mBottomDividerEnable = true ;
		invalidate() ;
	}
	
	public void hideBottomDivider(){
		this.mBottomDividerEnable = false ;
		invalidate(); 
	}
	
	public int getBottomDividerColor() {
		return mBottomDividerColor;
	}
	
	public void setBottomDividerColor(int color){
		this.mBottomDividerColor = color ;
		invalidate() ;
	}
	
	public void setBottomDivdierHeight(float height){
		this.mBottomDividerHeight = height ;
		invalidate() ;
	}
	
	public float getBottomDividerHeight() {
		return mBottomDividerHeight;
	}
}
