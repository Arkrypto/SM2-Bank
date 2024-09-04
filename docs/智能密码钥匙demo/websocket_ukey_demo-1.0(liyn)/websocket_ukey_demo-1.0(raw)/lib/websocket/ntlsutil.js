var wss_obj, check_ntls_timer, check_sender_timer;
var startTime, endTime, respond_json_length = 0;
var send_message_data = null;
if (typeof console == "undefined") {
  this.console = {
    log: function (msg) {
    }
  };
}

var NTLSPro = {
  plugin_exist: false, // 是否存在插件
  once: false, //检查一次
  is_mobile: false, //检查移动端或者PC
  support_websocket: false, //检查浏览器是否支持websocket，ie10以下不支持websocket
  websocket_url: '',  //websoket url，如果为空自动识别https/http协议，配置后将不再自动识别
  download_plugin_div_id: 'download_olym_ukey_plugin',  //默认值
  device_list_div_id: 'DevicelistDivId', //默认值
  show_message_div_id: 'flashMessage', //显示错误信息ID
  connect_failed_count: 5,  //连接失败n次后断开连接
  current_connect_failed_count: 0,  //当前连接失败n次后断开连接
  ws_url: 'ws://127.0.0.1:10021',   //根据url协议判断默认使用的url,失败后自动切换到wss连接
  wss_url: 'wss://127.0.0.1:10022', //根据url协议判断默认使用的url,失败后自动切换到ws连接
  second_url: '', //失败后自动切换后的url
  second_connect_failed_count: 0, //重连失败次数,达到 connect_failed_count 次后断开
  has_connect_success: false, //连接成功后不再重连别的URL
  //ws_url: 'wss://127.0.0.1:10022',
  //wss_url: 'ws://127.0.0.1:10021',
  websocket_init: function () {
    //write_log('检测插件...');
    if (!NTLSPro.websocket_url) {
      var protocol = window.location.protocol;
      write_log('协议: ' + protocol);
      if (protocol == 'https:') {
        NTLSPro.websocket_url = NTLSPro.wss_url;
        NTLSPro.second_url = NTLSPro.ws_url;
      } else {
        NTLSPro.websocket_url = NTLSPro.ws_url;
        NTLSPro.second_url = NTLSPro.wss_url;
      }
    }
    write_log('websocket 请求链接: ' + NTLSPro.websocket_url);
    wss_obj = new WebSocket(NTLSPro.websocket_url);

    wss_obj.onopen = function (event) {
      NTLSPro.plugin_exist = true;
      write_log('websocket 连接成功');
      clearTimeout(check_ntls_timer);
      NTLSPro.second_connect_failed_count = 0;
      NTLSPro.current_connect_failed_count = 0;
      NTLSPro.has_connect_success = true;

      //当服务由未启动 变为  已启动
      if ($('#' + NTLSPro.download_plugin_div_id).css('display') == 'block') {
        $('#' + NTLSPro.download_plugin_div_id).hide();
        NTLSPro.enumerate_ukey_user();
      }
    };

    wss_obj.onmessage = function (event) {

      if (event.data == null) {
        write_log("websocket 响应为空");
        return;
      }
      var message = event.data;
      write_log("websocket 接收数据: " + message);

      var res = JSON.parse(message);
      if (typeof res != "object") {
        write_log("websocket 响应内容不是object对象");
        return;
      }
      if (res.code != 0) {
        $('#' + NTLSPro.show_message_div_id).html('错误信息：' + res.message + ', 错误码：' + res.code).show();
        write_log('错误信息：' + res.message + ', 错误码：' + res.code, false);
        return;
      }
      eval(res.action + "(" + message + ")");
    };

    wss_obj.onclose = function (event) {
      write_log("websocket 连接关闭");
    };

    wss_obj.onerror = function (event) {
      write_log("websocket 错误信息类型: " + event.type);
      websocket_connect_error();
      if (NTLSPro.once) {
        close_webwocket();
        //wss_obj.close();
        //clearTimeout(check_ntls_timer);
      }

    };

    function error_code (code) {
      var msg = '';
      switch (code) {
        case 1000 :
          msg = '正常关闭';
          break;
        case 1001 :
          msg = '终端离开, 可能因为端错误, 也可能因为浏览器正从打开连接的页面跳转离开';
          break;
        case 1002 :
          msg = '由于协议错误而中断连接';
          break;
        case 1003 :
          msg = '由于接收到不允许的数据类型而断开连接 (如仅接收文本数据的终端接收到了二进制数据).';
          break;
        case 1005 :
          msg = '没有收到预期的状态码';
          break;
        case 1006 :
          msg = '非正常关闭';
          break;
        case 1007 :
          msg = '由于收到了格式不符的数据而断开连接 (如文本消息中包含了非 UTF-8 数据)';
          break;
        case 1008 :
          msg = '由于收到不符合约定的数据而断开连接. 这是一个通用状态码, 用于不适合使用 1003 和 1009 状态码的场景';
          break;
        case 1009 :
          msg = '由于收到过大的数据帧而断开连接';
          break;
        case 1010 :
          msg = '客户端期望器商定一个或多个拓展, 但器没有处理, 因此客户端断开连接.';
          break;
        case 1011 :
          msg = '客户端由于遇到没有预料的情况阻止其完成请求, 因此端断开连接.';
          break;
        case 1012 :
          msg = '器由于重启而断开连接';
          break;
        case 1013 :
          msg = '器由于临时原因断开连接, 如器过载因此断开一部分客户端连接';
          break;
        case 1015 :
          msg = '表示连接由于无法完成 TLS 握手而关闭 (例如无法验证器证书).';
          break;
      }
      return msg;
    }

    function websocket_connect_error () {
      write_log("websocket 连接失败");
      if (NTLSPro.has_connect_success == false && NTLSPro.second_url != '') {
        close_webwocket();
        //二次连接尝试连接失败次数
        if (NTLSPro.second_connect_failed_count < NTLSPro.connect_failed_count) {
          NTLSPro.second_connect_failed_count++;
          write_log("切换连接：" + NTLSPro.second_url + '，尝试连接第' + NTLSPro.second_connect_failed_count + '次，共' + NTLSPro.connect_failed_count + '次，请稍等...');
          NTLSPro.websocket_url = NTLSPro.second_url;
          check_ntls_timer = setTimeout('NTLSPro.websocket_init()', 2000);
        }
      }
      if (NTLSPro.has_connect_success == false) {
        $('#default_notice').hide();
        //$('#'+NTLSPro.device_list_div_id).hide();
        $('#' + NTLSPro.download_plugin_div_id).show();
        $('#InsertKeyImageDiv').hide();
        NTLSPro.plugin_exist = false;
      }
    }
  }
  , websocket_status: function () {
    var ok = true;
    if (wss_obj.readyState != WebSocket.OPEN) {
      write_log("websocket 状态： 未连接, 状态码：" + wss_obj.readyState);
      ok = false;
      clearTimeout(check_sender_timer);
    }
    if (ok) {
      write_log("websocket 状态： 已连接");
    }
    return ok;
  }
  , mobile_detecte: function (str) {
    var ua = detect.parse(navigator.userAgent);
    var os = ua.os.family;
    if (os.indexOf('Android') > -1 || os.indexOf('iOS') > -1) { // is mobile
      write_log('移动端浏览器');
      NTLSPro.is_mobile = true;
      return false;
    } else {
      write_log('PC端浏览器');
      return true;
    }
  }
  , browser_detecte: function () {
    var ua = detect.parse(navigator.userAgent);
    var browser_type = ua.browser.family;
    var version = ua.browser.version;
    write_log('浏览器类型：' + ua.browser.name);

    if ('WebSocket' in window) {  // < ie10  use sockjs
      NTLSPro.support_websocket = true;
      return true;
    } else {
      if (browser_type == 'IE' && version < 10) { // is ie
        return false;
        //低版本浏览器用 第三方插件支持websocket
      } else {
        NTLSPro.support_websocket = true;
        return true;
      }
    }
  }
  , depend_detecte: function () {
    //依懒检测
    NTLSPro.mobile_detecte();
    NTLSPro.browser_detecte();
  }
  , send_message: function (message) {
    //发送消息
    send_message_data = JSON.stringify(message);
    check_sender_timer = setTimeout("send_msg()", 1000);
  }
  , enumerate_ukey_user: function () {
    //枚举UKEY
    var json = { action: "enumerate_ukey_user" };
    NTLSPro.send_message(json);
  }
  , get_cert_content: function (keyindex, container, issigner) {
    //获取证书内容
    if (!arguments[2]) {
      issigner = 1;
    }
    console.log(222);
    var json = { "action": "get_cert_content", "issigner": issigner, "keyindex": keyindex, "container": container };
    console.log('json', json);
    NTLSPro.send_message(json);
  }
  , message_sign: function (keyindex, container, pass, challenge) {
    //渔翁数据签名
    var json = {
      "action": "message_sign",
      "pin": pass,
      "keyindex": keyindex,
      "container": container,
      "message": challenge
    };
    NTLSPro.send_message(json);
  }
  , data_sm2_signature: function (keyindex, container, pass, challenge) {
    //sm2 签名
    var hashtype = $('#sm2_hashtype').val();
    var format = $('#sm2_format').val();
    var json = {
      "action": "sm2_sign",
      "pin": pass,
      "keyindex": keyindex,
      "container": container,
      "message": challenge,
      "hashtype": hashtype,
      "format": format
    };
    NTLSPro.send_message(json);
  }
  , data_sm2_verifysign: function (challenge, signdata, cert, hashtype) {
    //sm9 验签
    var json = {
      "action": "sm2_verifysign",
      "message": challenge,
      "hashtype": hashtype,
      "signdata": signdata,
      "cert": cert
    };
    NTLSPro.send_message(json);
  }
  , data_sm9_signature: function (keyindex, container, pass, challenge, hashtype) {
    //sm9 签名
    var json = {
      "action": "sm9_sign",
      "pin": pass,
      "keyindex": keyindex,
      "container": container,
      "message": challenge,
      "hashtype": hashtype
    };
    NTLSPro.send_message(json);
  }
  , data_sm9_verifysign: function (cn, challenge, signdata, cert_parameter, hashtype) {
    //sm9 验签
    var json = {
      "action": "sm9_verifysign",
      "cn": cn,
      "message": challenge,
      "hashtype": hashtype,
      "signdata": signdata,
      "parameter": cert_parameter
    };
    NTLSPro.send_message(json);
  }
};


//ie 11 及以下
function testActive () {
  write_log('使用IE ActiveXObject检测 ');
  try {
    var comActiveX = new ActiveXObject('OlymSslVpn.BhoIe.1');
  } catch (e) {
    write_log('本地未安装插件(' + e + ')');
    return false;
  }
  write_log('本地已安装插件');
  return true;
}

//停止websocket检测
function close_webwocket () {
  clearTimeout(check_sender_timer);
  try {
    clearTimeout(check_ntls_timer);
    if (typeof wss_obj != 'undefined') {
      wss_obj.close();
    }
  } catch (e) {
    write_log(e);
  }
  $('#ntls_websocket_server_check').attr('disabled', false);
  NTLSPro.plugin_exist = false;
}


function start_time () {
  var d = new Date();
  startTime = d.getTime();
  write_log('请求开始时间：' + startTime);
  return startTime;
}

function end_time () {
  var d = new Date();
  endTime = d.getTime();
  var uesTime = endTime - startTime;
  var sec = uesTime / 1000;
  write_log('本次请求用时：' + uesTime + 'ms, ' + sec + '秒');
  //write_log('性能：'+bytes_to_size(respond_json_length/uesTime*1000)+'/'+'秒');
  return uesTime;
}

function bytes_to_size (bytes) {
  if (bytes === 0) return '0 B';
  var k = 1024, sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
    i = Math.floor(Math.log(bytes) / Math.log(k));
  return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}


function sleep (delay) {
  var start = new Date().getTime();
  while (new Date().getTime() < start + delay) ;
}


//写日志
function write_log (log, status, needBr) {
  var len = $('#ntls_server_check_result').length;
  if (len > 0) {
    //$('#ntls_server_check_result').append($.trim(log)+"\n");
    if (arguments[1] === true) {
      $('#ntls_server_check_result').append("<font color='green'>" + $.trim(log) + "</font></br>");
    } else if (arguments[1] === false) {
      $('#ntls_server_check_result').append("<font color='red'>" + $.trim(log) + "</font></br>");

    } else {
      $('#ntls_server_check_result').append($.trim(log) + "</br>");
    }
    if(needBr){
      $('#ntls_server_check_result').append('</br>');
    }
    var height = $("#ntls_server_check_result")[0].scrollHeight;
    $("#ntls_server_check_result").scrollTop(height);
  } else {
    console.log(log);
  }
}

//清空日志
function clear_log () {
  $('#ntls_server_check_result').text('');
}

function send_msg () {
  $('#default_notice').hide();
  //连接已断开,重连
  if (!NTLSPro.websocket_status()) {
    NTLSPro.current_connect_failed_count++;
    write_log('尝试连接第' + NTLSPro.current_connect_failed_count + '次，共' + NTLSPro.connect_failed_count + '次，请稍等...');
    NTLSPro.websocket_init();
    //尝试连接n次，连接不上断开
    if (NTLSPro.current_connect_failed_count >= NTLSPro.connect_failed_count) {
      NTLSPro.current_connect_failed_count = 0;
      close_webwocket();
    } else {
      check_sender_timer = setTimeout("send_msg()", 5000);
    }
  } else {
    clearTimeout(check_sender_timer);
    write_log('websocket 发送数据:' + send_message_data);
    wss_obj.send(send_message_data);
  }
}

//关闭浏览时 停止websocket服务检测
$(window).on('unload', function () {
  close_webwocket();
});
