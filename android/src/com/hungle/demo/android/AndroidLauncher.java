package com.hungle.demo.android;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.Tracker;
import com.hungle.demo.BlockGame;
import com.hungle.demo.ControlsApp;
import com.hungle.demo.ScreenshotFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class AndroidLauncher extends AndroidApplication implements ControlsApp{
	
	private static final String AD_BANNER_UNIT_ID = "ca-app-pub-1098407154700376/9515464049";
	private static final String AD_VIDEO_UNIT_ID = "ca-app-pub-1098407154700376/9515464049";
	private InterstitialAd interstitialAd;
	private RelativeLayout layout;
	private CallbackManager callbackManager;
	
	String packageName = "com.yourcopany.nameofyourgame";
	String urlGPlay = "https://play.google.com/store/apps/details?id="
			+ packageName;
	String urlRate = "market://details?id=" + packageName;
	
	private Tracker mTracker;
	private View decorView;
	private BlockGame game;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					
					@Override
					public void onSuccess(LoginResult result) {
						// TODO Auto-generated method stub
						System.out.println("Login!!");
					}
					
					@Override
					public void onError(FacebookException error) {
						// TODO Auto-generated method stub
						System.out.println("error: "+error.toString());
					}
					
					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
						
					}
				});
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BlockGame(this), config);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		AppEventsLogger.activateApp(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		AppEventsLogger.deactivateApp(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void shareFacebook(int score){
		if(ShareDialog.canShow(ShareLinkContent.class)){
			ShareLinkContent linkContent = new ShareLinkContent.Builder()
					.setContentTitle("Titlexxx")
					.setContentUrl(Uri.parse("URL"))
					.setImageUrl(Uri.parse("URLImage"))
					.setContentDescription("MY BEST SCORE: "+score)
					.build();
			
			ShareDialog.show(this, linkContent);
		}else{
			//log ra,hoặc tạo Toast.
		}
	}
	
	@Override
	public void shareScreenShot(){
		String screenshotPath = ScreenshotFactory.
				saveScreenshot(Gdx.files.getExternalStoragePath() 
						+ "/screenshots/screen.png");
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(screenshotPath,option);
		
		SharePhoto photo = new SharePhoto.Builder()
				.setBitmap(bitmap)
				.build();
		SharePhotoContent content = new SharePhotoContent.Builder()
				.addPhoto(photo)
				.build();
		
		ShareDialog.show(this,content);
	}

	@Override
	public void showads() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideAds() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showFullAds() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushScreen(String screen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushAction(String Target, String index) {
		// TODO Auto-generated method stub
		
	}
	
	
}
