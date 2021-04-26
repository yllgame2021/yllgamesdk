
local YllGameHelper = class("YllGameHelper")
local targetPlatform = cc.Application:getInstance():getTargetPlatform()
local activityClassName = "org/cocos2dx/lua/AppActivity"

YllGameHelper.Language = "ar"
--登陆
function YllGameHelper:login()
	local callback = handler(self, self.onLoginCallBack)
	if device.platform == "android" then
		local luaj = require "cocos.cocos2d.luaj"
		local args = {callback}
		local signs = "(I)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "login", args, signs)
		if not ok then
			print("call init fail"..ret)
		end
	elseif(device.platform == "ios") then
		local args = {
			luaFun = callback
		}
		local luaoc = require "cocos.cocos2d.luaoc"
		local className = "RootViewController"
		local result,version = luaoc.callStaticMethod(className,"login",args)
	else
		local jsonString = "{'loginCode':1,'openUserId':5316348,'nickName':'useeedr_5316348','accessToken':'123'}"
		callback(jsonString)
	end
end
--登陆回调
function YllGameHelper:onLoginCallBack(jsonString)
	dump(jsonString, "登陆回调成功")
	local userInfo = json.decode(jsonString)
	g_commonhandler:notifyEvent("luaLoginCallBack", userInfo)
end
--同步角色
function YllGameHelper:syncRoleInfo(roleid, serverID, callback)
	if device.platform == "android" then
		local luaj = require "cocos.cocos2d.luaj"
		local args = {roleid, serverID, callback}
		local signs = "(Ljava/lang/String;Ljava/lang/String;I)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "syncRoleInfo", args, signs)
		if not ok then
			print("call init fail"..ret)
		end
	elseif (device.platform == "ios") then
		local args = {
			rid = roleid,
			sid = serverID,
			luaFun = callback
		}
		local luaoc = require "cocos.cocos2d.luaoc"
		local className = "RootViewController"
		local result,version = luaoc.callStaticMethod(className,"syncRoleInfo",args)
	else
		callback("success")
	end
end
--修改昵称
function YllGameHelper:modifyName(callback)
	if device.platform == "android" then
		local luaj = require "cocos.cocos2d.luaj"
		local args = {callback}
		local signs = "(I)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "showModifyName", args, signs)
		if not ok then
			print("call init fail"..ret)
		end
	elseif (device.platform == "ios") then
		local args = {luaFun = callback}
		local luaoc = require "cocos.cocos2d.luaoc"
		local className = "RootViewController"
		local result,version = luaoc.callStaticMethod(className,"showModifyName",args)
	else
		callback("success")
	end
end
--设置
function YllGameHelper:showSetting(roleServiceID, roleID)
	if device.platform == "android" then
		local luaj = require "cocos.cocos2d.luaj"
		local args = {roleServiceID, roleID}
		local signs = "(Ljava/lang/String;Ljava/lang/String;)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "showSetting", args, signs)
		if not ok then
			print("call init fail"..ret)
		end
	elseif (device.platform == "ios") then
		local args = {
			sid = roleServiceID,
			rid = roleID,
		}
		local luaoc = require "cocos.cocos2d.luaoc"
		local className = "RootViewController"
		local result,version = luaoc.callStaticMethod(className,"showSetting",args)
	else
		local jsonString = "{'loginCode':0,'openUserId':5316348,'nickName':'useeedr_5316348'}"
		local userInfo = json.decode(jsonString)
		g_commonhandler:notifyEvent("luaLoginCallBack", userInfo)
	end
end
--获取SDK信息
function YllGameHelper:getSDKInfo()
	local str = "{'baseurl':'mac','versionname':'mac'}"
	if device.platform == "android" then
		local luaj = require "cocos.cocos2d.luaj"
		local args = {}
		local signs = "()Ljava/lang/String;"
		local ok,ret = luaj.callStaticMethod(activityClassName, "getSDKInfo", args, signs)
		if not ok then
			print("call init fail"..ret)
		else
			str = ret
		end
	elseif (device.platform == "ios") then
		local args = {}
		local luaoc = require "cocos.cocos2d.luaoc"
		local className = "RootViewController"
		local result,version = luaoc.callStaticMethod(className,"getSDKInfo",args)
	end
	return str
end
--客服
function YllGameHelper:showserviceChat(roleServiceID, roleID)
	if device.platform == "android" then
		local luaj = require "cocos.cocos2d.luaj"
		local args = {roleServiceID, roleID}
		local signs = "(Ljava/lang/String;Ljava/lang/String;)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "showserviceChat", args, signs)
		if not ok then
			print("call init fail"..ret)
		end
	elseif (device.platform == "ios") then
		local args = {
			rsid = roleServiceID,
			rid = roleID
		}
		local luaoc = require "cocos.cocos2d.luaoc"
		local className = "RootViewController"
		local result,version = luaoc.callStaticMethod(className,"showserviceChat",args)
	end
end
--设置语言
function YllGameHelper:setLanguage()
	if self.Language == "ar" then 
		self.Language = "en"
	else
		self.Language = "ar"
	end
	if device.platform == "android" then
		local luaj = require "cocos.cocos2d.luaj"
		local args = {self.Language}
		local signs = "(Ljava/lang/String;)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "setLanguage", args, signs)
		if not ok then
			print("call init fail"..ret)
		end
	elseif (device.platform == "ios") then
		local args = {
			lan = self.Language,
		}
		local luaoc = require "cocos.cocos2d.luaoc"
		local className = "RootViewController"
		local result,version = luaoc.callStaticMethod(className,"setLanguage",args)
	end
end
--获取商品列表
function YllGameHelper:getPayList(callback)
	local getUrl = "https://sdkapi.yallagame.com/v1/pay/product?ptype=1"
	if device.platform == "android" then
		getUrl = "https://sdkapi.yallagame.com/v1/pay/product?ptype=2"
	end
	local xhr = cc.XMLHttpRequest:new()
    xhr.responseType = cc.XMLHTTPREQUEST_RESPONSE_JSON
    xhr:setRequestHeader("Content-Type", "application/json")
    xhr:open("GET", getUrl)
    local function loginCallback()
        print("xhr.readyState is:", xhr.readyState, "xhr.status is: ", xhr.status)
        if xhr.readyState == 4 and (xhr.status >= 200 and xhr.status < 207) then
            local response = json.decode(xhr.response)
			if response.ResultCode == 1000 then 
				local data = response.Data
				callback(data)
			else
				print(response.ResultMsg)
			end
        else
            print("获取失败")
        end
    end
    xhr:registerScriptHandler(loginCallback)
    xhr:send()
end

--点击商品开始充值
function YllGameHelper:pay(roleID, roleServiceID, sku, price, pointID, callback)
	if device.platform == "android" then
		local luaj = require "cocos.cocos2d.luaj"
		local args = {roleID, roleServiceID, sku, price, pointID, callback}
		local signs = "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V"
		local ok,ret = luaj.callStaticMethod(activityClassName, "pay", args, signs)
		if not ok then
			print("call init fail"..ret)
		end
	elseif (device.platform == "ios") then
		local args = {
			rid = roleID,
			sid = roleServiceID,
			sku = sku,
			pri = price,
			pid = pointID,
			luaFun = callback
		}
		local luaoc = require "cocos.cocos2d.luaoc"
		local className = "RootViewController"
		local result,version = luaoc.callStaticMethod(className,"pay",args)
	else
		callback(pointID)
	end
end

return YllGameHelper
