package com.examp.yllgame;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yllgame.sdk.message.YGMessageApi;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //判断是否是SDK内部消息，是的话 就返回true 返回false则表示非SDK内部消息 需游戏方自行处理
        if (!YGMessageApi.getInstance().handlePushMessage(remoteMessage))
            Log.d("FirebaseMessaging", "游戏内部消息");
        super.onMessageReceived(remoteMessage);
    }
}
