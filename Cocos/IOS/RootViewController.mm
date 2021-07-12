/****************************************************************************
 Copyright (c) 2013      cocos2d-x.org
 Copyright (c) 2013-2016 Chukong Technologies Inc.
 Copyright (c) 2017-2018 Xiamen Yaji Software Co., Ltd.

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

#import "RootViewController.h"
#import "cocos2d.h"
#import "platform/ios/CCEAGLView-ios.h"
#include "scripting/lua-bindings/manual/platform/ios/CCLuaObjcBridge.h"


int loginCallBack;
@interface RootViewController() <YllGameSDKDelegate>

@end

@implementation RootViewController

+(RootViewController*) getInstance{
    return mInstance;
}
/*
 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if ((self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil])) {
        // Custom initialization
    }
    return self;
}
*/

// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView {
    // Initialize the CCEAGLView
    CCEAGLView *eaglView = [CCEAGLView viewWithFrame: [UIScreen mainScreen].bounds
                                         pixelFormat: (__bridge NSString *)cocos2d::GLViewImpl::_pixelFormat
                                         depthFormat: cocos2d::GLViewImpl::_depthFormat
                                  preserveBackbuffer: NO
                                          sharegroup: nil
                                       multiSampling: NO
                                     numberOfSamples: 0 ];
    
    // Enable or disable multiple touches
    [eaglView setMultipleTouchEnabled:NO];
    
    // Set EAGLView as view of RootViewController
    self.view = eaglView;
    mInstance = self;
}

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
}


// For ios6, use supportedInterfaceOrientations & shouldAutorotate instead
#ifdef __IPHONE_6_0
- (NSUInteger) supportedInterfaceOrientations{
    return UIInterfaceOrientationMaskAllButUpsideDown;
}
#endif

- (BOOL) shouldAutorotate {
    return YES;
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    [super didRotateFromInterfaceOrientation:fromInterfaceOrientation];

    auto glview = cocos2d::Director::getInstance()->getOpenGLView();

    if (glview)
    {
        CCEAGLView *eaglview = (__bridge CCEAGLView *)glview->getEAGLView();

        if (eaglview)
        {
            CGSize s = CGSizeMake([eaglview getWidth], [eaglview getHeight]);
            cocos2d::Application::getInstance()->applicationScreenSizeChanged((int) s.width, (int) s.height);
        }
    }
}

//fix not hide status on ios7
- (BOOL)prefersStatusBarHidden {
    return YES;
}

// Controls the application's preferred home indicator auto-hiding when this view controller is shown.
- (BOOL)prefersHomeIndicatorAutoHidden {
    return YES;
}

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];

    // Release any cached data, images, etc that aren't in use.
}

//登陆与回调
+(void) login:(NSDictionary *)dic{
    [[RootViewController getInstance] loginCall:dic];
}
-(void) loginCall:(NSDictionary *)dic{
    self.loginCallBack = [[dic objectForKey:@"luaFun"] intValue];
    [YllGameSDK getInstance].delegate = self;
    [[YllGameSDK getInstance] yg_loginWithUserInfo:^(YGUserInfoModel * userInfoModel) {
        //将返回码传入游戏层进行处理，返回码含义参照YGUserInfoModel类中YGState枚举
        NSDictionary *info=@{@"accessToken":userInfoModel.accessToken ,
                             @"nickName":userInfoModel.nickname,
                             @"openUserId":userInfoModel.userOpenId,
                             @"loginCode":[NSString stringWithFormat:@"%d",(int)userInfoModel.state]};
        NSError *error = nil;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:info
                                                              options:NSJSONWritingPrettyPrinted
                                                                error:&error];
        NSString *jsonString = [[NSString alloc] initWithData:jsonData
        encoding:NSUTF8StringEncoding];

        //将需要传递给 Lua function 的参数放入 Lua stack
        cocos2d::LuaObjcBridge::pushLuaFunctionById(self.loginCallBack);
        cocos2d::LuaObjcBridge::getStack()->pushString([jsonString UTF8String]);//返回json字串
        cocos2d::LuaObjcBridge::getStack()->executeFunction(1);//1个参数
     }];
}

//静默登陆与回调
+(void) loginGuest:(NSDictionary *)dic{
    [[RootViewController getInstance] loginCallGuest:dic];
}
-(void) loginCallGuest:(NSDictionary *)dic{
    self.loginCallBack = [[dic objectForKey:@"luaFun"] intValue];
    [YllGameSDK getInstance].delegate = self;
    [[YllGameSDK getInstance] yg_silentGuestLoginWithUserInfo:^(YGUserInfoModel * userInfoModel) {
        //将返回码传入游戏层进行处理，返回码含义参照YGUserInfoModel类中YGState枚举
        NSDictionary *info=@{@"accessToken":userInfoModel.accessToken ,
                             @"nickName":userInfoModel.nickname,
                             @"openUserId":userInfoModel.userOpenId,
                             @"loginCode":[NSString stringWithFormat:@"%d",(int)userInfoModel.state]};
        NSError *error = nil;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:info
                                                              options:NSJSONWritingPrettyPrinted
                                                                error:&error];
        NSString *jsonString = [[NSString alloc] initWithData:jsonData
        encoding:NSUTF8StringEncoding];

        //将需要传递给 Lua function 的参数放入 Lua stack
        cocos2d::LuaObjcBridge::pushLuaFunctionById(self.loginCallBack);
        cocos2d::LuaObjcBridge::getStack()->pushString([jsonString UTF8String]);//返回json字串
        cocos2d::LuaObjcBridge::getStack()->executeFunction(1);//1个参数
     }];
}


//客服
+(void) showserviceChat:(NSDictionary *)dic{
    NSString *rsid = [dic objectForKey:@"rsid"];
    NSString *rid = [dic objectForKey:@"rid"];
    [[YllGameSDK getInstance] yg_showServiceChatViewWithRoleId:rid gameServerId:rsid];
}

//同步角色
+(void) syncRoleInfo:(NSDictionary *)dic{
    [[RootViewController getInstance] syncRoleInfoCall:dic];
}
-(void) syncRoleInfoCall:(NSDictionary *)dic{
    NSString *roleid = [dic objectForKey:@"rid"];
    NSString *roleName = [dic objectForKey:@"rname"];
    NSString *roleLevel = [dic objectForKey:@"rlv"];
    NSString *roleVipLevel = [dic objectForKey:@"viplv"];
    NSString *serverId = [dic objectForKey:@"sid"];
    NSString *roleCastleLevel = [dic objectForKey:@"clv"];
    self.syncRoleCallBack = [[dic objectForKey:@"luaFun"] intValue];
    [[YllGameSDK getInstance] yg_synchroRoleWithRoleId:roleid roleName:roleName roleLevel:roleLevel roleVipLevel:roleVipLevel gameServerId:serverId roleCastleLevel:roleCastleLevel completeHandle:^(NSError * _Nonnull error) {
        if (!error) {
            //同步角色回调
            //将需要传递给 Lua function 的参数放入 Lua stack
            cocos2d::LuaObjcBridge::pushLuaFunctionById(self.syncRoleCallBack);
            cocos2d::LuaObjcBridge::getStack()->pushString("success");//返回同步成功
            cocos2d::LuaObjcBridge::getStack()->executeFunction(1);//1个参数
//            cocos2d::LuaObjcBridge::releaseLuaFunctionById(self.syncRoleCallBack);//释放
        }
    }];
}

//修改昵称
+(void) showModifyName:(NSDictionary *)dic{
    [[RootViewController getInstance] showModifyNameCall:dic];
}
-(void) showModifyNameCall:(NSDictionary *)dic{
    self.modifyNameCallBack = [[dic objectForKey:@"luaFun"] intValue];
    [[YllGameSDK getInstance] yg_showNicknameView];
}
        
//设置界面
+(void) showSetting:(NSDictionary *)dic{
    NSString *roleid = [dic objectForKey:@"rid"];
    NSString *serverId = [dic objectForKey:@"sid"];
    [[YllGameSDK getInstance] yg_showSettingsViewWithRoleId:roleid gameServerId:serverId];
}

//账号管理界面
+(void) showAccountManage:(NSDictionary *)dic{
    [[YllGameSDK getInstance] yg_showAccountManagementView];
}

//检查账号绑定
+(void) checkBind:(NSDictionary *)dic{
    [[YllGameSDK getInstance] yg_checkBindState];
}

//检查SDK版本
+(void) checkSDKInfo:(NSDictionary *)dic{
    [[YllGameSDK getInstance] yg_checkSDKVersion];
}

//获取SDK版本
+(NSString*) getSDKInfo:(NSDictionary *)dic{
    NSString *SDKHost = [[YllGameSDK getInstance] yg_getHost];
    return SDKHost;
}

//设置语言
+(void) setLanguage:(NSDictionary *)dic{
    NSString *lan = [dic objectForKey:@"lan"];
    [YllGameSDK getInstance].localLanguage = lan;
}

//设置SDK联网模式
+(void) setNetModel:(NSDictionary *)dic{
    int NetModel = [[dic objectForKey:@"net"] intValue];
    if (NetModel == 1) {
        [YllGameSDK getInstance].netMode = YGStrongNet;
    }
    else if (NetModel == 2) {
        [YllGameSDK getInstance].netMode = YGWeakNet;
    }
}

// 获取fb好友列表
+(void) getFriends:(NSDictionary *)dic{
    [[RootViewController getInstance] getfriends2:dic];
}
-(void) getfriends2:(NSDictionary *)dic{
    self.getFriendBack = [[dic objectForKey:@"luaFun"] intValue];
    [[YllGameSDK getInstance] yg_getFacebookFriendsWithCompleteHandle:^(NSArray<YGFBFriendInfoModel *> * _Nonnull friendsArray) {
        if(friendsArray.count > 0){
            NSString *str = [[RootViewController getInstance] friendArrayToJSON:friendsArray];
            //将需要传递给 Lua function 的参数放入 Lua stack
            cocos2d::LuaObjcBridge::pushLuaFunctionById(self.getFriendBack);
            cocos2d::LuaObjcBridge::getStack()->pushString([str.description UTF8String]);//返回获取信息
            cocos2d::LuaObjcBridge::getStack()->executeFunction(1);//1个参数
        }
    }];
}

//分享
+(void) shareToFB:(NSDictionary *)dic{
    [[RootViewController getInstance] shareToFB2:dic];
}
-(void) shareToFB2:(NSDictionary *)dic{
    NSString *quote = [dic objectForKey:@"quote"];
    NSString *link = [dic objectForKey:@"link"];
    self.shareBack = [[dic objectForKey:@"luaFun"] intValue];
    [[YGShareManager getInstance] shareLinkContentWithQuote:quote linkContent:link success:^{
        cocos2d::LuaObjcBridge::pushLuaFunctionById(self.shareBack);
        cocos2d::LuaObjcBridge::getStack()->pushString("success");//返回获取信息
        cocos2d::LuaObjcBridge::getStack()->executeFunction(1);//1个参数
    } cancel:^{
        cocos2d::LuaObjcBridge::pushLuaFunctionById(self.shareBack);
        cocos2d::LuaObjcBridge::getStack()->pushString("cancel");//返回获取信息
        cocos2d::LuaObjcBridge::getStack()->executeFunction(1);//1个参数
    } failed:^(NSError * _Nonnull) {
        cocos2d::LuaObjcBridge::pushLuaFunctionById(self.shareBack);
        cocos2d::LuaObjcBridge::getStack()->pushString("failed");//返回获取信息
        cocos2d::LuaObjcBridge::getStack()->executeFunction(1);//1个参数
    }];

}

+(void) copyToClipboard:(NSDictionary *)dic{
    NSString *str = [dic objectForKey:@"str"];
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    [pasteboard setString:str];
    
}

//测试崩溃
+(void) crashTest:(NSDictionary *)dic{
    NSString * test;
    NSDictionary *info=@{@"test":test};
}
//商品充值
+(void) pay:(NSDictionary *)dic{
    [[RootViewController getInstance] payCall:dic];
}
-(void) payCall:(NSDictionary *)dic{
    NSString *roleid = [dic objectForKey:@"rid"];
    NSString *serverId = [dic objectForKey:@"sid"];
    NSString *sku = [dic objectForKey:@"sku"];
    NSString *price = [dic objectForKey:@"pri"];
    NSString *pointID = [dic objectForKey:@"pid"];
    NSInteger skutype = [[dic objectForKey:@"skutype"] integerValue];
    self.payCallBack = [[dic objectForKey:@"luaFun"] intValue];
    YGPayType type;
    if (skutype == 0) {
        type = YGConsumePay;
    }
    else if (skutype == 1) {
        type = YGSubscribePay;
    }
    //其他参数
    NSDate* date = [NSDate dateWithTimeIntervalSinceNow:0];
    NSTimeInterval time=[date timeIntervalSince1970]*1000;
    NSString *timeString = [NSString stringWithFormat:@"%.0f", time];
    // 创建订单
    [[YllGameSDK getInstance] yg_createOrderWithRoleId:roleid gameServerId:serverId cpno:timeString cptime:timeString sku:sku amount:price pointId:pointID payType:type successBlock:^{
        cocos2d::LuaObjcBridge::pushLuaFunctionById(self.payCallBack);
        cocos2d::LuaObjcBridge::getStack()->pushString([pointID.description UTF8String]);//返回同步成功
        cocos2d::LuaObjcBridge::getStack()->executeFunction(1);//1个参数
        cocos2d::LuaObjcBridge::releaseLuaFunctionById(self.payCallBack);//释放
    } failedBlock:^(YGPaymentFailedType type, NSString * _Nonnull errorDescription) {
        
    }];
}

//自定义埋点事件
+(void) onEvent:(NSDictionary *)dic{
    NSString *evName = [dic objectForKey:@"evName"];
    NSString * jsStr = [dic objectForKey:@"jsStr"];
    NSDictionary *dicstr = [[RootViewController getInstance] dictForJSONString:jsStr];
    [[YGEventManager getInstance] onEvent:evName params:dicstr];
}

//str to dic
- (NSDictionary *)dictForJSONString:(NSString *)str
{
    NSData *jsonData = [str dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                        options:NSJSONReadingMutableContainers
                                                          error:nil];
    return dic;
}

//数组转为json
- (NSString *)friendArrayToJSON:(NSArray *)friendArr {
    if (friendArr && friendArr.count > 0) {
        
        NSMutableArray *arr = [NSMutableArray arrayWithCapacity:0];
        
        for (YGFBFriendInfoModel *model in friendArr) {
            NSDictionary *info=@{@"fbId":model.fbId ,
                                 @"userOpenId":model.userOpenId,
                                 @"name":model.name};
//                                 @"avatarUrl":model.avatarUrl};
//                                 @"avatarWidth":[NSString stringWithFormat:@"%d",(int)model.avatarWidth],
//                                 @"avatarHeight":[NSString stringWithFormat:@"%d",(int)model.avatarHeight]};
            NSData *jsonData = [NSJSONSerialization dataWithJSONObject:info options:0 error:nil];
            NSString *jsonText = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
            
            [arr addObject:jsonText];
        }
        
        return [self objArrayToJSON:arr];
    }
    
    return nil;
}
 
//把多个json字符串转为一个json字符串
- (NSString *)objArrayToJSON:(NSArray *)array {
    
    NSString *jsonStr = @"[";
    
    for (NSInteger i = 0; i < array.count; ++i) {
        if (i != 0) {
            jsonStr = [jsonStr stringByAppendingString:@","];
        }
        jsonStr = [jsonStr stringByAppendingString:array[i]];
    }
    jsonStr = [jsonStr stringByAppendingString:@"]"];
    
    return jsonStr;
}
 
@end
