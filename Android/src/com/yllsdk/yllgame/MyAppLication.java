package com.examp.yllgame;

import android.app.Application;

import com.yllgame.sdk.YllGameSdk;

import java.util.Arrays;

public class MyAppLication extends Application {
    public static Application application = null;
    @Override
    public void onCreate() {
        super.onCreate();
        //将参数修改为自己申请的参数，application, appid, googleClientId, appsFlyersDevKey
        YllGameSdk.getInstance().init(this, "202012031818", "301774558106-7va7f0jc10hrr1j4ttdvi43382i2v9cc.apps.googleusercontent.com", "SXxcrcc7oqnPXV9ycDerVP");
        //初始化必须调用设置SDK支持语言集合 目前只支持 ar：阿语 和 en：英语
        YllGameSdk.setLanguageList(Arrays.asList("ar", "en"));
        //调用设置SDK默认语言 ar：阿语 和 en：英语  如果没设置默认 就按照默认语言集合取第一个
        YllGameSdk.setLanguage("ar");
        application = this;
    }

    public static Application getApplication() {
        return application;
    }
}

