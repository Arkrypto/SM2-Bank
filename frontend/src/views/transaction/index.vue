<template>
  <div class="app-container">
    <div class="step-panel">
      <el-row type="flex" justify="center">
        <el-col :md="18" :sm="18" :xs="24">
          <el-steps :active="stepActive" simple finish-status="success">
            <el-step title="填写存款表单" />
            <el-step title="录入信息确认" />
            <el-step title="资金转入结果" />
          </el-steps>
        </el-col>
      </el-row>
    </div>
    <div>
      <el-card v-show="stepActive===0" class="box-card form-panel">
        <div slot="header">
          <span>单笔转账</span>
        </div>
        <el-row type="flex" justify="center" :gutter="10">
          <el-col :md="8" :sm="10" :xs="11">
            <el-card class="box-card" shadow="hover">
              <div slot="header" class="card-header">
                <span>常用账户</span>
              </div>
              <el-input
                v-model="queryFriendName"
                placeholder="输入名称查询"
                prefix-icon="el-icon-search"
                @input="findFriendByName(queryFriendName)"
              />
              
              <el-collapse v-model="activeCardName" accordion class="card-list" @change="chooseCard(activeCardName)">
                <el-collapse-item
                  v-for="item in pageList"
                  :key="item.account"
                  :title="item.friendName"
                  :name="item.account"
                >
                  <div>
                    <span style="float: left">{{ item.account }}</span>
                    <span style="float: right; color: #8492a6;">
                      <el-divider direction="vertical" />
                      {{ item.bankName }}
                    </span>
                  </div>
                </el-collapse-item>
              </el-collapse>

            </el-card>
          </el-col>

          <el-col :md="1" :sm="1" :xs="1">
            <i class="el-icon-caret-right move-to-right" />
          </el-col>

          <el-col :md="15" :sm="13" :xs="12">
            <el-card class="box-card" shadow="hover">
              <el-form ref="transactionForm" :rules="rules" label-position="left" label-width="80px" :model="transactionForm">
                <el-form-item>
                  <span class="card-header">转账信息</span>
                </el-form-item>
                <el-form-item label="收款账户" prop="toAccount">
                  <el-input v-model="transactionForm.toAccount" />
                </el-form-item>
                <el-form-item label="账户名称" prop="toUsername">
                  <el-input v-model="transactionForm.toUsername" />
                </el-form-item>
                <el-form-item label="收款银行" prop="toBankName">
                  <el-input v-model="transactionForm.toBankName" />
                </el-form-item>
                <el-divider />
                <el-form-item>
                  <span class="card-header">付款信息</span>
                </el-form-item>
                <el-form-item label="付款账号" prop="myAccount">
                  <el-select v-model="transactionForm.myAccount" placeholder="选择账户" style="width: 100%;" @change="setBalance">
                    <el-option v-for="account in myAccountList" :key="account.id" :label="account.id" :value="account.id">
                      <span style="float: left">{{ account.id }}</span>
                      <span style="float: right; color: #8492a6;">
                        <el-divider direction="vertical" />
                        {{ account.bankName }}
                      </span>
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="付款银行">
                  <el-input v-model="myBankName" readonly />
                </el-form-item>
                <el-form-item label="可用余额">
                  <el-input v-model="balance" readonly>
                    <template slot="append">单位(元)</template>
                  </el-input>
                </el-form-item>
                <el-form-item label="转账金额" prop="amount">
                  <el-input v-model="transactionForm.amount" oninput="value=value.replace(/[^0-9.]/g,'')">
                    <template slot="append">单位(元)</template>
                  </el-input>
                </el-form-item>

                <el-form-item label="支付密码" prop="payPassword">
                  <div>
                    <el-input
                      :key="passwordType"
                      ref="payPassword"
                      v-model="transactionForm.payPassword"
                      :type="passwordType"
                      tabindex="2"
                      style="width: 40%"
                      oninput="value=value.replace(/[^0-9.]/g,'')"
                      show-password
                    />
                    
                    <router-link to="/information/index">忘记密码？</router-link>
                    
                  </div>


                  <!-- <el-input v-model="transactionForm.payPassword" >
                  </el-input> -->
                </el-form-item>

                <el-form-item label="转账说明">
                  <el-input v-model="transactionForm.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" />
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>
        </el-row>
        <div style="text-align: right;margin-top: 20px;">
          <el-button @click="next">下一步</el-button>
        </div>
      </el-card>

      <el-card v-show="stepActive===1" class="box-card form-panel">
        <div slot="header">
          <span>转账确认</span>
        </div>
        <el-row type="flex" justify="center" :gutter="10">
          <el-col :md="8" :sm="10" :xs="11">
            <el-card class="box-card" shadow="never">
              <el-form label-position="left" label-width="80px" :model="transactionForm">
                <el-form-item>
                  <span class="card-header">付款信息</span>
                </el-form-item>
                <el-form-item label="付款账户">
                  <el-input v-model="transactionForm.myAccount" readonly />
                </el-form-item>
                <el-form-item label="付款银行">
                  <el-input v-model="myBankName" readonly />
                </el-form-item>
                <el-form-item label="付款人">
                  <el-input v-model="name" readonly />
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>

          <el-col :md="1" :sm="1" :xs="1">
            <i class="el-icon-d-arrow-right move-to-right" />
          </el-col>

          <el-col :md="15" :sm="13" :xs="12">
            <el-card class="box-card" shadow="never">
              <el-form
                ref="codeForm"
                :rules="rules"
                label-position="left"
                label-width="80px"
                :model="transactionForm"
              >
                <el-form-item>
                  <span class="card-header">收款信息</span>
                </el-form-item>
                <el-form-item label="收款账户">
                  <el-input v-model="transactionForm.toAccount" readonly />
                </el-form-item>
                <el-form-item label="收款银行">
                  <el-input v-model="transactionForm.toBankName" readonly />
                </el-form-item>
                <el-form-item label="收款人">
                  <el-input v-model="transactionForm.toUsername" readonly />
                </el-form-item>
                <el-divider />
                <el-form-item>
                  <span class="card-header">转账信息</span>
                </el-form-item>
                <el-form-item label="转账金额">
                  <el-input v-model="transactionForm.amount" readonly>
                    <template slot="append">单位(元)</template>
                  </el-input>
                </el-form-item>
                <el-form-item label="转账说明">
                  <el-input v-model="transactionForm.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" readonly />
                </el-form-item>
                <el-form-item label="手续费用">
                  <el-input v-model="transactionForm.fee" readonly>
                    <template slot="append">单位(元)</template>
                  </el-input>
                </el-form-item>
                <el-divider />
                <el-form-item>
                  <span class="card-header">验证码信息</span>
                </el-form-item>
                <el-form-item label="手机号码">
                  <el-input v-model="phone" readonly />
                </el-form-item>
                <el-form-item label="验证码" prop="verifyCode">
                  <el-input v-model.number="transactionForm.verifyCode">
                    <template slot="append">
                      <a @click="fetchVerifyCode">获取验证码</a>
                    </template>
                  </el-input>
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>
        </el-row>
        <el-row type="flex" justify="space-between" style="margin-top: 20px">
          <el-col :span="12">
            <el-button @click="back">上一步</el-button>
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-button @click="next">提交</el-button>
          </el-col>
        </el-row>
      </el-card>

      <el-card v-show="stepActive===2" class="box-card form-panel">
        <div slot="header">
          <span>转账结果</span>
        </div>
        <el-row type="flex" justify="center">
          <el-col :md="10" :sm="10" :xs="12" >
            <el-card>
              <div class="text-wrapper">
                  <el-descriptions v-if="resultInfo.result" title="交易成功"  :column ="1" size="medium" >
                      <el-descriptions-item label="账户">{{resultInfo.myAccount}}</el-descriptions-item>
                      <el-descriptions-item label="用户名">{{resultInfo.name}}</el-descriptions-item>
                      <el-descriptions-item label="对方账户">{{resultInfo.toAccount}}</el-descriptions-item>
                      <el-descriptions-item label="对方用户名">{{resultInfo.toUsername}}</el-descriptions-item>
                      <el-descriptions-item label="转账金额">{{resultInfo.amount}}</el-descriptions-item>
                      <el-descriptions-item label="转入银行">{{resultInfo.toBankName}}</el-descriptions-item>
                      <el-descriptions-item label="手续费">{{resultInfo.fee}}</el-descriptions-item>
                  </el-descriptions>

                  <el-descriptions v-else title="交易失败" :column= "1" size="medium" >
                     <el-descriptions-item label="失败原因">{{resultInfo.reason}}</el-descriptions-item>
                  </el-descriptions>

                  <el-descriptions  :column= "1" size="medium"  direction="vertical"  >
                     <el-descriptions-item label="交易时间">{{resultInfo.time}}</el-descriptions-item>
                     <el-descriptions-item label="服务器交易签名" style="white-space: pre-wrap" >{{resultInfo.signature}}</el-descriptions-item>
                     <el-descriptions-item label="交易时间戳" style="white-space: pre-wrap" >{{resultInfo.timeStamp}}</el-descriptions-item>
                  </el-descriptions>

                <el-button style="margin-top: 20px" @click="next">关闭</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-card>

    </div>
  </div>
</template>

<script>
import { getAccountList, getFriendAccountList, doTransaction } from '@/api/account'
import { mapGetters } from 'vuex'
import { getVerifyCode, verifyCode } from '@/api/user'
// import { signData } from '@/utils/mtools'
import { ntlsUtil } from '@/utils/ntls-plugin'

export default {
  data() {
    const validateCode = (rule, value, callback) => {
      verifyCode({ 'name': this.name, 'verifyCode': this.transactionForm.verifyCode }).then(response => {
        if (response.data) { // todo: 后端二次验证
          callback()
        } else {
          callback(new Error('验证码错误'))
        }
      })
    }
    const validateBalance = (rule, value, callback) => {
      if (Number(value) > this.balance) {
        callback(new Error('余额不足'))
      }
      callback()
    }
    return {
      friendAccountList: [],
      pageList: [],
      myAccountList: [],
      phone: '',
      balance: '',
      myBankName: '',
      queryFriendName: '',
      stepActive: 0,
      activeCardName: '',
      transactionForm: {
        id: '',
        name: '',
        toAccount: '',
        toUsername: '',
        toBankName: '',
        myAccount: '',
        amount: '',
        description: '',
        fee: 0.00,
        verifyCode: '',
        signature: null,
        iniData: null,
        payPassword:""
      },
      resultInfo:{
        result: false,
        name: '',
        toAccount: '',
        toUsername: '',
        toBankName: '',
        myAccount: '',
        amount: '',
        fee: 0.00,
        signature: '',
        time: '',
        timeStamp: '',
        reason: '',
      },
      rules: {
        toAccount: [{ required: true, message: '请输入收款账户', trigger: 'blur' }],
        toUsername: [{ required: true, message: '请输入收款人', trigger: 'blur' }],
        toBankName: [{ required: true, message: '请输入收款银行', trigger: 'blur' }],
        myAccount: [{ required: true, message: '请选择付款账户', trigger: 'change' }],
        amount: [{ required: true, message: '请输入转账金额', trigger: 'blur' },
          { validator: validateBalance, trigger: 'blur' }],
        verifyCode: [{ required: true, message: '请输入验证码', trigger: 'blur' },
          { type: 'number', message: '验证码类型错误' },
          { validator: validateCode, trigger: 'blur' }]
      },
      passwordType: 'password',
      keyindex: '',
      container: '',
      alg_type: "SM2",
      key_type: "file",
      check_key_type: false,
    }
  },
  computed: {
    ...mapGetters([
      'id',
      'name'
    ])
  },
  created() {
    this.fetchAccountList()
    this.fetchFriendAccountList()
    this.envCheck()
  },
  methods: {
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
	    }
    },

    setTransactionForm() {
      this.transactionForm.name = this.name
      this.transactionForm.id = this.id
      if (this.$route.params.id) {
        this.transactionForm.myAccount = this.$route.params.id
        this.myBankName = this.$route.params.bankName
        this.balance = this.$route.params.balance.toFixed(2)
        this.phone = this.$route.params.phone.toString()
        this.phone = this.phone.substr(0, 3) + '****' + this.phone.substr(7)
      } else {
        this.transactionForm.myAccount = this.myAccountList[0].id
        this.setBalance(this.myAccountList[0].id)
      }
    },
    resetForm() {
      this.transactionForm = {
        id: this.id,
        name: this.name,
        toAccount: '',
        toUsername: '',
        toBankName: '',
        myAccount: '',
        amount: '',
        description: '',
        fee: 0.00,
        verifyCode: ''
      }
      this.phone = ''
      this.balance = ''
      this.myBankName = ''
    },
    fetchAccountList() {
      getAccountList(this.id).then(response => {
        this.myAccountList = response.data
        this.setTransactionForm()
      })
    },
    fetchFriendAccountList() {
      getFriendAccountList(this.id).then(response => {
        this.friendAccountList = response.data
        this.pageList = this.friendAccountList
      })
    },
    findFriendByName(queryFriendName) {
      this.pageList = this.friendAccountList.filter(account => {
        return (account.friendName.toLowerCase().indexOf(queryFriendName.toLowerCase()) === 0)
      })
    },
    setBalance(val) {
      const account = this.myAccountList.filter(account => account.id === val)[0]
      this.myBankName = account.bankName
      this.balance = account.balance.toFixed(2)
      this.phone = account.phone.toString()
      this.phone = this.phone.substr(0, 3) + '****' + this.phone.substr(7)
    },
    chooseCard(val) {
      this.$refs['transactionForm'].clearValidate()
      if (val) {
        const account = this.friendAccountList.filter(item => item.account === val)[0]
        this.transactionForm.toAccount = this.activeCardName
        this.transactionForm.toUsername = account.friendName
        this.transactionForm.toBankName = account.bankName
      } else {
        this.$refs['transactionForm'].resetFields()
      }
    },
    fetchVerifyCode() {
      getVerifyCode(this.id).then(response => {
        const date = new Date() // Fixme: 模拟接收短信
        const receivedAt = date.getHours() + ':' + date.getMinutes()
        this.$notify.info({
          title: receivedAt,
          message: '验证码为：' + response.data,
          duration: 10000,
        })
        console.log(response.data) // fixme: delete this
      })
    },
    forgetPw(){
        this.$router.push({path:'/information/index'})
    },
    encodeBase64(str){
      return Buffer.from(str, 'utf8').toString('base64');
    },
    next() {
      if (this.stepActive === 0) {
        this.$refs['transactionForm'].validate((valid) => {
          if (valid) this.stepActive = 1
        })
      } else if (this.stepActive === 1) {
        this.$refs['codeForm'].validate((valid) => {
          if (valid) {
            this.transactionForm.iniData = JSON.stringify(this.transactionForm)
            let iniData = this.encodeBase64(this.transactionForm.iniData);
            let ukey_val = this.keyindex;
            let identity_val = this.container;
            // 如果websocket没有获取到，就从session里取
            if(ukey_val == ""){
              ukey_val = localStorage.getItem("keyindex");
            }
            if(identity_val == ""){
              identity_val = localStorage.getItem("container");
            }
            let hashtype = "";
            let format = "asn.1";
            let pass = localStorage.getItem("pin");
            console.log(iniData);
            ntlsUtil.func.data_sm2_signature(ukey_val, identity_val, pass, iniData, hashtype, format, this.doTrans);
          }
        })
      } else {
        console.log('根据操作结果显示不同图片')
        this.fetchAccountList()
        this.$refs['transactionForm'].clearValidate()
        this.$refs['codeForm'].clearValidate()
        this.stepActive = 0
      }
    },
    doTrans(message){
      if(!message || !message.data){
        alert("签名失败, 请检查UKey是否插入或PIN码是否正确, 或PIN码是否被锁定");
        return false;
      }
      this.transactionForm.signature = message.data;
      console.log(this.transactionForm.signature)
      doTransaction(this.transactionForm).then(response => {
        var data = response.data
        this.resultInfo.name = data.name
        this.resultInfo.myAccount = data.account
        this.resultInfo.toAccount = data.toAccount
        this.resultInfo.toUsername = data.toName
        this.resultInfo.result = true
        this.resultInfo.amount = data.amount
        this.resultInfo.toBankName = data.toBank
        this.resultInfo.fee = data.fee
        this.resultInfo.time = data.time
        this.resultInfo.signature = data.serverSignature
        this.resultInfo.timeStamp = data.timeStamp
        this.resetForm()
      }).catch( error => {
        var data = error.data
        this.resultInfo.reason = data.reason
        this.resultInfo.time = data.time
        this.resultInfo.signature = data.serverSignature
        this.resultInfo.timeStamp = data.timeStamp
      }).finally(() => {
        this.stepActive = 2
      })
    },
    back() {
      if (this.stepActive-- > 1) this.stepActive = 2
    }
    // next() {
    //   if (this.stepActive === 0) {
    //     this.$refs['transactionForm'].validate((valid) => {
    //       if (valid) this.stepActive = 1
    //     })
    //   } else if (this.stepActive === 1) {
    //     this.$refs['codeForm'].validate((valid) => {
    //       if (valid) {
    //         var format = JSON.stringify(this.transactionForm)
    //         this.transactionForm.iniData = format
    //         console.log(format)
    //         this.transactionForm.signature = signData(format)
    //         console.log(this.transactionForm.signature)
    //         doTransaction(this.transactionForm).then(response => {
    //             var  data = response.data
    //             this.resultInfo.name = data.name
    //             this.resultInfo.myAccount = data.account
    //             this.resultInfo.toAccount = data.toAccount
    //             this.resultInfo.toUsername = data.toName
    //             this.resultInfo.result = true
    //             this.resultInfo.amount = data.amount
    //             this.resultInfo.toBankName = data.toBank
    //             this.resultInfo.fee = data.fee
    //             this.resultInfo.time = data.time
    //             this.resultInfo.signature = data.serverSignature
    //             this.resultInfo.timeStamp = data.timeStamp
    //             this.resetForm()
    //         }).catch( error => {
    //             var data = error.data
    //             this.resultInfo.reason = data.reason
    //             this.resultInfo.time = data.time
    //             this.resultInfo.signature = data.serverSignature
    //             this.resultInfo.timeStamp = data.timeStamp
    //         }).finally(() => {
    //           this.stepActive = 2
    //         })
    //       }
    //     })
    //   } else {
    //     console.log('根据操作结果显示不同图片')
    //     this.fetchAccountList()
    //     this.$refs['transactionForm'].clearValidate()
    //     this.$refs['codeForm'].clearValidate()
    //     this.stepActive = 0
    //   }
    // },
  }
}
</script>
<style lang="scss" scoped>
.app-container {
  margin: 30px;
  //border: 1px solid red;
}
.step-panel{
  margin: 10px 0;
  //border: 1px solid red;
}
.form-panel{
  padding: 5px;
  //border: 1px solid red;
  font-size: 24px;
  font-weight:bold;
}
.move-to-right{
  margin-top: 100px;
  font-size: xx-large;
}
.card-header{
  font-size: 20px;
}
.card-list{
  margin: 10px 0;//上下 左右
}
.text-wrapper{
  word-break: break-all;
  word-wrap: break-word;
}
</style>

