//
//  YGUserInfoModel.h
//  YllGame
//
//  Created by waha225 on 2020/12/25.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSInteger, YGState) {
    YGTokenOverdue, // token过期, 重新登录
    YGChangeNickName, // 修改昵称成功
    YGSwitchSuccess, // 账号切换成功
    YGSwitchFailure, // 账户切换失败
    YGLoginSuccess, // 登录成功
    YGLoginFailure, // 登录失败
    YGAccountBlock, // 账号被封
    YGAccountRemote, // 异地登录
    YGLogout, // 退出登录
};


NS_ASSUME_NONNULL_BEGIN

@interface YGUserInfoModel : NSObject

/// 服务端验证token
@property (nonatomic, copy) NSString *accessToken;
/// 用户平台的唯一Id
@property (nonatomic, copy) NSString *userOpenId;
/// 用户昵称
@property (nonatomic, copy) NSString *nickname;
/// 账户变更类型和状态
@property (nonatomic, assign) YGState state;


@end

NS_ASSUME_NONNULL_END
