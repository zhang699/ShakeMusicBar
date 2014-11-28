package ntut.shakemusicbar.library;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ShakeMusicBarView extends LinearLayout {
	private static final float VELOCITY_DEFAULT = 118.0f;
	
	private List<IndividualMusicShakeBar> mBars = new ArrayList<IndividualMusicShakeBar>();
	private Handler mHandler;
	private OnInitFinishedListener mOnListener;
	
	private int mIntervalsBetweenBars;
	private int mBarCount;
	private int mColor;
	private int mBackgroundDrawable = -1;
	private float mVelocity = VELOCITY_DEFAULT;

	private int mCalculatedBarWidth;
	
	public interface OnInitFinishedListener{
		void onInitFinished();
	}
	
	public ShakeMusicBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mHandler = new Handler();
		
		setupBarViewConfiguration(context, attrs);
		setupDefaultConfigurationIfNeed();
	}

	private void setupBarViewConfiguration(Context context, AttributeSet attrs) {
		TypedArray barViewAttributes = context.obtainStyledAttributes(attrs, R.styleable.ShakeMusicBarView);
		
		mBarCount =barViewAttributes.getInteger(R.styleable.ShakeMusicBarView_barCount, 0);
		mIntervalsBetweenBars = barViewAttributes.getInteger(R.styleable.ShakeMusicBarView_barInvsInPx, 0);
		mColor = barViewAttributes.getColor(R.styleable.ShakeMusicBarView_barColor, 0);
		mVelocity = barViewAttributes.getInteger(R.styleable.ShakeMusicBarView_barVelocity, (int)VELOCITY_DEFAULT);
		
		barViewAttributes.recycle();
	}
	
	private void setupDefaultConfigurationIfNeed() {
		if(mBarCount == 0){
			mBarCount = 1;
		}
		if(mColor == 0){
			mColor = Color.GREEN;
		}
	}
	
	/**
	 * Initialize using default color, intervals between bars, the number of bars
	 */
	public void init(){
		init(mColor, mIntervalsBetweenBars, mBarCount);
	}
	/** Initialize using specified color, intervals between bars, the number of bars
	 * @param color the color value
	 * @param invBetweenBars the pixel interval between bars
	 * @param barCount number of bars
	 */
	public void init(final int color, final int invBetweenBars, final int barCount){
		mIntervalsBetweenBars = invBetweenBars;
		mBarCount = barCount;
		mColor = color;
		
		runShakingTask();
	}

	private void runShakingTask() {
		this.post(new Runnable() {

			@Override
			public void run() {
				initIndividualBars();
				
				shake(true);
				
				setupBarDrawable();
				setupInitCallback();
			}
		});
	}
	
	private void initIndividualBars() {
		createIndidualBars(mBarCount);
		
		float barViewWidth = getWidth();
		int numberOfIntervalsBetweenBars = mBarCount - 1;
		
		float indidvualBarWidth = (barViewWidth-numberOfIntervalsBetweenBars*mIntervalsBetweenBars);
		mCalculatedBarWidth = Math.round((indidvualBarWidth/ (float)mBarCount));
		
		for(int i=0;i<mBars.size();i++){
			IndividualMusicShakeBar currentBar = mBars.get(i);
		
			initBarViewPropertyAndAddToParent(i, currentBar);

			currentBar.init(mColor, mVelocity);
		}
	}

	private void createIndidualBars(int count){
		mBars.clear();
		for(int i =0;i<count;i++){
			mBars.add(new IndividualMusicShakeBar(getContext(), mHandler));
		}
	}

	private void initBarViewPropertyAndAddToParent(int numberOfBar, IndividualMusicShakeBar currentBar) {
		LayoutParams mBarLayoutParams = createLinearLayoutParams(mCalculatedBarWidth);
		int actualBarIntervals = calculateActualBarInterval(numberOfBar);
		
		mBarLayoutParams.setMargins(0, 0, actualBarIntervals , 0);
		addView(currentBar, mBarLayoutParams);
	}

	private int calculateActualBarInterval(int numberOfBar) {
		boolean isLastBar = numberOfBar < (mBars.size()-1);
		int actualBarIntervals = isLastBar ? mIntervalsBetweenBars: 0;
		return actualBarIntervals;
	}
	
	private void setupInitCallback() {
		if(mOnListener != null){
			mOnListener.onInitFinished();
		}
	}

	private void setupBarDrawable() {
		if(mBackgroundDrawable >= 0){
			applyBarDrawable(mBackgroundDrawable);
		}
	}
	
	public void setOnInitFinishedListener(OnInitFinishedListener initListener){
		mOnListener = initListener;
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
		return mIntervalsBetweenBars;
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
		init(mColor, mIntervalsBetweenBars, barCount);
	}

	/** Change color of the bar
	 * @param color color value
	 */
	public void changeColor(int color) {
		removeAllViews();
		mBackgroundDrawable = -1;
		init(color, mIntervalsBetweenBars, mBarCount);
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
