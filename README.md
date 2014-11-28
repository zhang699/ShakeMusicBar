ShakeMusicBar
=============

A ShakeMusicBarView which imitates from Google Play Music Android APP is used to indicate the music is playing or stopped. 

ScreenShot
==
![ScreenShot](https://raw.github.com/zhang699/ShakeMusicBar/master/image/preview2.PNG)


Usage
==
Use it in yout layout.xml.
```xml
<ntut.shakemusicbar.library.ShakeMusicBarView
    android:id="@+id/shake_view"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:layout_below="@id/btn_switch"
    android:orientation="horizontal"
    custom:barColor="@android:color/darker_gray"
    custom:barCount="3"
    custom:barInvsInPx="3" />
```

Get instance and call init() to start to shake
```java
mBarView = (ShakeMusicBarView) findViewById(R.id.shake_view);
	
mBarView.setOnInitFinishedListener(new OnInitFinishedListener() {

      @Override
      public void onInitFinished() {
          //do something after the initialization is done. if there is nothings to do, 
          //you can give up putting this listener
      }
});
		
mBarView.init();
```
while you want to stop shaking and then stop to specified hight.

```java
mBarView.shake(false);
mBarView.stopToHeight(5.0f);
```

change property dynamically.
```java
mBarView.setVelocity(5); // In pixels
mBarView.changeColor(0xffff0000) // color in red
mBarView.changeBarCount(3) // change number of bars reside in the barview to 3
```

Thanks
==
This project requirement came from Jie-Chuen Li.
