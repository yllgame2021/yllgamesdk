
# 版本更新纪录
### SDK版本1.0.3
1. 第三方依赖库升级
```
//Facebook登陆（9.0.0版本升级到11.0.0）
api 'com.facebook.android:facebook-login:11.0.0'
//新增Facebook分享 
api 'com.facebook.android:facebook-share:11.0.0'
```
2. 新增Facebook好友列表和分享
3. 修改支付pay方法增加支付类型skuType
4.  SDK回调设置统一接口
### SDK版本1.0.2.2
1. 增加弱联网模式设置
## SDK版本1.0.2.1  
1. BuglySDK接入 
2. 客户端埋点事件调整 
3. FAQ增加直接跳转至在线客服的入口按钮 
4. 增加静默游客登录接口
5. 数据统计SDK版本更新 
```
api 'com.appsflyer:af-android-sdk:6.2.3@aar' 
api 'com.appsflyer:oaid:6.2.4' 
api 'com.android.installreferrer:installreferrer:2.2'
```
## SDK版本1.0.2 
1. 增加手机号绑定登陆 
2. 调整登陆弹窗的样式 
3. 增加了客户端埋点统计 
4. 华为渠道登陆、支付接入 
5. gradle 增加配置 api 'com.google.android.material:material:1.3.0'库
## SDK版本1.0.1 
1. 新增登陆界面,登陆广播新增用户昵称修改通知和退出登录，type更新，删除切换账号，检查账号状态
2. 新增FCM推送
3. 新增设置界面
4. 新增设置SDK语言
5. 新增在线客服
6. SDK限制minSdkVersion到安卓5.0+（Api 21+）和更新第三方SDK库

