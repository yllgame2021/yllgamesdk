|上报事件 | 事件描述 | 上报时机 | EventName(事件名) | Info属性简写 | 统计说明 | 示例
| ------------ | ------------ | ------------ | ------------ | ------------ | ------------ |------------ |
|updateBegin | 更新游戏开始 | 启动游戏开始安装更新时触发 | YGEventGameUpdateBegin |  | 当游戏有版本更新时，更新起始位置做一次时间戳统计记录，单位：毫秒。并且记录更新 | [[YGEventManager getInstance] onEvent:YGEventGameUpdateBegin params:nil];
|updateEnd | 更新游戏结束 | 启动游戏安装更新完成时触发 | YGEventGameUpdateEnd | tt：总耗时时间戳 | 当更新结束时，计算开始时间-结束时间的耗时，并上报 | [[YGEventManager getInstance] onEvent:YGEventGameUpdateEnd params:@{@"tt": @"tt"}];
|gameLoginSuccess_socket | 游戏服登录成功 | 收到socket登录成功时触发 | YGEventGameSocketLoginSuccess | si：区服id" |  | [[YGEventManager getInstance] onEvent:YGEventGameSocketLoginSuccess params:@{@"si": @"si"}];
|gameLoginFailed_socket | 游戏服登录失败 | 收到socket登录失败时触发 | YGEventGameSocketLoginFailed |si：区服id<br>fm:失败原因" |  | [[YGEventManager getInstance] onEvent:YGEventGameSocketLoginFailed params:@{@"si": @"si", @"fm": @"fm"}];
|gameLoginSuccess | 游戏服务器登陆校验 | 游戏方服务器登陆校验成功时触发 | YGEventGameUserLoginSuccess |  | 当登陆流程走到游戏方服务端校验，成功上报 |  [[YGEventManager getInstance] onEvent:YGEventGameUserLoginSuccess params:nil];
|gameLoginFailed | 游戏服务器登陆校验 | 游戏方服务器登陆校验失败时触发 | YGEventGameUserLoginFailed | fm:失败原因" | 当登陆流程走到游戏方服务端校验，失败上报 | [[YGEventManager getInstance] onEvent:YGEventGameUserLoginFailed params:@{@"fm": @"fm"}];
|create | 创建角色 | 创建角色成功 | YGEventGameRoleCreate |  | 创角上报 | [[YGEventManager getInstance] onEvent:YGEventGameRoleCreate params:nil];
|enter | 角色进入游戏 | 选择角色加载游戏界面成功 | YGEventGameEnter |  | 进入游戏主界面上报 | [[YGEventManager getInstance] onEvent:YGEventGameEnter params:nil];
|upgrade | 角色等级升级 | 角色等级升级时触发 | YGEventGameRoleLevelUp |  | 角色等级升级时上报 | [[YGEventManager getInstance] onEvent:YGEventGameRoleLevelUp params:nil];
