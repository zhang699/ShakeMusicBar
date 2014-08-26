package ntut.shakemusicbar.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewPropertyAnimator;

public class IndividualMusicShakeBar extends View {
	private static final Float[] MULTIPLIER_ARR = {0.9f,0.8f, 0.7f ,0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.1f, 0.0f};
	private static final int ANIMATIOR_DURATION = 150;
	
	private Handler mHandler;
	private ViewPropertyAnimator mViewAnimator;
	private int mIndex = 0;
	private boolean mIsShake = false;
	private List<Float> mShuffleList;
	private float mPrevMulti;
	private float mVelocity;// v = aniY/duration, duration = aniY/v, duration is in seconds 
	//private static final int FIXED_VELOCITY = 0.2f;
	public IndividualMusicShakeBar(Context context, Handler handler) {
		super(context);
		mHandler = handler;
		mViewAnimator = animate();
		mViewAnimator.setDuration(ANIMATIOR_DURATION);
		
		mShuffleList = Arrays.asList(MULTIPLIER_ARR);
	}
	
	public IndividualMusicShakeBar(Context context){
		this(context, new Handler());
	}
	
	private void shuffleMultiplier(){
		Collections.shuffle(mShuffleList);
	}
	
	public void init(int color){
		setBackgroundColor(color);
		startShake();
	}
	
	public void setVelocity(float velocity){
		mVelocity = velocity;
	}
	public float getVelocity(){
		return mVelocity;
	}

	private void startShake() {
		mHandler.postDelayed(new Runnable() {
		

			@Override
			public void run() {
				if(!mIsShake){
					return;
				}
			
				float height = getHeight();
				float multiplier = mShuffleList.get(mIndex);
			
				float aniY = height*multiplier;

				//ignore nextMultiplier if it is close to previous one which will cause bar present pause in visual
				//Pause visual effect is not match our expectation.
				while(Math.abs(mPrevMulti - multiplier) <= 0.0f){
					multiplier = mShuffleList.get((++mIndex) % mShuffleList.size());
				}
				
				float duration = aniY / mVelocity;
				long durationInLong = (long) (duration*1000);
				mViewAnimator.setDuration(durationInLong);
				mViewAnimator.translationY(aniY);
				
				if(mPrevMulti != multiplier){
					++mIndex;
				}
				if(mIndex >= mShuffleList.size()){
					mIndex = 0;
				}
				
				mPrevMulti = multiplier;
				shuffleMultiplier();
				postDelayed(this, durationInLong);
			}
		}, ANIMATIOR_DURATION);
	}
	
	public void stopToHeight(float yInPX){
		final int height = (int) (getHeight() - yInPX);
		mViewAnimator.translationY(height);
	}
	
	public void shake(boolean isShake) {
		if(isShake && mIsShake){
			//already shaking.
			return;
		}
		mIsShake = isShake;
		if(isShake){
			startShake();
		}
	}
}
