function showPicTips(){
	 $(".sm9_tips_pic").show("slow"); 
}
function hidePicTips() {
	 $(".sm9_tips_pic").hide("slow"); 
}
var poll_id;
function showLoginMode(p) {
	$(".key_login").css("display","none");
	$(".pc_login").css("display","none");
	$(".qr_login").css("display","none");
	$(".switch a").removeClass("switch_mouse_on");
	$("#logintype").val(p);
	
	
	//$('#a_sm9').attr("disabled",false); 
    //$('#a_sm9').css("pointer-events","auto");
	close_webwocket(); //切换时关闭连接
	
	if(p==1) {//用户名口令登录
		$(".pc_login").css("display","");
		$("#a_pc").addClass("switch_mouse_on");
		clearTimeout(poll_id);
	}else if(p==2) {//UKEY登录
		$(".key_login").css("display","");
		$("#a_sm9").addClass("switch_mouse_on");
		 clearTimeout(poll_id);
		 
		//UKEY登录
		/*var KeyLoginObject = document.getElementById('SM2_UKEY_LOGIN');
		if (null != KeyLoginObject){
		}else{
			CreateKeyLoginWindows('SecmailSafeLoginDiv');//创建key登录界面
		}*/
		 
		//$('#a_sm9').attr("disabled",true);   //禁用ukey登录 a标签点击
	    //$('#a_sm9').css("pointer-events","none");
	    $('#DevicelistDivId').hide();
	    $('#default_notice').show();
	    $('#InsertKeyImageDiv').hide();
		ukey_init(); 
		 
	}else if(p==3) {//qr
		$(".qr_login").css("display","");
		$("#a_qr").addClass("switch_mouse_on");
		poll_id=setInterval("qrcode_sign_status()",5000);
	}
}

//校验服务器时间和本地时间
function validata_server_time(){
	var d = new Date();
	var micro_time = d.getTime();
	var server_time = parseInt(document.getElementById('now').value)+3600;
	if(micro_time/1000 > server_time){
		return false;
	}else{
		return true;
	}
}

//ajax 轮询签名
function qrcode_sign_status(){
   var r = $('#chapchallenge').val();
   $.ajax({
       url: "/users/signStatus",
       type: 'post',
       data: {'rand':r},
       dataType: 'json',
       success: function(data){
    	   $("#msg").text(data.msg);
    	   if(data.code==1){
    		   clearTimeout(poll_id);
    		   $("#msg").text("正在登录中...");
    		   setTimeout("wait()",2000);
    		   window.location.href="/users/login?token="+r+"&type=qr";
    	   }
        } 
    });  
}

function wait(){
	//empty
}

//UKEYajax验证签名数据成功后提交form表单
function showResponse(originalRequest){
	var resText = originalRequest;
	var emailValidate = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$|^([0][1]|[1])[2-9][0-9]{9}$/;
	if(!emailValidate.test($.trim(resText))) {
		alert(resText);
	}else{
		//必须提交用户名、口令、否则cakephp 的 auth插件不能正确运行
		document.getElementById('email').value= resText;
		document.forms['loginForm'].submit();
	}
}

//更换验证码
function change_img() {
	var ob = document.getElementById('code');
	ob.src ='/users/captcha?id=' + Math.random();
}

//登录时验证码用户名、密码
function validata_logins(){
	var emailVal=document.getElementById('email').value;
	var pwdVal=document.getElementById('password').value;
	if($('#logintype').val() == 1){
		var errObj=$('#message_user');
	}else{
		var errObj=$('#message_key');
	}
	if(!emailVal){
		$(errObj).html("<b class=\"ico icoError\"></b><span>用户名为空</span>	");
		return false;
	}
	if(!pwdVal){
		$(errObj).html("<b class=\"ico icoError\"></b><span>密码为空</span>	");
		return false;
	}
	if($('#auth_type').val() == 1 && $('#logintype').val() == 1){
		var codeVal=document.getElementById('verifyCode').value;
		if(!codeVal){
			$(errObj).html("<b class=\"ico icoError\"></b><span>验证码为空</span>	");
			return false;
		}
	}
	if(!validata_server_time()){
		$(errObj).html("<b class=\"ico icoError\"></b><span>本地时间晚于服务器时间,请将本地时间修改成服务器时间</span>	");
		return false;
	}
	//密码加密
    var secret_iv = $('#login_secret').val();
    if(secret_iv != '' && secret_iv != null && secret_iv.length == 32){
    	var key = secret_iv.substring(16);
		var iv = secret_iv.substring(0,16);
		if(key != '' && key != null && iv != '' && iv != null && key.length == 16 && iv.length == 16){
			//密码加密
			key = CryptoJS.enc.Utf8.parse(key);
			iv = CryptoJS.enc.Utf8.parse(iv);
			var cleartext = $('#password').val();
			var encrypted = CryptoJS.AES.encrypt(cleartext, key, {iv: iv,mode: CryptoJS.mode.CBC,padding: CryptoJS.pad.Pkcs7});
			// 转换为字符串
			var ciphertext = encrypted.toString();
			$('#password').val(ciphertext);
			return true;
		}else{
			$(errObj).html("<b class=\"ico icoError\"></b><span>加密密钥无效</span>	");
			return false;
		}
    }else{
    	$(errObj).html("<b class=\"ico icoError\"></b><span>加密密钥长度无效</span>	");
    	return false;
    }
}