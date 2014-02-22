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
	
	public void init(){
		init(mColor, mInvBetweenBars, mBarCount);
	}
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

	public void shake(boolean isShake){
		for(IndividualMusicShakeBar bar : mBars){
			bar.shake(isShake);
		}
	}
	
	public void setVelocity(int velocity){
		mVelocity = velocity;
		for(IndividualMusicShakeBar bar : mBars){
			bar.setVelocity(velocity);
		}
	}
	
	public void stopToTranslateY(float yInPX){
		for(IndividualMusicShakeBar bar : mBars){
			bar.stopToTranslateY(yInPX);
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

	public void changeBarCount(int barCount) {
		removeAllViews();
		init(mColor, mInvBetweenBars, barCount);
	}

	public void changeColor(int color) {
		removeAllViews();
		mBackgroundDrawable = -1;
		init(color, mInvBetweenBars, mBarCount);
	}

	public void removeAllViews() {
		shake(false);
		super.removeAllViews();
	}
	
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
