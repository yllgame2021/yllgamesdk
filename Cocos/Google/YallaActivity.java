/****************************************************************************
 Copyright (c) 2015 Chukong Technologies Inc.

 http://www.cocos2d-x.org

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/
package com.examp.yllgame;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.billingclient.api.BillingClient;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yllgame.sdk.BuildConfig;
import com.yllgame.sdk.YllGameSdk;
import com.yllgame.sdk.common.YGCommonApi;
import com.yllgame.sdk.constants.YGConstants;
import com.yllgame.sdk.constants.YGEventConstants;
import com.yllgame.sdk.entity.GameFacebookFriendEntity;
import com.yllgame.sdk.event.YGEventApi;
import com.yllgame.sdk.listener.YGBooleanCallBack;
import com.yllgame.sdk.listener.YGCallBack;
import com.yllgame.sdk.login.YGLoginApi;
import com.yllgame.sdk.pay.YGPayApi;
import com.yllgame.sdk.pay.YGPaymentListener;
import com.yllgame.sdk.tripartite.YGTripartiteApi;
import com.yllgame.sdk.ui.dialog.listener.UpdateUserNameListener;
import com.yllgame.sdk.user.YGUserApi;
import com.yllgame.sdk.utils.LogUtils;
import com.yllgame.sdk.utils.ToastUtil;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import org.cocos2dx.lua.AppActivity;
import org.cocos2dx.lua.DragButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import afu.org.checkerframework.checker.nullness.qual.Nullable;


public class YallaActivity extends Cocos2dxActivity {

    private static YallaActivity yallaActivity = null;
    //登陆回调，需要在登陆的时候传入lua函数
    private static int luaLoginCallBack = -1;
    //修改昵称回调
    private static int modifyNameCallBack = -1;
    //同步角色回调
    private static int syncRoleCallBack = -1;
    //支付回调
    private static int payCallBack = -1;
    //分享回调
    private static int friendCallBack = -1;
    // 确保 init 在 UI 线程调用
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    //浏览器相关
    private static WebView webView = null;
    private static ImageButton closebtn = null;
    private static ImageButton reloadbtn = null;
    private static DragButton backButton = null;

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.logEForDeveloper("c2d-onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.logEForDeveloper("c2d-onRestart");
        onWindowFocusChanged(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.logEForDeveloper("c2d-onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.logEForDeveloper("c2d-onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.logEForDeveloper("c2d-onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.logEForDeveloper("c2d-onDestroy");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yallaActivity = this;
    }

    //登陆界面
    public static void login(final int luaFunc) {
        luaLoginCallBack = luaFunc;
        YGLoginApi.getInstance().login(yallaActivity);
    }
    //静默登陆
    public static void loginGuest(final int luaFunc) {
        luaLoginCallBack = luaFunc;
        YGLoginApi.getInstance().silentGuestLogin();
    }
    //登陆回调
    public static void login_callBack(final String info) {
        Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
            @Override
            public void run() {
                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(luaLoginCallBack, info);
//                Cocos2dxLuaJavaBridge.releaseLuaFunction(luaLoginCallBack);
            }
        });
    }
    //用户管理界面
    public static void accountManager() {
        YGUserApi.getInstance().openAccountManager(yallaActivity);
    }
    //修改昵称
    public static void showModifyName(final int luaFunc){
        modifyNameCallBack = luaFunc;
        YGUserApi.getInstance().showUpdateNickNameDialog(yallaActivity, new UpdateUserNameListener() {
            /**
             * 修改成功回调
             * @param stat true：修改成功 false：修改失败
             * @param userName 修改后的用户名
             */
            @Override
            public void onResult(boolean stat, String userName) {
                Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        if(stat){
                            Cocos2dxLuaJavaBridge.callLuaFunctionWithString(modifyNameCallBack, userName);
//                          Cocos2dxLuaJavaBridge.releaseLuaFunction(modifyNameCallBack);
                        }
                    }
                });
            }
        });
    }
    //设置界面
    public static void showSetting(String serviceId, String roleID){
        YGUserApi.getInstance().showSettingsView(yallaActivity, serviceId, roleID);
    }
    //客服界面
    public static void showserviceChat(String roleServiceID, String roleID) {
        YGUserApi.getInstance().showServiceChatView(yallaActivity, roleServiceID, roleID);
    }
    //设置语言
    public static void setLanguage(String language) {
        YllGameSdk.setLanguage(language);
    }
    //设置SDK模式
    public static void setNetModel(final int netM) {
        LogUtils.logEForDeveloper("设置网络模式为"+netM);
        if (netM == 1) {
            YllGameSdk.setNetMode(YGConstants.SDK_STRONG_NET);
        }else if (netM == 2){
            YllGameSdk.setNetMode(YGConstants.SDK_WEAK_NET);
        }
    }
    //同步角色
    // roleId：角色id ：Int 必要参数
    // roleName：角色名称 ：string 必要参数
    // roleLevel：角色等级 ：int 必要参数
    // roleVipLevel：角色Vip等级 ：int 非必要 没有默认0
    // serverId：角色所在服务器id：int 必要参数
    // roleCastleLevel：城堡等级 ：int 非必要 没有默认0
    //  CommonBooleanCallBack：同步角色回调
    public static void syncRoleInfo(String roleId, String roleName, String roleLevel, String roleVipLevel, String serverId ,String roleCastleLevel,final int luaFunc) {
        syncRoleCallBack = luaFunc;
        YGUserApi.getInstance().syncRoleInfo(roleId, roleName, roleLevel, roleVipLevel, serverId, roleCastleLevel, new YGBooleanCallBack() {
            @Override
            public void onResult(boolean b) {
                //回调，同步角色成功or失败
                if (b) {
                    LogUtils.logEForDeveloper("同步角色成功");
                    Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            Cocos2dxLuaJavaBridge.callLuaFunctionWithString(syncRoleCallBack, "success");
//                            Cocos2dxLuaJavaBridge.releaseLuaFunction(syncRoleCallBack);
                        }
                    });
                } else {

                }
            }
        });
    }

    //获取SDK信息
    public static String getSDKInfo(){
        JSONObject result = new JSONObject();
        try {
            result.put("baseurl", BuildConfig.BASE_URL);
            result.put("versionname", BuildConfig.versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String finalStr = result.toString().replaceAll("\\\\", "");
        return finalStr.toString();
    }
    //充值
    public static void pay(String skytype, String roleID, String roleServiceID, String sku, String price , String pointID, final int luaFunc) {
        LogUtils.logEForDeveloper("充值类型"+skytype);
        payCallBack = luaFunc;
        YGPayApi.pay(yallaActivity, skytype, roleID, roleServiceID, sku, System.currentTimeMillis() + "",
            System.currentTimeMillis() + "", 1 + "", price, pointID, new YGPaymentListener() {
                @Override
                public void paymentSuccess() {
                    Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.logEForDeveloper("支付成功");
                            Cocos2dxLuaJavaBridge.callLuaFunctionWithString(payCallBack, pointID);
                            //Cocos2dxLuaJavaBridge.releaseLuaFunction(payCallBack);
                        }
                    });
                }

                @Override
                public void paymentFailed(int code) {
                    LogUtils.logEForDeveloper("充值失败，返回码"+code);
                }
        });
    }

    public static void crashTest(String test) {
        test.toString();
    }

    //自定义事件
    public static void onEvent(String eventName, String jsonData) {
        Map paramsMap = converJsonToMap(jsonData);
        YGEventApi.onEvent(eventName, paramsMap);
    }

    public static Map converJsonToMap(String json) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Map map = gson.fromJson(json, new TypeToken<Map>() {
        }.getType());
        return map;
    }

    public static void copyToClipboard(String str)
    {
        Runnable runnable = new Runnable() {
            @SuppressWarnings("deprecation")
            public void run() {
                ClipboardManager mClipboardManager = (ClipboardManager)yallaActivity.getSystemService(CLIPBOARD_SERVICE);
                mClipboardManager.setText(str);
            }
        };
        yallaActivity.runOnUiThread(runnable);
    }
    //分享
    public static void shareToFB(String  desc, String url, final int luaFunc){
        YGTripartiteApi.getInstance().shareLink(yallaActivity, desc, url, new FacebookCallback<Sharer.Result> (){

            @Override
            public void onSuccess(Sharer.Result result) {
                //回调,释放lua函数
                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(luaFunc, "success");
                Cocos2dxLuaJavaBridge.releaseLuaFunction(luaFunc);
            }

            @Override
            public void onCancel() {
                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(luaFunc, "cancel");
                Cocos2dxLuaJavaBridge.releaseLuaFunction(luaFunc);
            }

            @Override
            public void onError(FacebookException error) {
                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(luaFunc, "faild");
                Cocos2dxLuaJavaBridge.releaseLuaFunction(luaFunc);
            }
        });
    }
    //获取好友列表
    public static String getFriends(final int luaFunc){
        friendCallBack = luaFunc;
        String json = "";
        Log.e("bagin", "开始获取");
        YGTripartiteApi.getInstance().getFacebookFriends(yallaActivity, new YGCallBack<List<GameFacebookFriendEntity>>(){
            @Override
            public void onSuccess(List<GameFacebookFriendEntity> gameFacebookFriendEntities) {
                Cocos2dxGLSurfaceView.getInstance().queueEvent(new Runnable() {
                    @Override
                    public void run() {

                        LogUtils.logEForDeveloper("收到好友列表"+gameFacebookFriendEntities.size());
                        JSONArray json = new JSONArray();
                        for(GameFacebookFriendEntity entityOne : gameFacebookFriendEntities){
                            JSONObject jo = new JSONObject();
                            try {
                                jo.put("fbId", entityOne.getFbId());
                                jo.put("userOpenId", entityOne.getUserOpenId());
                                jo.put("name", entityOne.getName());
//                                jo.put("avatarUrl", entityOne.getAvatarUrl());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            json.put(jo);
                        }

                        LogUtils.logEForDeveloper(json.toString());
//                        String finalStr = json.toString().replaceAll("\\\\", "");
                        //回调,释放lua函数
                        Cocos2dxLuaJavaBridge.callLuaFunctionWithString(friendCallBack, json.toString());
//                        Cocos2dxLuaJavaBridge.releaseLuaFunction(friendCallBack);
                    }
                });
            }

            @Override
            public void onFail(int i) {
                Log.e("faild", "获取失败");
            }
        });
        return json;
    }

    public static void OpenWebViewPortrait(final String url, final int luaFunc) {
        Log.e("YallaActivity======", url);
        Intent intent = new Intent(yallaActivity, GameWebActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("func",luaFunc);
        yallaActivity.startActivity(intent);
    }

    public static void onOpenWebView(String url, int luaFunc){
        Log.e("onOpenWebView", "onOpenWebView: "+url);
        Runnable runnable = new Runnable() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @SuppressWarnings("deprecation")
            public void run() {

                webView = new WebView(yallaActivity);
//                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//                        FrameLayout.LayoutParams.WRAP_CONTENT);
//                layoutParams.leftMargin = 0;
//                layoutParams.topMargin = 0;
//                layoutParams.width = yallaActivity.mFrameLayout.getWidth();
//                layoutParams.height = yallaActivity.mFrameLayout.getHeight();
//                layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
//                webView.setLayoutParams(layoutParams);

                webView.setFocusable(true);
                webView.setFocusableInTouchMode(true);
                webView.setBackgroundColor(0);
                //设置
                webView.getSettings().setSupportZoom(false);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setUseWideViewPort(true); //屏幕适配
                webView.getSettings().setLoadWithOverviewMode(true);
                //webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);//关闭缓存
                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//缓存默认设置，根据cache-control决定是否从网络上取数据。

                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient());

                yallaActivity.mFrameLayout.addView(webView);
                webView.loadUrl(url);

                closebtn = new ImageButton(yallaActivity);//关闭
                closebtn.setBackgroundResource(R.drawable.btnback);//按钮资源路径
                closebtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                closebtn.setVisibility(View.GONE);
                yallaActivity.mFrameLayout.addView(closebtn);

                reloadbtn = new ImageButton(yallaActivity);//刷新
                reloadbtn.setBackgroundResource(R.drawable.btnreload);//按钮资源路径
                reloadbtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                reloadbtn.setVisibility(View.GONE);
                yallaActivity.mFrameLayout.addView(reloadbtn);

                backButton = new DragButton(yallaActivity);//控制按钮

                backButton.setBackgroundResource(R.drawable.btnshow);//按钮资源路径
                backButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                backButton.setOnClickListener(new View.OnClickListener() {//按钮回调
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onClick(View view) {
                        if(closebtn.getVisibility() == View.GONE ){
                            closebtn.setVisibility(View.VISIBLE);
                            reloadbtn.setVisibility(View.VISIBLE);
                            backButton.setBackgroundResource(R.drawable.btnshow1);//按钮资源路径
                        }
                        else{
                            closebtn.setVisibility(View.GONE);
                            reloadbtn.setVisibility(View.GONE);
                            backButton.setBackgroundResource(R.drawable.btnshow);//按钮资源路径
                        }
                    }
                });
                yallaActivity.mFrameLayout.addView(backButton);//按钮在webview之后添加,所以在上层

                Bitmap bmback = BitmapFactory.decodeResource(yallaActivity.getResources(), R.drawable.btnback);
                Bitmap bmshow = BitmapFactory.decodeResource(yallaActivity.getResources(), R.drawable.btnshow);
                Bitmap bmreload = BitmapFactory.decodeResource(yallaActivity.getResources(), R.drawable.btnreload);
                backButton.setX(0);
                backButton.setY(yallaActivity.mFrameLayout.getHeight()/2 - bmshow.getHeight()/2);
                closebtn.setX(0);
                closebtn.setY(backButton.getY()-(bmback.getHeight()-bmshow.getHeight()/2)+3);
                reloadbtn.setX(0);
                reloadbtn.setY(backButton.getY() + bmshow.getHeight()/2+3);
                backButton.SetAttachCompents(closebtn,reloadbtn);

                Log.e("test======", "btnback.getHeight()" + bmback.getHeight() + ", showbtn.getHeight()  = " + bmshow.getHeight());

                closebtn.setOnClickListener(new  View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("backButtononClick", "backButtononClick");
                        AlertDialog.Builder builder = new AlertDialog.Builder(yallaActivity);  //先得到构造器
                        builder.setTitle("提示"); //设置标题
                        builder.setMessage("是否确认退出?"); //设置内容
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //回调,释放lua函数
                                Cocos2dxLuaJavaBridge.callLuaFunctionWithString(luaFunc, "success");
                                Cocos2dxLuaJavaBridge.releaseLuaFunction(luaFunc);

                                //释放所有资源
                                if(webView != null){
                                    ViewGroup vg = (ViewGroup) webView.getParent();
                                    if(vg != null){
                                        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                                        webView.clearHistory();
                                        vg.removeView(webView);
                                        webView.destroy();
                                        webView = null;

                                        vg.removeView(backButton);
                                        vg.removeView(closebtn);
                                        vg.removeView(reloadbtn);
                                        backButton = null;
                                        closebtn = null;
                                        reloadbtn = null;
                                    }
                                }

                                dialog.dismiss(); //关闭dialog
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                });

                reloadbtn.setOnClickListener(new View.OnClickListener() {//按钮回调
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onClick(View view) {
                        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                        webView.clearHistory();
                        webView.loadUrl(url);
                    }
                });

            }
        };
        yallaActivity.runOnUiThread(runnable);
    }

    //分享回调才需要
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        YGCommonApi.setCallback(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

