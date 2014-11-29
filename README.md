#ShakeMusicBar


A ShakeMusicBarView is music indicator, which imitates from Google Play Music android application, used to indicate the music is playing or stopped. 

#Demo

<img src="https://raw.github.com/zhang699/ShakeMusicBar/master/image/preview2.PNG" width="320" alt="Screenshot"/>


#Usage

Use it in your layout.xml.
```xml
<ntut.shakemusicbar.library.ShakeMusicBarView
    android:id="@+id/shake_view"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:layout_below="@id/btn_switch"
    android:orientation="horizontal"
    custom:barColor="@android:color/darker_gray"
    custom:barCount="3"
    custom:barVelocity="150"
    custom:barInvsInPx="3" />
```

Get instance and call `init` method to start to shake
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
While you want to stop shaking and then stop to specified hight
, you can use `shake` and `stopToHeight` methods.

```java
mBarView.shake(false);
mBarView.stopToHeight(5.0f); //stop to 5 pixel height from the base.
```

Change `barVelocity`, `barColor` `barCount` property dynamically.
```java
mBarView.setVelocity(5); // In pixels
mBarView.changeColor(0xffff0000); // set background color of bar to red
mBarView.changeBarCount(3); // change number of bars reside in the barview to 3
```

You also can specifiy drawable as bar's background.
```java
mBarView.changeDrawable(R.drawable.rectangle_background);
```

#Thanks

I am very grateful to [Jie-Chuen Li](https://www.linkedin.com/pub/jason-li/94/949/a43) for giving me an opportunity to contribute this project.


#License

	The MIT License (MIT)
	
	Copyright (c) 2014 zhang699
	
	Permission is hereby granted, free of charge, to any person obtaining a copy of
	this software and associated documentation files (the "Software"), to deal in
	the Software without restriction, including without limitation the rights to
	use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
	the Software, and to permit persons to whom the Software is furnished to do so,
	subject to the following conditions:
	
	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.
	
	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
	FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
	COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
	CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

