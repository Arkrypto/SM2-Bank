<template>
  <div class="login-container">
    <el-collapse-transition>
      <el-form v-if="switchLogin" ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
        <div class="title-container">
          <h3 class="title">网上银行-登录</h3>
        </div>
        <el-form-item prop="username">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="username"
            v-model="loginForm.username"
            placeholder="用户名"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="密码"
            name="password"
            tabindex="2"
            auto-complete="on"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>

        <el-form-item prop="key">
          <span class="svg-container">
            <svg-icon icon-class="key" />
          </span>
          <el-input
            :key="passwordType"
            ref="key"
            v-model="loginForm.key"
            :type="passwordType"
            placeholder="key"
            name="key"
            tabindex="2"
            auto-complete="on"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>



        <el-form-item prop="code">
          <el-row>
            <el-col :span="12">
                <el-input size="normal" type="text" v-model="loginForm.code" auto-complete="off"
                  placeholder="点击图片更换验证码" @keydown.enter.native="submitLogin" style="width: 250px"></el-input>
            </el-col>
            <el-col :span="12" :push="5">
                   <img :src="vcUrl" @click="updateVerifyCode" alt="" style="cursor: pointer" >
            </el-col>
          </el-row>
        </el-form-item>


        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>

        <div class="tips" style="text-align: right;">
          <span style="margin-right:20px;">忘记密码？</span>
          <el-button type="info" size="mini" @click="switchLogin=!switchLogin">注册<i class="el-icon-arrow-down el-icon--right" /></el-button>
          <el-button type="info" size="mini" @click="getCert()">查看证书</el-button>
          <el-button type="info" size="mini" @click="testSign()">测试签名</el-button>
        </div>
      </el-form>
    </el-collapse-transition>

    <el-collapse-transition>
      <el-form v-if="!switchLogin" ref="registerForm" :model="registerForm" :rules="registerRules" class="login-form" auto-complete="on" label-position="left">
        <div class="title-container">
          <h3 class="title">网上银行-注册</h3>
        </div>
        <el-form-item prop="username">
          <span class="svg-container">
            <svg-icon icon-class="user" />
          </span>
          <el-input
            ref="username"
            v-model="registerForm.username"
            placeholder="用户名"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            :key="passwordType"
            ref="password"
            v-model="registerForm.password"
            :type="passwordType"
            placeholder="密码"
            name="password"
            tabindex="2"
            auto-complete="on"
            @keyup.enter.native="handleLogin"
             show-password
          />
        </el-form-item>

        <el-form-item prop="payPassword">
          <span class="svg-container">
            <svg-icon icon-class="payPassword" />
          </span>
          <el-input
            :key="passwordType"
            ref="payPassword"
            v-model="registerForm.payPassword"
            :type="passwordType"
            placeholder="支付密码"
            name="payPassword"
            tabindex="2"
            auto-complete="on"
            @keyup.enter.native="handleLogin"
             show-password
          />
        </el-form-item>


        <el-form-item prop="idCard">
          <span class="svg-container">
            <svg-icon icon-class="idCard" />
          </span>
          <el-input
            ref="idCard"
            v-model="registerForm.idCard"
            placeholder="身份证号"
            name="idCard"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>



        <el-form-item prop="age">
          <span class="svg-container">
            <svg-icon icon-class="age" />
          </span>
          <el-input
            ref="age"
            v-model.number="registerForm.age"
            placeholder="年龄"
            name="age"
            type="text"
            tabindex="3"
            auto-complete="on"
          />
        </el-form-item>
        <el-form-item prop="gender">
          <span class="svg-container">
            <svg-icon icon-class="sex" />
          </span>
          <el-input placeholder="性别" style="width: 25%" readonly />
          <el-radio v-model="registerForm.gender" label="1" style="width: 30%" tabindex="4">男</el-radio>
          <el-radio v-model="registerForm.gender" label="0" style="width: 30%">女</el-radio>
        </el-form-item>
        <el-form-item prop="email">
          <span class="svg-container">
            <svg-icon icon-class="email" />
          </span>
          <el-input
            ref="email"
            v-model="registerForm.email"
            placeholder="邮箱"
            name="email"
            type="text"
            tabindex="5"
            auto-complete="on"
          />
        </el-form-item>

        <el-button style="width: 30%;margin-right: 25%" icon="el-icon-arrow-up" plain @click="switchLogin=!switchLogin">返回登录</el-button>
        <el-button :loading="loading" type="warning" style="width:40%;margin-bottom:30px;" @click.native.prevent="handleRegister">注册</el-button>
      </el-form>
    </el-collapse-transition>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'
import { register } from '@/api/user'
import mToken from '@/utils/mToken'
import { constantRoutes } from '@/router'
// import {signData,exportCert,getContainer, verifySignature, verifyKey} from "@/utils/mtools"
import { ntlsUtil } from '@/utils/ntls-plugin'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('请正确输入用户名！'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/
      if (!re.test(value)) {
        callback(new Error('密码至少包含大小写字母和数字，且不少于8位'))
      } else {
        callback()
      }
    }
    const validateiCard = (rule, value, callback) => {
      const re = /(^\d{18}$)|(^\d{17}(\d|X|x)$)/
      if (!re.test(value)) {
        callback(new Error('身份证号必须是18位数字,只有最后一位可以是X'))
      } else {
        callback()
      }
    }
    const validateAge = (rule, value, callback) => {
      if (value < 1 || value > 100) {
        callback(new Error('年龄必须在1-100之间!'))
      } else {
        callback()
      }
    }
    const validatePpw = (rule, value, callback) => {
      const re = /(^\d{6}$)/
      if (!re.test(value)) {
        callback(new Error('支付密码必须是6位数字'))
      } else {
        callback()
      }
    }
    return {
      vcUrl: '/bank/vercode?time=' + new Date(),
      switchLogin: true,
      loginForm: {
        username: 'xzt1',
        password: 'Xzt011026',
        key: '123456',
        code:'',
        signature: null,
        iniData: null
      },
      registerForm: {
        username: '',
        password: '',
        age: '',
        gender: '1',
        email: '',
        certificate: null,
        idCard:'',
        payPassword:''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },                                                                         
      registerRules: {
        username: [{ required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' }],

        password: [{ required: true, validator: validatePassword, trigger: 'blur' }],

        idCard:[{required:true,validator:validateiCard,trigger:'blur'}],

        payPassword:[{required:true,message:'请输入支付密码',trigger:'blur'},
        {required:true,validator: validatePpw,trigger:'blur'}],

        age: [{ required: true, message: '请输入年龄', trigger: 'blur' },
          { type: 'number', message: '年龄必须为数字值', trigger: 'blur' },
          { required: true, validator: validateAge, trigger: 'blur' }],

        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
        email: [{ required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      alg_type: "SM2",
      key_type: "file",
      check_key_type: false,
      keyindex: "",
      container: "",
      ret: false,
      cert: ""
    }
  },

  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created(){
    this.envCheck();
  },                                      
  methods: {
    encodeBase64(str){
      return Buffer.from(str, 'utf8').toString('base64');
    },
    envCheck(){
      if(!ntlsUtil.wsObj){
        ntlsUtil.websocketInit(this.check_plugin_exist, null, this.check_plugin_exist);
      } else {
        this.check_plugin_exist();
      }
    },
    //检查插件
    check_plugin_exist(){
	    if(ntlsUtil.pluginExist==false){
		    //连接不成功,插件未安装或者服务未启动
		    console.log("hahaha")
        alert("连接不成功,插件未安装或者服务未启动")
		    return false;
	    }
	    //step4 检测是否插入ukey, 所有接收信息的回调函数名称都为当前发送消息的action名称
	    ntlsUtil.func.enumerate_ukey_user(this.enumerate_ukey_user);
      // console.log("hahaha")
    },
    //检测ukey是否插入
    enumerate_ukey_user(message){
	    if(message == null){
        alert("WebSocket检测请求失败")
		    return false;
	    }
	    var ukey_exist = false;
	    for(var i = 0; i < message.data.usbkey.length; i++){
		    var key_data = message.data.usbkey[i];
		    if(typeof key_data.keytype != 'undefined'){
			    if(this.check_key_type == true &&  key_data.keytype != this.key_type ){ }
          else{
				    //keytype=file 在私钥标识中查找，从每个identity.0.type 查找sm2/sm9/...
				    var this_ukey_exist = false;
				    var length = key_data.identity.length;
				    for (var m = 0; m < length; m++){
				      //只匹配需要的alg
				      if(key_data.identity[m].type.toUpperCase().indexOf(this.alg_type) != -1){
				        this_ukey_exist = true;//局部
				      }
				    }
            if(this_ukey_exist){
					    ukey_exist = true;//全局
				    }
          }
        } else {
			    //key_type=usbkey  在产商中查找
			    if(key_data.manufacturer.toUpperCase().indexOf(alg_type)!==-1 || key_data.alg.toUpperCase().indexOf(this.alg_type)!==-1 ){
			      ukey_exist = true;
			    }
		    }
      }
	    if(ukey_exist == false){
		    alert('未检测到'+this.alg_type+'私钥标识'); //按扭功能 ，检测sm2/sm9等 UKEY是否插件
		    return false;
	    }else{
		    console.log('检测到'+this.alg_type+'私钥标识',true,true);
		    console.log(JSON.stringify(message));
        this.keyindex = message.data.usbkey[0].keyindex;
        this.container = message.data.usbkey[0].identity[0].container;
        console.log("keyindex: " + this.keyindex + "\ncontainer: " + this.container);
        this.ret = true;
	    }
    },
    // 登录
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        valid = valid && this.ret
        if (valid) {
          this.loading = true
          this.loginForm.iniData = JSON.stringify(this.loginForm);
          console.log(this.loginForm.iniData);
          let inData = this.encodeBase64(this.loginForm.iniData);
          let ukey_val = this.keyindex;
          let identity_val = this.container;
          let pass = this.loginForm.key;
          let hashtype = "";
          let format = "asn.1";
          ntlsUtil.func.data_sm2_signature(ukey_val, identity_val, pass, inData, hashtype, format, this.doLogin);
        } else {
          console.log('error submit!!');
          alert("error submit!")
          return false
        }
      })
    },
    doLogin(message){
      if(!message || !message.data){
        alert("签名失败, 请检查UKey是否插入或PIN码是否正确, 或PIN码是否被锁定");
        this.loading = false;
        return false;
      }
      // console.log("签名获取成功: " + message.data);
      console.log("签名所用证书为: " + this.cert  + "\n前端签名原文为" + this.encodeBase64(this.loginForm.iniData) + "\n前端签名所得密文为: " + message.data)
      this.loginForm.signature = message.data;
      
      this.$store.dispatch('user/login', this.loginForm).then(() => {
        // this.redirect = 
        localStorage.setItem("pin", this.loginForm.key);
        localStorage.setItem("keyindex", this.keyindex);
        localStorage.setItem("container", this.container);
        this.$router.push({ path: this.redirect || '/' })
        
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getCert(){
      let ukey_val = this.keyindex;
      let identity_val = this.container;
      ntlsUtil.func.get_cert_content(ukey_val, identity_val, 1, this.print);
    },
    print(message){
      if(!message || !message.data){
        alert("证书为空");
        return;
      }
      // alert(message.data);
      console.log(message.data)
      let cert = message.data.replace(/[\r\n]/g,"");
      cert = cert.substr(27, cert.length)
      cert = cert.substr(0, cert.indexOf("-"))

      this.cert = cert;
      console.log(this.cert)
      alert(this.cert)
    },
    testSign(){
      this.loginForm.iniData = JSON.stringify(this.loginForm);
      let inData = this.encodeBase64(this.loginForm.iniData);
      let ukey_val = this.keyindex;
      let identity_val = this.container;
      let pass = this.loginForm.key;
      let hashtype = "";
      let format = "asn.1";
      ntlsUtil.func.data_sm2_signature(ukey_val, identity_val, pass, inData, hashtype, format, this.print1);
    },
    print1(message){
      alert("签名原文: " + this.loginForm.iniData + "\n\n原文Base64编码: " + this.encodeBase64(this.loginForm.iniData) + "\n\n签名密文: " + message.data + "\n\n所用证书: " + this.cert);
      console.log("签名原文: " + this.loginForm.iniData + "\n\n原文Base64编码: " + this.encodeBase64(this.loginForm.iniData) + "\n\n签名密文: " + message.data + "\n\n所用证书: " + this.cert);
    
    },

    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          let ukey_val = this.keyindex;
          let identity_val = this.container;
          ntlsUtil.func.get_cert_content(ukey_val, identity_val, 1, this.doRegister);
        }
      })
    },
    doRegister(message){
      if(!message || !message.data){
        alert("获取证书内容失败");
        return false;
      }
      console.log(message.data)
      // 截取cert
      let cert = message.data.replace(/[\r\n]/g,"");
      cert = cert.substr(27, cert.length)
      cert = cert.substr(0, cert.indexOf("-"))
      console.log(cert)

      this.registerForm.certificate = cert;
      register(this.registerForm).then(() => {
        this.$message({
          message: '注册成功，即将返回登录页面！',
          type: 'success',
          duration: 2000
        })
        this.loginForm.username = this.registerForm.username
        this.loginForm.password = this.registerForm.password
        setTimeout(() => {
          this.switchLogin = true
        }, 1500)
        this.$refs['registerForm'].resetFields()
      }).finally(() => {
        this.loading = false
      })
    },


    
    updateVerifyCode() {
        this.vcUrl = '/bank/vercode?time='+new Date();
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })  
    },
    // 原来的登录和注册接口
    // handleLogin() {
    //   this.$refs.loginForm.validate(valid => {
    //     var ret = verifyKey(this.loginForm.key)
    //     valid = valid && (ret === 0)
    //     if (valid) {
    //       this.loading = true
       
    //       this.loginForm.iniData = JSON.stringify(this.loginForm)
    //       this.loginForm.signature = signData(this.loginForm.iniData)
      
    //       this.$store.dispatch('user/login', this.loginForm).then(() => {
    //         this.$router.push({ path: this.redirect || '/' })
    //         this.loading = false
    //       }).catch(() => {
    //         this.loading = false
    //       })
    //     } else {
    //       console.log('error submit!!')
    //       return false
    //     }
    //   })
    // },
    // handleRegister() {
    //   this.$refs.registerForm.validate(valid => {
    //     if (valid) {
    //       this.loading = true
    //       var container = getContainer()
    //       this.registerForm.certificate=exportCert(container,1)

    //       register(this.registerForm).then(() => {
    //         this.$message({
    //           message: '注册成功，即将返回登录页面！',
    //           type: 'success',
    //           duration: 2000
    //         })
    //         this.loginForm.username = this.registerForm.username
    //         this.loginForm.password = this.registerForm.password
    //         setTimeout(() => {
    //           this.switchLogin = true
    //         }, 1500)
    //         this.$refs['registerForm'].resetFields()
    //       }).finally(() => {
    //         this.loading = false
    //       })
    //     }
    //   })
    // }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0;
      -webkit-appearance: none;
      border-radius: 0;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }
  //:not(#gender)
  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}

</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0 auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
