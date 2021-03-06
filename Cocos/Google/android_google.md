# Cocos2d-x（Android_Google）接入文档

SDK下载地址(请联系对接人获取)
## 环境
- Cocos2d-x 3.17
- Android Studio 4.1.3
- Gradle Version 5.1.1
- Gradle Plugin Version 3.4.3


## 1.接入前项目检查

**根据以往游戏接入后出现的各种问题，YllSDK对游戏项目做以下几点建议：**

### 1.1修改 `gradle.properties` 文件中

```js
PROP_COMPILE_SDK_VERSION=28
PROP_MIN_SDK_VERSION=21
PROP_TARGET_SDK_VERSION=28
PROP_BUILD_TOOLS_VERSION=28.0.1
```

## 2.Android项目修改

### 2.1添加资源

- 将 Android 目录下的 src 文件夹下的内容拷贝到项目的 app/src 目录下
- 将 Android 目录下的 libs 文件夹下的 aar 文件拷贝到项目的 app/libs 目录下
- 导入 `YllGameHelper.lua` 文件到工程里，并在调用方法的时候引用，例如：

``` lua
  local YllGameHelper = require("app.models.YllGameHelper")
  --require里面是YllGameHelper.lua所在目录
```

### 2.2配置清单文件

- 在`res/string.xml`中添加自己的Facebook APPID和登陆ID,联系发行方提供。举例如果APPID为**15734567891011** 则:

```xml
<string name="facebook_app_id" translatable="false">15734567891011</string>
<string name="fb_login_protocol_scheme" translatable="false">fb15734567891011</string>
```

- 在项目中的`AndroidManifest`中`application`key中添加以下代码，并将相关信息改为自己实际的内容

```xml
<!-- YllSDK begin`````````````````````` -->
<activity
    android:name="com.facebook.FacebookActivity"
    android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
    android:label="@string/app_name" />
<activity
    android:name="com.facebook.CustomTabActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        
        <data android:scheme="@string/fb_login_protocol_scheme" />
    </intent-filter>
</activity>

<meta-data
    android:name="com.facebook.sdk.ApplicationId"
    android:value="@string/facebook_app_id" /> 
<!-- 注册SDK中登陆广播 用户登陆信息通过该广播返回 -->
<receiver
    android:name=".ygapi.YGLoginReceiver"
    android:exported="true">
    <intent-filter>
        <action android:name="com.yllgame.sdk.loginReceiver" />
    </intent-filter>
</receiver> 
<!-- Firebase 推送向光配置-->
<service
    android:name=".MyFirebaseMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
<!-- YllSDK end`````````````````````` -->

```

### 2.3配置gradle

修改APP的工程下`build.gradle`

- 将下面对应的value填入对应的key中

```js
android {
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
    packagingOptions {
        doNotStrip "*/*/libijiami*.so"
    }
    compileOptions {
        targetCompatibility JavaVersion.VERSION_1_8
        sourceCompatibility JavaVersion.VERSION_1_8
    }
    defaultConfig {
        //显示声明的支持
        javaCompileOptions { annotationProcessorOptions { includeCompileClasspath = true } }
    }
    sourceSets.main {
        assets.srcDirs = ['assets']
    }
}
```

- 在`dependencies`加入依赖库

```js
//Android X支持库  必须添加
api 'androidx.appcompat:appcompat:1.2.0'
api 'com.google.android.material:material:1.3.0'
//okhttp网络请求库 必须添加
api("com.squareup.okhttp3:okhttp:4.9.0")
//gson数据解析库 必须添加
api 'com.google.code.gson:gson:2.8.5'
//Facebook登陆依赖库 必须添加
api 'com.facebook.android:facebook-login:9.0.0'
//Google登陆依赖库 必须添加
api 'com.google.android.gms:play-services-auth:19.0.0'
api "com.google.android.gms:play-services-ads-identifier:17.0.0"
//Google支付依赖库 必须添加
api "com.android.billingclient:billing:3.0.0"
//数据库依赖库 必须添加
def room_version = "2.2.5"
api "androidx.room:room-runtime:$room_version"
api "androidx.room:room-compiler:$room_version"
api "net.zetetic:android-database-sqlcipher:4.4.2"
//数据统计依赖库 必须添加
api 'com.appsflyer:af-android-sdk:5.0.0'
api 'com.appsflyer:af-android-sdk:5.+'
api 'com.android.installreferrer:installreferrer:1.1'
//FCM 推送相关
api platform('com.google.firebase:firebase-bom:26.4.0')
api 'com.google.firebase:firebase-messaging'
api 'com.google.firebase:firebase-analytics'
//Bugly
api 'com.tencent.bugly:crashreport:3.3.92'
//SDK基础库,需要将名称改为libs文件夹里面的实际名称
implementation(name: 'YllGameSdk_1.0.1.1', ext: 'aar')
```

### 2.3 配置Google推送环境

1. 将`google-services.json`文件(运营方提供)放入`proj.android/app`目录，并检查其中配置是否与申请的一致
2. 修改`proj.android`文件夹下面的`build.gradle`文件，在`dependencies`中添加`classpath 'com.google.gms:google-services:4.3.3'`
3. 修改`proj.android/app`文件夹下面的`build.gradle`文件，在`apply plugin: 'com.android.application'`下面添加`apply plugin: 'com.google.gms.google-services'`

### 2.4升级gradle版本

某些功能需要使用5.0以上的gradle版本，所以需要升级项目gradle

File->project Structure打开窗口，选择project

设置Gradle Version为5.1.1

Gradle Plugin Version为对应的版本3.4.0

最新的是Plugin4.1.3  && version 6.5（不建议）

### 2.5常见报错

- ``“make: *** No rule to make target `cocos2dlua’. Stop.”``

修改`proj.android-studio/jni/Android.mk`中

```js
LOCAL_MODULE := cocos2dlua_shared
```

修改为

```js
LOCAL_MODULE := cocos2dlua 
```

- 资源丢失

gradle5之后，拷贝资源的方式发生了变化

修改`build.gradle`

修改前

```js
copy {
    from "${buildDir}/../../../../../res"
    into "${buildDir}/intermediates/assets/${variant.dirName}/res"
}

copy {
    from "${buildDir}/../../../../../src"
    into "${buildDir}/intermediates/assets/${variant.dirName}/src"
}
```

修改后

```js
copy {
    from "${buildDir}/../../../../../"
    include "res/**"
    include "src/**"
    into "${buildDir}/intermediates/assets/${variant.dirName}/"
}
```

## 3.SDK初始化与API接口

### 3.0 SDK初始化
- SDK初始化函数：``` YllGameSdk.getInstance().init(); ```
``` java 
 /**
     * 初始化
     *
     * @param application
     * @param appId            游戏的gameAppId
     * @param googleClientId   游戏的googleClientId
     * @param appsFlyersDevKey 游戏的appsFlyersDevKey
     * @param buglyId          游戏的buglyAppId
     */
    public void init(Application application, String appId, String googleClientId, String appsFlyersDevKey, String buglyAppId)
 ```

- 在`AndroidManifest.xml`的`application`中添加`android:name=“.MyAppLication"`
- 在`MyAppLication`的`Oncreator`中，为SDK初始化函数，修改自己对应的参数，完成SDK初始化
- 将 AppActivity继承自YllSDKActivity

### 3.1登陆与回调
```YGLoginApi.getInstance().login(cocos2dxActivity);```
``` java
    /**
     * 登陆界面
     *
     * @param activity 当前Activity
     */
    public void login(Activity activity)
```

如果配置正确，将会回调到**com.yllsdk.yllgame.**ygapi.YGLoginReceiver中，参照其中的状态码，通知自己游戏逻辑。(加粗部分改为实际包名)

注：项目中所有登陆以及切换账号都会通过广播通知并且在下发用户信息。

**YGLoginReceiver为固定写法，需要放在项目包名.ygapi下**

返回登陆失败和Token失效建议游戏内再调一次登陆Api重试！
退出登录要退出到登陆界面并且清除本地用户信息

### 3.2 静默游客登录
- SDK调起静默游客登陆函数：
``` YGLoginApi.getInstance().silentGuestLogin(); ```
``` java
    /**
     * 静默游客登录
     */
    public void silentGuestLogin()
```

### 3.3同步角色与回调
``` YGUserApi.getInstance().syncRoleInfo(); ```
``` java 
    /**
     * 同步角色信息
     *
     * @param roleId：角色id              ：int 必要参数
     * @param roleName：角色名称          ：string 必要参数
     * @param roleLevel：角色等级         ：int 必要参数
     * @param roleVipLevel：角色Vip等级   ：int 必要 没有默认0
     * @param serverId：角色所在服务器id   ：int 必要参数
     * @param roleCastleLevel：城堡等级   ：int 必要 没有默认0
     * @param callback：同步角色回调:       true 同步成功 false 同步失败
     */
    public void syncRoleInfo(String roleId,
                             String roleName,
                             String roleLevel,
                             String roleVipLevel,
                             String serverId,
                             String roleCastleLevel,
                             new YGBooleanCallBack() {
                                 @Override
                                 public void onResult(boolean b) {
                                     //回调，同步角色成功or失败，建议失败之后再次调用
                                     if (b) 
                                     else
                                 }
                             })
```

### 3.4 Google充值与回调
- SDK调起谷歌支付的函数为：`` YGPayApi.pay() ``
```java
/**
 * 支付
 *
 * @param activity      当前activity
 * @param roleId        角色id
 * @param roleServiceId 角色服务器Id
 * @param sku           商品的sku
 * @param cpNo          支付的订单号
 * @param cpTime        支付订单的创建时间
 * @param number        支付的数量 目前为1
 * @param amount        支付的金额
 * @param pointId       支付的充值点
 * @param listener      支付的回调
 */
YGPayApi.pay(cocos2dxActivity, roleID,roleServiceID, sku, cpno,cptime, number, price, pointID,
    new YGPaymentListener() {
        @Override
        public void paymentSuccess() {
            LogUtils.logEForDeveloper("支付成功");
        }

        @Override
        public void paymentFailed(int code) {
            if (code == BillingClient.BillingResponseCode.USER_CANCELED)
                LogUtils.logEForDeveloper("用户取消");
            else if (code == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED)
                LogUtils.logEForDeveloper("用户已购买未消耗");
        }
    }
});
```

### 3.4 打开客服界面

```java
YGUserApi.getInstance().showServiceChatView(cocos2dxActivity, roleServiceID, roleID);
```

### 3.5 打开SDK设置界面

```java
YGUserApi.getInstance().showSettingsView(cocos2dxActivity, roleServiceID, roleID);
```

### 3.6打开修改昵称界面

```java
YGUserApi.getInstance().showUpdateNickNameDialog(cocos2dxActivity, new UpdateUserNameListener() {
    public void onResult(boolean stat, String userName) {
        if(stat){
        //修改成功
        }
    }      
});
```

### 3.7打开用户管理界面

```java
YGUserApi.getInstance().openAccountManager(cocos2dxActivity);
```

### 3.8检查账号绑定

```java
YGLoginApi.getInstance().checkBindStat(cocos2dxActivity);
```

### 3.9设置SDK语言

```java
//调用设置SDK默认语言 ar：阿语 和 en：英语  如果没设置默认 就按照默认语言集合取第一个
YllGameSdk.setLanguageList(Arrays.asList("ar", "en"));
YllGameSdk.setLanguage("ar");
```

### 3.10获取SDK版本(非必要)

```java
YGLoginApi.getInstance().getVersionInfo();
```

### 3.11通用事件埋点
[YllSDK Android埋点文档](https://github.com/yllgame2021/yllgamesdk/blob/085c8905c886073dcd4e069ec2617084f2153600/%E5%9F%8B%E7%82%B9%E9%9C%80%E6%B1%82/Android/%E7%BB%9F%E8%AE%A1%E5%9F%8B%E7%82%B9Android.md)
```java
//event_name 参照埋点文档中的埋点名称，支持自定义
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
```

## 4.推送

### 4.1推送处理

推送处理在`MyFirebaseMessagingService.java`文件中，判断是否为SDK内部消息，然后进行处理

### 4.2获取推送token

```js
GMessageApi.getInstance().getPushToken()
```

