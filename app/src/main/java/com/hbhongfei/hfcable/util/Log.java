package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

public class Log extends MultiDexApplication {

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler  = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(base);
	}
}
