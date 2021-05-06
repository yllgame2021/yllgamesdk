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

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.android.billingclient.api.BillingClient;
import com.yllgame.sdk.BuildConfig;
import com.yllgame.sdk.YllGameSdk;
import com.yllgame.sdk.listener.YGBooleanCallBack;
import com.yllgame.sdk.login.YGLoginApi;
import com.yllgame.sdk.pay.YGPayApi;
import com.yllgame.sdk.pay.YGPaymentListener;
import com.yllgame.sdk.ui.dialog.listener.UpdateUserNameListener;
import com.yllgame.sdk.user.YGUserApi;
import com.yllgame.sdk.utils.LogUtils;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import org.cocos2dx.lua.AppActivity;
import org.json.JSONException;
import org.json.JSONObject;


public class YallaActivity extends Cocos2dxActivity {

    private static Cocos2dxActivity cocos2dxActivity = null;
    //登陆回调，需要在登陆的时候传入lua函数
    private static int luaLoginCallBack = -1;
    //修改昵称回调
    private static int modifyNameCallBack = -1;
    //同步角色回调
    private static int syncRoleCallBack = -1;
    //支付回调
    private static int payCallBack = -1;
    // 确保 init 在 UI 线程调用
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cocos2dxActivity = this;
    }

    //登陆界面
    public static void login(final int luaFunc) {
        luaLoginCallBack = luaFunc;
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                YGLoginApi.getInstance().login(cocos2dxActivity);
            }
        });
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
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                YGUserApi.getInstance().openAccountManager(cocos2dxActivity);
            }
        });
    }
    //修改昵称
    public static void showModifyName(final int luaFunc){
        modifyNameCallBack = luaFunc;
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                YGUserApi.getInstance().showUpdateNickNameDialog(cocos2dxActivity, new UpdateUserNameListener() {
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
//                                    Cocos2dxLuaJavaBridge.releaseLuaFunction(modifyNameCallBack);
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    //设置界面
    public static void showSetting(String roleServiceID, String roleID){
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                YGUserApi.getInstance().showSettingsView(cocos2dxActivity, roleServiceID, roleID);
            }
        });
    }
    //客服界面
    public static void showserviceChat(String roleServiceID, String roleID) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                YGUserApi.getInstance().showServiceChatView(cocos2dxActivity, roleServiceID, roleID);
            }
        });
    }
    //设置语言
    public static void setLanguage(String language) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                YllGameSdk.setLanguage(language);
            }
        });
    }
    //同步角色
    // roleId：角色id ：Int 必要参数
    // roleName：角色名称 ：string 必要参数
    // roleLevel：角色等级 ：int 必要参数
    // roleVipLevel：角色Vip等级 ：int 非必要 没有默认0
    // serverId：角色所在服务器id：int 必要参数
    // roleCastleLevel：城堡等级 ：int 非必要 没有默认0
    //  CommonBooleanCallBack：同步角色回调
    public static void syncRoleInfo(String roleId, String serverId, final int luaFunc) {
        syncRoleCallBack = luaFunc;
        YGUserApi.getInstance().syncRoleInfo(roleId, "5", "5", "5", serverId, "5", new YGBooleanCallBack() {
            @Override
            public void onResult(boolean b) {
                //回调，同步角色成功or失败
                if (b) {
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
    public static void pay(String roleID, String roleServiceID, String sku, String price , String pointID, final int luaFunc) {
        payCallBack = luaFunc;
//        LogUtils.logEForDeveloper("roleID:"+roleID+",roleServiceID:"+roleServiceID+",sku:"+sku+",price:"+price+",pointID:"+pointID);
        YGPayApi.pay(cocos2dxActivity, roleID,
                roleServiceID, sku, System.currentTimeMillis() + "",
                System.currentTimeMillis() + "", 1 + "", price, pointID,
                new YGPaymentListener() {
                    @Override
                    public void paymentSuccess() {
                        LogUtils.logEForDeveloper("支付成功");
                        Cocos2dxLuaJavaBridge.callLuaFunctionWithString(payCallBack, pointID);
//                        Cocos2dxLuaJavaBridge.releaseLuaFunction(payCallBack);
                    }

                    @Override
                    public void paymentFailed(int code) {
                        if (code == BillingClient.BillingResponseCode.USER_CANCELED)
                            LogUtils.logEForDeveloper("用户取消");
                        else if (code == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED)
                            LogUtils.logEForDeveloper("用户已购买未消耗");
                    }
                });
    }
}

