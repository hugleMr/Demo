package com.hungle.demo;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate implements ControlsApp{
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new BlockGame(this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

	@Override
	public void shareFacebook(int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shareScreenShot() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAds() {
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
	public void pushAction(String target, String index) {
		// TODO Auto-generated method stub
		
	}
}