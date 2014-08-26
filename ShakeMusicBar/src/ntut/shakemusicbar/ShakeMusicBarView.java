package ntut.shakemusicbar;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ShakeMusicBarView extends LinearLayout {
	private List<IndividualMusicShakeBar> mBars = new ArrayList<IndividualMusicShakeBar>();
	private Handler mHandler;
	private OnInitFinishedListener mOnListener;
	
	private int mInvBetweenBars;
	private int mBarCount;
	private int mColor;
	private int mBackgroundDrawable = -1;
	private float mVelocity = 118.0f;
	interface OnInitFinishedListener{
		void onInitFinished();
	}
	public ShakeMusicBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mHandler = new Handler();
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShakeMusicBarView);
		mBarCount =a.getInteger(R.styleable.ShakeMusicBarView_barCount, 0);
		mInvBetweenBars = a.getInteger(R.styleable.ShakeMusicBarView_barInvsInPx, 0);
		mColor = a.getColor(R.styleable.ShakeMusicBarView_barColor, 0);
		if(mBarCount == 0){
			mBarCount = 1;
		}
		if(mColor == 0){
			mColor = Color.GREEN;
		}
		
		a.recycle();
	}
	
	private void createBars(int count){
		mBars.clear();
		for(int i =0;i<count;i++){
			mBars.add(new IndividualMusicShakeBar(getContext(), mHandler));
		}
	}

	public void setOnInitFinishedListener(OnInitFinishedListener initListener){
		mOnListener = initListener;
	}
	
	/**
	 * Initialize using default color, intervals between bars, the number of bars
	 */
	public void init(){
		init(mColor, mInvBetweenBars, mBarCount);
	}
	/** Initialize using specified color, intervals between bars, the number of bars
	 * @param color the color value
	 * @param invBetweenBars the pixel interval between bars
	 * @param barCount number of bars
	 */
	public void init(final int color, final int invBetweenBars, final int barCount){
		this.post(new Runnable() {

			@Override
			public void run() {
				mInvBetweenBars = invBetweenBars;
				mBarCount = barCount;
				mColor = color;
				
				float width = getWidth();
				int invCount = barCount - 1;
				int widthOfBar = Math.round(((width-invCount*invBetweenBars)/ (float)barCount));
				createBars(barCount);
				
				for(int i=0;i<mBars.size();i++){
					IndividualMusicShakeBar currentBar = mBars.get(i);
					LayoutParams mBarLayoutParams = createLinearLayoutParams(widthOfBar);
					mBarLayoutParams.setMargins(0, 0, i < (mBars.size()-1) ? invBetweenBars: 0, 0);
					addView(currentBar, mBarLayoutParams);
					currentBar.init(color);
					currentBar.setVelocity(mVelocity);
				}
				
				shake(true);
				
				if(mBackgroundDrawable >= 0){
					applyBarDrawable(mBackgroundDrawable);
				}
				if(mOnListener != null){
					mOnListener.onInitFinished();
				}
			}
		});
	
	}

	/** Start or Stop shaking 
	 * @param isShake This true indicated start to shake, false is stop shaking.
	 */
	public void shake(boolean isShake){
		for(IndividualMusicShakeBar bar : mBars){
			bar.shake(isShake);
		}
	}
	
	/** Set the specified velocity to all of bars inside this view.
	 * @param velocity the moving pixel distance per second 
	 */
	public void setVelocity(int velocity){
		mVelocity = velocity;
		for(IndividualMusicShakeBar bar : mBars){
			bar.setVelocity(velocity);
		}
	}
	
	/** After stop shaking, this method make all of bars still to specified pixel height. 
	 * @param yInPX the height stopped to.
	 */
	public void stopToHeight(float yInPX){
		for(IndividualMusicShakeBar bar : mBars){
			bar.stopToHeight(yInPX);
		}
	}
	private LayoutParams createLinearLayoutParams(int widthOfBar) {
		return new LayoutParams(widthOfBar,LinearLayout.LayoutParams.WRAP_CONTENT);
	}

	public float getVelocity() {
		if(mBars.size()  > 0){
			return mBars.get(0).getVelocity();
		}else{
			throw new RuntimeException("No IndividualBar contained, the size of list is 0");
		}
	}

	public int getInvBetweenBars() {
		return mInvBetweenBars;
	}


	public int getBarCount() {
		return mBarCount;
	}


	public int getColor() {
		return mColor;
	}

	/** Change the number of bars inside this view
	 * @param the number of bars
	 */
	public void changeBarCount(int barCount) {
		removeAllViews();
		init(mColor, mInvBetweenBars, barCount);
	}

	/** Change color of the bar
	 * @param color color value
	 */
	public void changeColor(int color) {
		removeAllViews();
		mBackgroundDrawable = -1;
		init(color, mInvBetweenBars, mBarCount);
	}

	public void removeAllViews() {
		shake(false);
		super.removeAllViews();
	}
	
	/** change the interval between bars.
	 * @param inv
	 */
	public void changeInvBetweenBars(int inv) {
		removeAllViews();
		init(mColor, inv, mBarCount);
	}

	public void changeDrawable(int backgroundDrawable){
		removeAllViews();
		mBackgroundDrawable = backgroundDrawable;
		init();
	}
	private void applyBarDrawable(int backgroundDrawable) {
		for(IndividualMusicShakeBar bar : mBars){
			bar.setBackgroundResource(backgroundDrawable);
		}
		
	}
	
	
}
