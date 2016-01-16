package com.hungle.demo.android;

import java.util.HashMap;

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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.hungle.demo.BlockGame;
import com.hungle.demo.ControlsApp;
import com.hungle.demo.ScreenshotFactory;

import android.R.color;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class AndroidLauncher extends AndroidApplication implements ControlsApp{
	
	private static final String AD_BANNER_UNIT_ID = "ca-app-pub-1098407154700376/9515464049";
	private static final String AD_VIDEO_UNIT_ID = "ca-app-pub-1098407154700376/9515464049";
	private RelativeLayout layout;
	private CallbackManager callbackManager;
	private InterstitialAd interstitialAd;
	
	protected AdView adView;
	protected AdView bannerAd;
	
	String packageName = "com.yourcopany.nameofyourgame";
	String urlGPlay = "https://play.google.com/store/apps/details?id="
			+ packageName;
	String urlRate = "market://details?id=" + packageName;
	
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

		View gameView = initializeForView(new BlockGame(this), config);
		setupAds();
		setupVideo();

		layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT,
		ViewGroup.LayoutParams.WRAP_CONTENT);

		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);

		layout.addView(bannerAd, params);

		setContentView(layout);
	}
	
	
	//=============== Facebook Sdk
	
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
	
	
	
	//========== Banner ads & Full Ads

	private void setupAds() {
		bannerAd = new AdView(this);
		bannerAd.setVisibility(View.INVISIBLE);
		bannerAd.setBackgroundColor(color.transparent);
		bannerAd.setAdUnitId(AD_BANNER_UNIT_ID);
		bannerAd.setAdSize(AdSize.BANNER);
	}
	
	private void setupVideo(){
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(AD_VIDEO_UNIT_ID);
		interstitialAd.loadAd(new AdRequest.Builder().build());
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
			}

			@Override
			public void onAdFailedToLoad(int errorCode) {
			}

			@Override
			public void onAdOpened() {
			}

			@Override
			public void onAdClosed() {
			}

			@Override
			public void onAdLeftApplication() {
			}
		});
	}
	
	@Override
	public void showAds() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bannerAd.setVisibility(View.VISIBLE);
				AdRequest.Builder builder = new AdRequest.Builder();
				AdRequest ad = builder.build();
				bannerAd.loadAd(ad);
			}
		});
	}

	@Override
	public void hideAds() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bannerAd.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	protected void loadVideo() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AdRequest.Builder builder = new Builder();
				AdRequest adRequest = builder.build();
				interstitialAd.loadAd(adRequest);
			}
		});	
	}

	@Override
	public void showFullAds() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (interstitialAd != null && interstitialAd.isLoaded()){
					interstitialAd.show();
				}else{
					loadVideo();
				}
			}
		});
	}
	
	
	
	//=========================== Google Analytics!!
	
	@Override
	protected void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}
	
	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app.
		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
	}
	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	
	synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {

			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = analytics.newTracker(R.xml.app_tracker);
			mTrackers.put(trackerId, t);
		}
		return mTrackers.get(trackerId);
	}
	
	public void setTrackerScreenName(String name) {
		Tracker t = getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Android_"+name);
		t.send(new HitBuilders.ScreenViewBuilder().build());
	}
	
	public void setActionAnalytics(String target,String value){
		Tracker t = getTracker(TrackerName.APP_TRACKER);
		t.set(target, value);
		t.send(new HitBuilders.EventBuilder()
				.setCategory("ANDROID_"+target)
				.setAction("Clicked: ")
				.setLabel("score: "+value).build());
	}

	@Override
	public void pushScreen(String screen) {
		// TODO Auto-generated method stub
		setTrackerScreenName(screen);
	}

	@Override
	public void pushAction(String target, String index) {
		// TODO Auto-generated method stub
		setActionAnalytics(target,index);
	}
	
	
}
