package com.examp.yllgame.ygapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yllgame.sdk.constants.YGConstants;
import com.yllgame.sdk.entity.GameUserInfoEntity;
import com.yllgame.sdk.utils.LogUtils;

import org.cocos2dx.lua.AppActivity;
import org.json.JSONException;
import org.json.JSONObject;

//import org.greenrobot.eventbus.EventBus;

/**
 * 登陆广播 1.0.1
 */
public class YGLoginReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.logEForDeveloper("登陆广播");
        //Constants.BROADCAST_RECEIVER_LOGIN_ACTION SDK中登陆的action
        if (intent.getAction() == YGConstants.BROADCAST_RECEIVER_LOGIN_ACTION) {
            //拿到登陆之后的用户信息
            GameUserInfoEntity userInfoEntity = (GameUserInfoEntity) intent.getExtras().getSerializable(YGConstants.BROADCAST_RECEIVER_LOGIN_INFO_KEY);
            JSONObject result = new JSONObject();
            try {
                result.put("loginCode", userInfoEntity.getType());
                result.put("openUserId", userInfoEntity.getOpenUserId());
                result.put("nickName", userInfoEntity.getNickName());
                result.put("accessToken", userInfoEntity.getAccessToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AppActivity.login_callBack(result.toString());
            //该示例中通过EventBus通知并且更新主界面的更新 具体的结合自身需求修改
            if (userInfoEntity.getType() == GameUserInfoEntity.TYPE_LOGIN_ACCOUNT_SUCCESS) {
                //登陆成功
//                EventBus.getDefault().post(userInfoEntity);
            } else if (userInfoEntity.getType() == GameUserInfoEntity.TYPE_LOGIN_ACCOUNT_FAIL) {
                //登陆失败 建议重新调取SDK的登陆函数
//                EventBus.getDefault().post("登陆失败");
            } else if (userInfoEntity.getType() == GameUserInfoEntity.TYPE_FAIL_ACCOUNT_REMOTE) {
                //账号异地登录 SDK内部会有弹窗 必须退出到登陆界面清除用户信息
//                EventBus.getDefault().post("异地登录");
            } else if (userInfoEntity.getType() == GameUserInfoEntity.TYPE_FAIL_ACCOUNT_BLOCKED) {
                //账号被封 SDK内部会有弹窗 建议退出到登陆界面
//                EventBus.getDefault().post("账号被封");
            } else if (userInfoEntity.getType() == GameUserInfoEntity.TYPE_FAIL_TOKEN_OVERDUE) {
                //账号Token过期  建议重新调取SDK的登陆函数
//                EventBus.getDefault().post("Token过期");
            } else if (userInfoEntity.getType() == GameUserInfoEntity.TYPE_ACCOUNT_CHANGE_USERNAME) {
                //修改昵称成功
//                EventBus.getDefault().post(userInfoEntity);
            } else if (userInfoEntity.getType() == GameUserInfoEntity.TYPE_ACCOUNT_LOGIN_OUT) {
                //退出登录
//                EventBus.getDefault().post("退出登录");
            }
        }
    }
}