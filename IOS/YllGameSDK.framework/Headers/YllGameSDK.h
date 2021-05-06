//
//  YllGameSDK.h
//  YllGame
//
//  Created by waha225 on 2020/12/18.
//

#import <UIKit/UIKit.h>
#import "YGUserInfoModel.h"
#import <Firebase/Firebase.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, YGPaymentFailedType) {
    YGPaymentFailedTypeError, // 支付失败
    YGPaymentFailedTypeCancel, // 取消支付
    YGPaymentFailedTypeTimeOut, // 请求超时
    YGPaymentFailedTypeDelivery, // 发货失败
    YGPaymentFaileTypeRepeat, // 相同SKU订单重复
    YGPaymentFaileTypePrisionPhone, // 不支持越狱手机
};

@protocol YllGameSDKDelegate <NSObject>

@required
/// 获取登录注册之后的用户信息
- (void)yg_getUserInfo:(nullable YGUserInfoModel *)userInfoModel;

@end

typedef void(^YGPaymentSuccess)(void);

typedef void(^YGPaymentFailed)(YGPaymentFailedType type, NSString *errorDescription);

@interface YllGameSDK: NSObject

+ (instancetype)getInstance;

#pragma mark Property
/// appId(必传)
@property (nonatomic, copy) NSString *gameAppId;
/// appsFlyerDevKey(必传)
@property (nonatomic, copy) NSString *appsFlyerDevKey;
/// appleAppId(必传)
@property (nonatomic, copy) NSString *appleAppId;
/// languageList 语言集合  游戏支持语言集合 现支持 ar 阿语 en 英语 该集合默认第一个是SDK的默认语言
@property (nonatomic, copy) NSArray <NSString *>*languageList;
/// 当前设置的语言, 不传以 languageList 的第一个值为默认语言, 若 languageList 为 null, 默认为 ar
@property (nonatomic, copy) NSString *localLanguage;

#pragma mark Delegate
/// 遵守协议
@property (nonatomic, weak) id<YllGameSDKDelegate> delegate;

#pragma mark Init
/// 初始化SDK (必调用) 
- (void)yg_init;
/// 获取 用户相关信息 (必调用)
/// 方法一:  需要结合 YllGameSDKDelegate 一起使用
- (void)yg_login;
/// 方法二:
- (void)yg_loginWithUserInfo:(void(^)(YGUserInfoModel *))userInfo;

- (void)yg_application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions;
- (BOOL)yg_application:(UIApplication *)application openURL:(NSURL *)url options:(nonnull NSDictionary<UIApplicationOpenURLOptionsKey, id> *)options;
- (BOOL)yg_application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray<id<UIUserActivityRestoring>> * _Nullable))restorationHandler;
- (void)yg_application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler;
- (void)yg_application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;
- (void)yg_applicationDidBecomeActive:(UIApplication *)application;

/// 同步游戏角色(游戏登录之后必须调用)
/// @param roleId 游戏角色
/// @param roleName 角色名
/// @param roleLevel 角色等级
/// @param roleVipLevel 角色vip等级
/// @param serverId 所在游戏服
/// @param roleCastleLevel 城堡等级
/// @param completeHandle  error == nil  成功 (SDKV1.0.1 版本新增结果回调)
- (void)yg_synchroRoleWithRoleId:(NSString *)roleId
                        roleName:(NSString *)roleName
                       roleLevel:(NSInteger)roleLevel
                    roleVipLevel:(NSInteger)roleVipLevel
                        serverId:(NSInteger)serverId
                 roleCastleLevel:(NSInteger)roleCastleLevel
                  completeHandle:(void(^)(NSError * _Nullable))completeHandle;

/// 创建支付清单
/// @param roleId 游戏角色Id
/// @param gameServerId 角色所在区服Id
/// @param cpno 订单号
/// @param cptime 订单生成时间
/// @param sku sku
/// @param amount amount
/// @param pointId 消费点Id
- (void)yg_createOrderWithRoleId:(NSString *)roleId
                    gameServerId:(double)gameServerId
                            cpno:(NSString *)cpno
                          cptime:(NSString *)cptime
                             sku:(NSString *)sku
                          amount:(NSString *)amount
                         pointId:(double)pointId
                    successBlock:(YGPaymentSuccess)successBlock
                     failedBlock:(YGPaymentFailed)failedBlock;

#pragma mark 其它
/// 检测SDK版本
- (void)yg_checkSDKVersion;
/// 检查游客账号是否绑定第三方账号, true == 绑定, false == 未绑定
- (BOOL)yg_checkBindState;
/// 获取SDK当前域名
- (NSString *)yg_getHost;
/// 获取firebase推送Token
- (void)yg_getPushToken:(void(^)(NSString * _Nullable, NSError * _Nullable))pushToken;

#pragma mark 页面相关
/// 展示账户管理
- (void)yg_showAccountManagementView;
/// 展示昵称修改页面
- (void)yg_showNicknameView;
/// 展示客服中心页面(SDKV1.0.1版本过期)
- (void)yg_showCustomerServiceView DEPRECATED_MSG_ATTRIBUTE("Please use yg_showServiceChatViewWithRoleId:gameServerId:");
/// 展示客服中心页面
/// @param roleId 角色Id
/// @param gameServerId 角色所在区服Id
- (void)yg_showServiceChatViewWithRoleId:(NSString *)roleId
                            gameServerId:(double)gameServerId;
/// 展示设置中心
/// @param roleId 角色Id
/// @param gameServerId 角色所在区服Id
- (void)yg_showSettingsViewWithRoleId:(NSString *)roleId
                         gameServerId:(double)gameServerId;
/// 展示登录页面
- (void)yg_showLoginView;

@end

NS_ASSUME_NONNULL_END
