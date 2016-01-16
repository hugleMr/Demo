package com.hungle.demo;

import java.util.Arrays;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSErrorException;
import org.robovm.apple.foundation.NSMutableDictionary;
import org.robovm.apple.foundation.NSPropertyList;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.pods.facebook.core.FBSDKAppEvents;
import org.robovm.pods.facebook.core.FBSDKApplicationDelegate;
import org.robovm.pods.facebook.share.FBSDKShareDialog;
import org.robovm.pods.facebook.share.FBSDKShareLinkContent;
import org.robovm.pods.facebook.share.FBSDKSharing;
import org.robovm.pods.facebook.share.FBSDKSharingDelegateAdapter;
import org.robovm.pods.google.GGLContext;
import org.robovm.pods.google.analytics.GAI;
import org.robovm.pods.google.analytics.GAIDictionaryBuilder;
import org.robovm.pods.google.analytics.GAIFields;
import org.robovm.pods.google.analytics.GAILogLevel;
import org.robovm.pods.google.analytics.GAITracker;
import org.robovm.pods.google.mobileads.GADAdSize;
import org.robovm.pods.google.mobileads.GADBannerView;
import org.robovm.pods.google.mobileads.GADBannerViewDelegateAdapter;
import org.robovm.pods.google.mobileads.GADInterstitial;
import org.robovm.pods.google.mobileads.GADInterstitialDelegateAdapter;
import org.robovm.pods.google.mobileads.GADRequest;
import org.robovm.pods.google.mobileads.GADRequestError;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;

public class IOSLauncher extends IOSApplication.Delegate implements ControlsApp{
	
	private static final Logger log = new Logger(IOSLauncher.class.getName(),
			Application.LOG_DEBUG);

	private IOSApplication iosApplication;
	private static final boolean USE_TEST_DEVICES = false;
    private String AdsID = "ca-app-pub-1098407154700376/2058392840";
	private static final String AD_VIDEO_ID = "ca-app-pub-1098407154700376/8104926441";
	private GADBannerView adview;
	private boolean adsInitialized = false;
	private GADInterstitial interstitial;
	private UIViewController adsViewController;
	private static final String appID = "xxx";
	private static final String appName = "zzz";
	private static final String GAME_URI_NEW = 
			"https://itunes.apple.com/us/app/"+appName+"/id"+appID+"?ls=1&mt=8";
	
	
	@Override
	public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
		try {
            GGLContext.getSharedInstance().configure();
        } catch (NSErrorException e) {
            System.err.println("Error configuring the Google context: " + e.getError());
        }

        // Optional: configure GAI options.
        GAI gai = GAI.getSharedInstance();
        gai.enableCrashReporting();
        gai.getLogger().setLogLevel(GAILogLevel.Verbose);
        
		boolean finished = super.didFinishLaunching(application, launchOptions);
		FBSDKApplicationDelegate.getSharedInstance().didFinishLaunching(application, launchOptions);
		//loginAction();
		return finished;
	}
	
	@Override
	public boolean openURL(UIApplication application, NSURL url, String sourceApplication, NSPropertyList annotation) {
		return FBSDKApplicationDelegate.getSharedInstance().openURL(application, url, sourceApplication, annotation);
	}

	@Override
	public void didBecomeActive(UIApplication application) {
		super.didBecomeActive(application);
		FBSDKAppEvents.activateApp();
	}
	
    @Override
    protected IOSApplication createApplication() {
    	 IOSApplicationConfiguration config = new IOSApplicationConfiguration();
         config.orientationPortrait = true;
         config.orientationLandscape = false;
         createAndLoadInterstitial();
         iosApplication = new IOSApplication(new BlockGame(this), config);
 		return iosApplication;
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

	@Override
	public void shareFacebook(int score) {
		// TODO Auto-generated method stub
		NSURL urlShare = new NSURL(GAME_URI_NEW);
		FBSDKShareLinkContent content = new FBSDKShareLinkContent();
		content.setContentURL(urlShare);
		content.setImageURL(new NSURL("http://imgur.com/oFMZZgr.png"));
		content.setContentTitle("It's Crazy!!");
		content.setContentDescription("'BEST SCORE : "+score);
		
		UIViewController uiv = new UIViewController();
		UIApplication.getSharedApplication().getKeyWindow().
				getRootViewController().addChildViewController(uiv);
		
		final FBSDKShareDialog dialog = new FBSDKShareDialog();
		dialog.setShareContent(content);
		dialog.setDelegate(new FBSDKSharingDelegateAdapter() {
            @Override
            public void didComplete(FBSDKSharing sharer, NSDictionary<?, ?> results) {
            }

            @Override
            public void didCancel(FBSDKSharing sharer) {
            }
        });
        dialog.show();
	}

	@Override
	public void shareScreenShot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAds() {
		// TODO Auto-generated method stub
		initializeAds();
		final CGSize screenSize = UIScreen.getMainScreen().getBounds()
				.getSize();
		double screenWidth = screenSize.getWidth();

		final CGSize adSize = adview.getBounds().getSize();
		double adWidth = adSize.getWidth();
		double adHeight = adSize.getHeight();

		adview.setFrame(new CGRect((screenWidth / 2) - adWidth / 2, 0,
				adWidth, adHeight));
	}

	@Override
	public void hideAds() {
		// TODO Auto-generated method stub
		initializeAds();
		final CGSize screenSize = UIScreen.getMainScreen().getBounds()
				.getSize();
		double screenWidth = screenSize.getWidth();

		final CGSize adSize = adview.getBounds().getSize();
		double adWidth = adSize.getWidth();
		double adHeight = adSize.getHeight();

		adview.setFrame(new CGRect((screenWidth / 2) - adWidth / 2, -adHeight,
				adWidth, adHeight));
	}

	@Override
	public void showFullAds() {
		// TODO Auto-generated method stub
		adsViewController = new UIViewController();
		UIApplication.getSharedApplication().getKeyWindow().
				getRootViewController().addChildViewController(adsViewController);
		interstitial.present(adsViewController);
	}

	@Override
	public void pushScreen(String screen) {
		// TODO Auto-generated method stub
		GAITracker tracker = GAI.getSharedInstance().getTracker("UA-XXX-1");
        tracker.put(GAIFields.ScreenName(),"IOS"+ screen);
        tracker.send(GAIDictionaryBuilder.createScreenView().build());
	}

	@Override
	public void pushAction(String target, String index) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		GAITracker tracker = GAI.getSharedInstance().getTracker("UA-XXX-1");
        NSMutableDictionary<?, ?> event = GAIDictionaryBuilder.createEvent(
        		"IOS_", "Clicked", index, null).build();
        tracker.send(event);
	}
	
	
	private void createAndLoadInterstitial() {
		interstitial = new GADInterstitial(AD_VIDEO_ID);
	    interstitial.setDelegate(new GADInterstitialDelegateAdapter() {
	        @Override
	        public void didReceiveAd (GADInterstitial ad) {
	            System.out.println("Did receive ad.");
	        }
	        
	        @Override
	        public void didDismissScreen(GADInterstitial ad) {
	        	super.didDismissScreen(ad);
	        	createAndLoadInterstitial();
	        }

	        @Override
	        public void didFailToReceiveAd (GADInterstitial ad, GADRequestError error) {
	            System.out.println(error.description());
	            System.out.println(error.getErrorCode());
	        }
	    });
	    interstitial.loadRequest(new GADRequest());
    }
	
	public void initializeAds() {
		if (!adsInitialized) {

			adsInitialized = true;

			adview = new GADBannerView(GADAdSize.SmartBannerPortrait());
			adview.setBackgroundColor(new UIColor(0, 0, 0, 0));
			adview.setAdUnitID(AdsID); // put your secret key here
			adview.setRootViewController(iosApplication.getUIViewController());
			iosApplication.getUIViewController().getView().addSubview(adview);

			final GADRequest request = new GADRequest();
			 if (USE_TEST_DEVICES) {
	                request.setTestDevices(Arrays.asList(GADRequest.getSimulatorID()));
	                Foundation.log("Test devices: " + request.getTestDevices());
	            }

			adview.setDelegate(new GADBannerViewDelegateAdapter() {
				@Override
				public void didReceiveAd(GADBannerView view) {
					super.didReceiveAd(view);
					log.debug("didReceiveAd");
				}
				@Override
				public void didFailToReceiveAd(GADBannerView view,
						GADRequestError error) {
					super.didFailToReceiveAd(view, error);
					log.debug("didFailToReceiveAd:" + error);
				}
				
			});

			adview.loadRequest(request);

			log.debug("Initalizing ads complete.");
		}
	}
}