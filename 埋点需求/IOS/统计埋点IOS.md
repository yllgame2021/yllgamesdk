# 埋点需求(IOS)

## 1.更新游戏开始
- 上报时机：`启动游戏开始安装更新时触发`
- EventName(事件名): `YGEventGameUpdateBegin`
- Info属性简写: `无`
- 统计说明: `当游戏有版本更新时，更新起始位置做一次时间戳统计记录，单位：毫秒。并且记录更新`
- 示例: 
```obj-c
[[YGEventManager getInstance] onEvent:YGEventGameUpdateBegin params:nil];
```

## 2.更新游戏结束
- 上报时机：`启动游戏安装更新完成时触发`
- EventName(事件名): `YGEventGameUpdateEnd`
- Info属性简写: `tt：总耗时时间戳`
- 统计说明: `当更新结束时，计算开始时间-结束时间的耗时，并上报`
- 示例: 
```obj-c
[[YGEventManager getInstance] onEvent:YGEventGameUpdateEnd params:@{@"tt": @"tt"}];
```

## 3.游戏服登录成功
- 上报时机：`收到socket登录成功时触发`
- EventName(事件名): `YGEventGameSocketLoginSuccess`
- Info属性简写: `si：区服id`
- 统计说明: `无`
- 示例: 
```obj-c
[[YGEventManager getInstance] onEvent:YGEventGameSocketLoginSuccess params:@{@"si": @"si"}];
```

## 4.游戏服登录失败
- 上报时机：`收到socket登录失败时触发`
- EventName(事件名): `YGEventGameSocketLoginFailed`
- Info属性简写: `si：区服id, fm:失败原因`
- 统计说明: ``
- 示例: 
```obj-c
[[YGEventManager getInstance] onEvent:YGEventGameSocketLoginFailed params:@{@"si": @"si", @"fm": @"fm"}];
```

## 5.游戏服务器登陆校验
- 上报时机：`游戏方服务器登陆校验成功时触发`
- EventName(事件名): `YGEventGameUserLoginSuccess`
- Info属性简写: `无`
- 统计说明: `当登陆流程走到游戏方服务端校验，成功上报`
- 示例: 
```obj-c
 [[YGEventManager getInstance] onEvent:YGEventGameUserLoginSuccess params:nil];
 ```

## 6.游戏服务器登陆校验
- 上报时机：`游戏方服务器登陆校验失败时触发`
- EventName(事件名): `YGEventGameUserLoginFailed`
- Info属性简写: `fm:失败原因`
- 统计说明: `当登陆流程走到游戏方服务端校验，失败上报`
- 示例: 
```obj-c
[[YGEventManager getInstance] onEvent:YGEventGameUserLoginFailed params:@{@"fm": @"fm"}];
```

## 7.创建角色
- 上报时机：`创建角色成功`
- EventName(事件名): `YGEventGameRoleCreate`
- Info属性简写: `无`
- 统计说明: `创角上报`
- 示例: 
```obj-c
[[YGEventManager getInstance] onEvent:YGEventGameRoleCreate params:nil];
 ```

## 8.角色进入游戏
- 上报时机：`选择角色加载游戏界面成功`
- EventName(事件名): `YGEventGameEnter`
- Info属性简写: `无`
- 统计说明: `进入游戏主界面上报`
- 示例: 
```obj-c
[[YGEventManager getInstance] onEvent:YGEventGameEnter params:nil];
 ```

## 9.角色等级升级
- 上报时机：`角色等级升级时触发`
- EventName(事件名): `YGEventGameRoleLevelUp`
- Info属性简写: `无`
- 统计说明: `角色等级升级时上报`
- 示例: 
```obj-c
[[YGEventManager getInstance] onEvent:YGEventGameRoleLevelUp params:nil];
```
