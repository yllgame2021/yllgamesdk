# 埋点需求(Android)

## 1.更新游戏开始
- 上报时机：`启动游戏开始安装更新时触发`
- EventName(事件名): `Android:YGEventConstants.YG_GAME_UPDATE_BEGIN`
- 事件参数: `无`
- 统计说明: `当游戏有版本更新时，更新起始位置做一次时间戳统计记录，单位：毫秒。并且记录更新`
- 示例: 
```java
YGEventApi.onEvent(YGEventConstants.YG_GAME_UPDATE_BEGIN);
```

## 2.更新游戏结束
- 上报时机：`启动游戏安装更新完成时触发`
- EventName(事件名): `Android:YGEventConstants.YG_GAME_UPDATE_END`
- 事件参数: `tt：总耗时时间戳`
- 统计说明: `当更新结束时，计算开始时间-结束时间的耗时，并上报`
- 示例: 
```java
HashMap<String, String> updateEnd = new HashMap<>();
updateEnd.put("tt", tt);
YGEventApi.onEvent(YGEventConstants.YG_GAME_UPDATE_END,updateEnd);
```

## 3.游戏服登录成功
- 上报时机：`收到socket登录成功时触发`
- EventName(事件名): `Android:YGEventConstants.YG_GAME_SOCKET_LOGIN_SUCCESS`
- 事件参数: `si：区服id`
- 统计说明: `无`
- 示例: 
```java
HashMap<String, String> gameLoginSuccess_socket = new HashMap<>();
gameLoginSuccess_socket.put("si", si);
YGEventApi.onEvent(YGEventConstants.YG_GAME_SOCKET_LOGIN_SUCCESS,gameLoginSuccess_socket);
```

## 4.游戏服登录失败
- 上报时机：`收到socket登录失败时触发`
- EventName(事件名): `Android:YGEventConstants.YG_GAME_SOCKET_LOGIN_FAILED`
- 事件参数: `si：区服id, fm:失败原因`
- 统计说明: ``
- 示例: 
```java
HashMap<String, String> gameLoginFailed_socket = new HashMap<>();
gameLoginFailed_socket.put("si", si);
gameLoginFailed_socket.put("fm", fm);
YGEventApi.onEvent(YGEventConstants.YG_GAME_SOCKET_LOGIN_FAILED,gameLoginFailed_socket);
```

## 5.游戏服务器登陆校验
- 上报时机：`游戏方服务器登陆校验成功时触发`
- EventName(事件名): `Android:YGEventConstants.YG_GAME_USER_LOGIN_SUCCESS`
- 事件参数: `无`
- 统计说明: `当登陆流程走到游戏方服务端校验，成功上报`
- 示例: 
```java
YGEventApi.onEvent(YGEventConstants.YG_GAME_USER_LOGIN_SUCCESS,new HashMap());
```

## 6.游戏服务器登陆校验
- 上报时机：`游戏方服务器登陆校验失败时触发`
- EventName(事件名): `Android:YGEventConstants.YG_GAME_USER_LOGIN_FAILED`
- 事件参数: `fm:失败原因`
- 统计说明: `当登陆流程走到游戏方服务端校验，失败上报`
- 示例: 
```java
HashMap<String, String> gameLoginFailed = new HashMap<>();
gameLoginFailed.put("fm", fm);
YGEventApi.onEvent(YGEventConstants.YG_GAME_USER_LOGIN_FAILED,gameLoginFailed);
```

## 7.创建角色
- 上报时机：`创建角色成功`
- EventName(事件名): `Android:YGEventConstants.YG_GAME_ROLE_CREATE`
- 事件参数: `无`
- 统计说明: `创角上报`,此埋点需要在同步角色之后上报。
- 示例: 
```java
 YGEventApi.onEvent(YGEventConstants.YG_GAME_ROLE_CREATE);
 ```

## 8.角色进入游戏
- 上报时机：`选择角色加载游戏界面成功`
- EventName(事件名): `Android:YGEventConstants.YG_GAME_ENTER`
- 事件参数: `无`
- 统计说明: `进入游戏主界面上报`
- 示例: 
```java
 YGEventApi.onEvent(YGEventConstants.YG_GAME_ENTER);
 ```

## 9.角色等级升级
- 上报时机：`角色等级升级时触发`
- EventName(事件名): `Android:YGEventConstants.YG_GAME_ROLE_LEVEL_UP`
- 事件参数: `rl：角色等级`
- 统计说明: `角色等级升级时上报`
- 示例: 
```java
//    rl：角色等级
HashMap<String, String> data_ = new HashMap<>();
data_.put("rl", rl);
YGEventApi.onEvent(YGEventConstants.YG_GAME_ROLE_LEVEL_UP, data_);
```

## 10. 角色VIP升级
- 上报时机：`角色VIP升级时触发`
- EventName(事件名): `game_vip_level_up`
- 事件参数: `vl：vip等级`
- 统计说明: `角色vip升级时上报`
- 示例: 
```java
//    vl：vip等级
HashMap<String, String> data_ = new HashMap<>();
data_.put("vl", vl);
YGEventApi.onEvent("game_vip_level_up", data_);
```

## 11. 自定义埋点
- 上报时机：`游戏方在合适的时候自行调用`
- EventName(事件名): `String类型，需要统计的事件`
- 埋点需要上报的参数`"key1": val1`
- 统计说明: `游戏方在合适的时候自行调用`
- 示例: 
```java
HashMap<String, String> data_ = new HashMap<>();
data_.put("key1", val1);
YGEventApi.onEvent(EventName, data_);
```
