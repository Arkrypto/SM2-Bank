

function NTLSPlugin() {
  this.pluginExist = false; // 插件是否存在（奥联密钥客户端）
  this._events = {}; // 存储事件
  this.websocketUrl = ''; // 连接的websocket地址
  this.ws_url = 'ws://127.0.0.1:10021';
  this.wss_url = 'wss://127.0.0.1:10022';
  this.hasConnectSuccess = false; // websocket是否有连接成功过
  this.currentReconnectFailedCount = 0; // 重连次数
  this.secondReconnectFailedCount = 0; // 二次重连次数
  this.maxReconnectCount = 5; // 最大重连次数
  this.wsObj = null; // websocket 实例对象

  // 初始化业务功能
  this._initFunc();
}

/**
 * 初始化websocket
 * @param onOpen websocket open事件
 * @param onMessage websocket message事件
 * @param onError websocket error事件
 * @param onClose websocket close事件
 * @returns {NTLSPlugin|boolean}
 */
NTLSPlugin.prototype.websocketInit = function (onOpen, onMessage, onError, onClose) {
  var that = this;
  var wsObj = null;
  if (this.wsObj) {
    console.error('请先关闭当前websocket再初始化！');
    return false;
  }
  if (!this.websocketUrl) {
    var protocol = window.location.protocol;
    if (protocol == 'https:') {
      this.websocketUrl = this.wss_url;
    } else {
      this.websocketUrl = this.ws_url;
    }
  }
  console.log('websocket 请求链接: ', this.websocketUrl);
  wsObj = this.wsObj = new WebSocket(this.websocketUrl);

  wsObj.onopen = function (event) {
    console.log('websocket 连接成功');
    that.secondReconnectFailedCount = 0;
    that.currentReconnectFailedCount = 0;
    that.hasConnectSuccess = true;
    that.pluginExist = true;

    if (typeof onOpen == 'function') {
      onOpen();
    }
  };

  wsObj.onmessage = function (event) {
    var message = event.data;
    if (message == null || message == undefined) {
      console.log("websocket 响应为空");
      return;
    }
    console.log("websocket 接收数据: " + message);
    if(typeof write_log === 'function'){
      write_log('websocket接收数据：' + message, undefined);
    }
    try {
      var res = JSON.parse(message);
      if (typeof res != "object") {
        console.log("websocket 响应内容不是object对象");
        return;
      }
      var action = res.action;
      // action对应的回调
      var actionCallback = that._events[action] || function () {
      };
      console.log('action对应的回调', action, res, that._events);
      var result = {
        code: 0,
        data: null,
        msg: ''
      };
      that._events[action] = null;

      if (typeof onMessage == 'function') {
        onMessage(res);
      }
      if (res.code != 0) {
        console.log('错误信息：' + res.message + ', 错误码：' + res.code, false);
        result.msg = res.message;
        result.code = res.code;
        // 这里做个兼容，防止响应回来的消息没有action
        if (typeof action === 'undefined') {
          var events = that._events;
          for (var attr in events) {
            if (typeof events[attr] === 'function') {
              events[attr](result);
            }
          }
        } else {
          actionCallback(result);
        }
        return;
      }
      result.data = res.data;
      actionCallback(result);
    } catch (e) {
      console.error(e);
    }
  };

  wsObj.onclose = function (event) {
    console.log("websocket 连接关闭");
    that.pluginExist = false;
    that.wsObj = null;
    if (typeof onClose == 'function') {
      onClose();
    }
  };

  wsObj.onerror = function (event) {
    console.log("websocket 错误信息类型: " + event.type);
    that.pluginExist = false;
    // 尝试二次连接
    if (that.secondReconnectFailedCount < that.maxReconnectCount) {
      that.close();
      that.secondReconnectFailedCount++;
      console.log('1.5秒后尝试重新连接' + that.websocketUrl + '，第' + that.secondReconnectFailedCount + '次，共' + that.maxReconnectCount + '次，请稍等...');

      var timer = setTimeout(function () {
        clearTimeout(timer);
        that.websocketInit(onOpen, onMessage, onError, onClose);
      }, 1500);
    } else {
      // 二次连接还失败的话则失败了
      that.pluginExist = false;
      if (typeof onError == 'function') {
        onError();
      }
    }
  };

  return this;
}

/**
 * 发送消息
 * @param message 消息内容
 * @returns {NTLSPlugin}
 */
NTLSPlugin.prototype.sendMessage = function (message) {
  var that = this;
  var timer;
  // 待发送待消息内容
  var send_message_data = JSON.stringify(message);
  var send_msg = function () {
    if (timer) {
      clearTimeout(timer);
    }
    var wsObj = that.wsObj;
    var wsStatus = wsObj && wsObj.readyState;
    if (wsStatus == 0) { // 连接中
      timer = setTimeout(send_msg, 500);
    } else if (wsStatus == 1) { // 可用
      console.log('websocket 发送数据:' + send_message_data);
      if(typeof write_log === 'function'){
        write_log('websocket发送数据：' + send_message_data);
      }
      wsObj.send(send_message_data);
    } else { // 连接已断开或正在断开, 重连
      // 尝试连接n次，连接不上断开
      if(that.currentReconnectFailedCount < that.maxReconnectCount){
        that.currentReconnectFailedCount++;
        console.log('（发送消息尝试连接）第' + that.currentReconnectFailedCount + '次，共' + that.maxReconnectCount + '次，请稍等...');
        var resendFn = function () {
          that.sendMessage(message);
        };
        that.websocketInit(resendFn, null, resendFn);
      }else{
        that.close();
        throw new Error('插件无法连接，消息发送失败！');
      }
    }
  };

  send_msg();
  return this;
}

/**
 * 关闭websocket连接
 * @returns {NTLSPlugin}
 */
NTLSPlugin.prototype.close = function () {
  if (this.wsObj) {
    try {
      this.pluginExist = false;
      this.wsObj.close();
      this.wsObj = null;
    } catch (e) {
      console.error(e);
    }
  }
  return this;
}

/**
 * 添加事件
 * @param eventName 事件名称
 * @param eventFn 事件回调
 * @returns {NTLSPlugin}
 */
NTLSPlugin.prototype.addEvent = function (eventName, eventFn) {
  console.log('addEvent', this)
  this._events[eventName] = eventFn;
  return this;
}

/**
 * 检查是否是手机端
 * @returns {boolean}
 */
NTLSPlugin.prototype.isMobile = function () {
  var osInfo = this.getOsInfo();
  return osInfo.name == 'Android' || osInfo.name == 'iPhone';
}
/**
 * 判断浏览器是否支持websocket
 * @returns {boolean}
 */
NTLSPlugin.prototype.browserSupport = function () {
  var browserInfo = this.getBrowser();
  console.log('浏览器类型：' + browserInfo.name);
  if ('WebSocket' in window) {  // < ie10  use sockjs
    return true;
  } else {
    if (browserInfo.name == 'IE' && browserInfo.version < 10) { // is ie
      // 低版本浏览器用 第三方插件支持websocket
      return false;
    } else {
      return true;
    }
  }
}

/**
 * 获取浏览器信息
 * @returns {{name: string, version: undefined}}
 */
NTLSPlugin.prototype.getBrowser = function() {
  var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串

  var isIE11 = userAgent.toLowerCase().match(/rv:([\d.]+)\) like gecko/); // IE 11中userAgent已经不包含'msie'所以用'msie'不能判断IE 11
  var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
  var isIE = (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) || isIE11; //判断是否IE浏览器
  var isEdge = userAgent.indexOf("Edge") > -1; //判断是否IE的Edge浏览器
  var isFF = userAgent.indexOf("Firefox") > -1; //判断是否Firefox浏览器
  var isSafari = userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Chrome") == -1; //判断是否Safari浏览器
  var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1; //判断Chrome浏览器
  var browser = {
    name: '',
    version: undefined
  };
  if (isOpera) {
    browser.name = 'Opera';
  } else if (isChrome) {
    browser.name = 'Chrome';
  } else if (isSafari) {
    browser.name = 'Safari';
  } else if (isFF) {
    browser.name = 'Firefox';
  } else if (isEdge) {
    browser.name = 'Edge';
  } else if (isIE) {
    browser.name = 'IE';
  }


  if (isIE) {
    var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
    reIE.test(userAgent);
    var fIEVersion = parseFloat(RegExp["$1"]);
    if (fIEVersion == 7) {
      browser.version = 7;
    } else if (fIEVersion == 8) {
      browser.version = 8;
    } else if (fIEVersion == 9) {
      browser.version = 9;
    } else if (fIEVersion == 10) {
      browser.version = 10;
    } else if (isIE11) { // IE 11中userAgent已经不包含'msie'所以用'msie'不能判断IE 11
      browser.version = 11;
    }
  }

  return browser;
}

/**
 * 获取操作系统信息
 * @returns {{name: string, version: string}}
 */
NTLSPlugin.prototype.getOsInfo = function() {
  var userAgent = navigator.userAgent.toLowerCase();
  var name = 'Unknown';
  var version = 'Unknown';
  if (userAgent.indexOf('win') > -1) {
    name = 'Windows';
    if (userAgent.indexOf('windows nt 5.0') > -1) {
      version = 'Windows 2000';
    } else if (userAgent.indexOf('windows nt 5.1') > -1 || userAgent.indexOf('windows nt 5.2') > -1) {
      version = 'Windows XP';
    } else if (userAgent.indexOf('windows nt 6.0') > -1) {
      version = 'Windows Vista';
    } else if (userAgent.indexOf('windows nt 6.1') > -1 || userAgent.indexOf('windows 7') > -1) {
      version = 'Windows 7';
    } else if (userAgent.indexOf('windows nt 6.2') > -1 || userAgent.indexOf('windows 8') > -1) {
      version = 'Windows 8';
    } else if (userAgent.indexOf('windows nt 6.3') > -1) {
      version = 'Windows 8.1';
    } else if (userAgent.indexOf('windows nt 6.2') > -1 || userAgent.indexOf('windows nt 10.0') > -1) {
      version = 'Windows 10';
    } else {
      version = 'Unknown';
    }
  } else if (userAgent.indexOf('iphone') > -1) {
    name = 'iPhone';
  } else if (userAgent.indexOf('mac') > -1) {
    name = 'Mac';
  } else if (userAgent.indexOf('x11') > -1 || userAgent.indexOf('unix') > -1 || userAgent.indexOf('sunname') > -1 || userAgent.indexOf('bsd') > -1) {
    name = 'Unix';
  } else if (userAgent.indexOf('linux') > -1) {
    if (userAgent.indexOf('android') > -1) {
      name = 'Android';
    } else {
      name = 'Linux';
    }
  } else {
    name = 'Unknown';
  }
  return {name, version};
}

/**
 * 初始化业务功能
 * @returns {NTLSPlugin}
 */
NTLSPlugin.prototype._initFunc = function () {
  this.func = {};
  for(var attr in funcs) {
    this.func[attr] = funcs[attr].bind(this);
  }
  return this;
}

// 业务功能
var funcs = {
  /**
   * 枚举ukey中的用户
   * @param cb
   * @returns {NTLSPlugin}
   */
  enumerate_ukey_user: function (cb) {
    // 枚举UKEY
    var json = { action: "enumerate_ukey_user" };
    /*if (this._events.hasOwnProperty(json.action)) {
      return;
    }*/
    // 存储回调
    this.addEvent(json.action, cb);
    this.sendMessage(json);
    return this;
  },
  /**
   * 获取证书内容
   * @param keyindex
   * @param container
   * @param issigner
   * @param callback
   * @returns {NTLSPlugin}
   */
  get_cert_content: function (keyindex, container, issigner, callback) {
    if (!arguments[2]) {
      issigner = 1;
    }
    var json = { "action": "get_cert_content", "issigner": issigner, "keyindex": keyindex, "container": container };
    /*if (this._events.hasOwnProperty(json.action)) {
      return;
    }*/
    // 存储回调
    this.addEvent(json.action, callback);
    this.sendMessage(json);
    return this;
  },
  /**
   * 渔翁数据签名
   * @param keyindex
   * @param container
   * @param pass
   * @param challenge
   * @param callback
   * @returns {NTLSPlugin}
   */
  message_sign: function (keyindex, container, pass, challenge, callback) {
    // 渔翁数据签名
    var json = {
      "action": "message_sign",
      "pin": pass,
      "keyindex": keyindex,
      "container": container,
      "message": challenge
    };
    /*if (this._events.hasOwnProperty(json.action)) {
      return;
    }*/
    // 存储回调
    this.addEvent(json.action, callback);
    this.sendMessage(json);
    return this;
  },
  /**
   * sm2 签名
   * @param keyindex
   * @param container
   * @param pass
   * @param challenge
   * @param hashtype
   * @param format
   * @param callback
   * @returns {NTLSPlugin}
   */
  data_sm2_signature: function (keyindex, container, pass, challenge, hashtype, format, callback) {
    //sm2 签名
    if(typeof hashtype == undefined || hashtype == ''){
      hashtype = 'none';
    }
    if(typeof format == undefined || format == ''){
      format = 'none';
    }
    var json = {
      "action": "sm2_sign",
      "pin": pass,
      "keyindex": keyindex,
      "container": container,
      "message": challenge,
      "hashtype": hashtype,
      "format": format
    };
    /*if (this._events.hasOwnProperty(json.action)) {
      return;
    }*/
    // 存储回调
    this.addEvent(json.action, callback);
    this.sendMessage(json);
    return this;
  },
  /**
   * sm2 验签
   * @param challenge
   * @param signdata
   * @param cert
   * @param hashtype
   * @param callback
   * @returns {NTLSPlugin}
   */
  data_sm2_verifysign: function (challenge, signdata, cert, hashtype, callback) {
    //sm2 验签
    var json = {
      "action": "sm2_verifysign",
      "message": challenge,
      "hashtype": hashtype,
      "signdata": signdata,
      "cert": cert
    };
    /*if (this._events.hasOwnProperty(json.action)) {
      return;
    }*/
    // 存储回调
    this.addEvent(json.action, callback);
    this.sendMessage(json);
    return this;
  },
  /**
   * sm9 签名
   * @param keyindex
   * @param container
   * @param pass
   * @param challenge
   * @param hashtype
   * @param callback
   * @returns {NTLSPlugin}
   */
  data_sm9_signature: function (keyindex, container, pass, challenge, hashtype, callback) {
    //sm9 签名
    var json = {
      "action": "sm9_sign",
      "pin": pass,
      "keyindex": keyindex,
      "container": container,
      "message": challenge,
      "hashtype": hashtype
    };
    /*if (this._events.hasOwnProperty(json.action)) {
      return;
    }*/
    // 存储回调
    this.addEvent(json.action, callback);
    this.sendMessage(json);
    return this;
  },
  /**
   * sm9 验签
   * @param cn
   * @param challenge
   * @param signdata
   * @param cert_parameter
   * @param hashtype
   * @param callback
   * @returns {NTLSPlugin}
   */
  data_sm9_verifysign: function (cn, challenge, signdata, cert_parameter, hashtype, callback) {
    //sm9 验签
    var json = {
      "action": "sm9_verifysign",
      "cn": cn,
      "message": challenge,
      "hashtype": hashtype,
      "signdata": signdata,
      "parameter": cert_parameter
    };
    /*if (this._events.hasOwnProperty(json.action)) {
      return;
    }*/
    // 存储回调
    this.addEvent(json.action, callback);
    this.sendMessage(json);
    return this;
  },
  /**
   * 设置服务器url
   * @param combosignUrl
   * @param authUrl
   * @param callback
   * @returns {NTLSPlugin}
   */
  set_url(combosignUrl, authUrl = '', callback){
    // 设置服务器url
    var json = {
      "action": "set_url",
      "COMBOSIGNURL": combosignUrl, // 协同签名服务器地址
      "AUTHURL": authUrl // 认证服务器地址
    };
    /*if (this._events.hasOwnProperty(json.action)) {
      return;
    }*/
    // 存储回调
    this.addEvent(json.action, callback);
    this.sendMessage(json);
    return this;
  }
}

export var ntlsUtil = new NTLSPlugin();