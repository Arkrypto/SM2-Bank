<template>
  <div class="app-container">
    <div class="step-panel">
      <el-row type="flex" justify="center">
        <el-col :md="18" :sm="18" :xs="24">
          <el-steps :active="stepActive" simple finish-status="success">
            <el-step title="填写取款表单" />
            <el-step title="录入信息确认" />
            <el-step title="资金转出结果" />
          </el-steps>
        </el-col>
      </el-row>
    </div>

    <div class="form-panel">
      <el-row type="flex" justify="center">
        <el-col v-show="stepActive===0" :md="12" :sm="18" :xs="24">
          <el-card shadow="hover">
            <div slot="header">
              <el-row type="flex" justify="space-between">
                <el-col :span="6"><span>资金转出</span></el-col>
                <el-col :span="6" style="text-align:right"><span>单位(元)</span></el-col>
              </el-row>
            </div>

            <el-form ref="withdrawalForm" :rules="rules" label-position="left" label-width="80px" :model="withdrawalForm">
              <el-form-item label="银行账号" prop="myAccount">
                <el-select v-model="withdrawalForm.myAccount" placeholder="选择账户" style="width: 100%;" @change="setBalance">
                  <el-option v-for="account in accountList" :key="account.id" :label="account.id" :value="account.id">
                    <span style="float: left">{{ account.id }}</span>
                    <span style="float: right; color: #8492a6;">
                      <el-divider direction="vertical" />
                      {{ account.bankName }}
                    </span>
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="可用余额">
                <el-input v-model="balance" readonly>
                  <template slot="append">单位(元)</template>
                </el-input>
              </el-form-item>
              <el-form-item label="取出金额" prop="amount">
                <el-input v-model="withdrawalForm.amount" oninput="value=value.replace(/[^0-9.]/g,'')" @input="moneyToCapital">
                  <template slot="append">单位(元)</template>
                </el-input>
              </el-form-item>
              <el-form-item label="大写金额">
                <el-input v-model="capitalMoney" readonly />
              </el-form-item>
              <el-form-item style="text-align:right">
                <el-button @click="next">下一步</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>

        <el-col v-show="stepActive===1" :md="12" :sm="18" :xs="24">
          <el-card shadow="never">
            <div slot="header">
              <el-row type="flex" justify="space-between">
                <el-col :span="6"><span>信息确认</span></el-col>
                <el-col :span="6" style="text-align:right"><span>单位(元)</span></el-col>
              </el-row>
            </div>

            <el-form ref="withdrawalCode" :rules="rules" label-position="left" label-width="80px" :model="withdrawalForm">
              <el-form-item label="银行账号">
                <el-input v-model="withdrawalForm.myAccount" readonly />
              </el-form-item>
              <el-form-item label="取出金额">
                <el-input v-model="withdrawalForm.amount" readonly />
              </el-form-item>
              <el-divider />
              <el-form-item label="手机号码">
                <el-input v-model="phone" readonly>
                  <template slot="append">
                    <a @click="fetchVerifyCode">获取验证码</a>
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item label="验证码" prop="verifyCode">
                <el-input v-model.number="withdrawalForm.verifyCode" />
              </el-form-item>
              <el-form-item>
                <el-row type="flex" justify="space-between">
                  <el-col :span="12">
                    <el-button @click="back">上一步</el-button>
                  </el-col>
                  <el-col :span="12" style="text-align: right">
                    <el-button @click="next">提交</el-button>
                  </el-col>
                </el-row>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>

        <el-col v-show="stepActive===2" :md="12" :sm="18" :xs="24">
          <el-card>
            <div slot="header">
              <span>转入结果</span>
            </div>
            <div style="text-align: center">
              <img src="@/assets/result_images/result1.png" alt="操作已提交">
              <p>资金转入已提交，待审核！</p>
              <el-button @click="next">确认</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getAccountList, doWithdrawal } from '@/api/account'
import { getVerifyCode, verifyCode } from '@/api/user'
import { mapGetters } from 'vuex'
// hint：已弃用
export default {
  data() {
    const validateCode = (rule, value, callback) => {
      verifyCode({ 'name': this.name, 'verifyCode': this.withdrawalForm.verifyCode }).then(response => {
        if (response.data) {
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
      accountList: null,
      stepActive: 0,
      phone: '',
      balance: '',
      capitalMoney: '',
      withdrawalForm: {
        name: '',
        myAccount: '',
        amount: '',
        verifyCode: ''
      },
      rules: {
        myAccount: [{ required: true, message: '请选择取款账户', trigger: 'change' }],
        amount: [{ required: true, message: '请输入取出金额', trigger: 'blur' },
          { validator: validateBalance, trigger: 'blur' }],
        verifyCode: [{ required: true, message: '请输入验证码', trigger: 'blur' },
          { type: 'number', message: '验证码类型错误' },
          { validator: validateCode, trigger: 'blur' }]
      }
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
    this.setWithdrawalForm()
  },
  methods: {
    setWithdrawalForm() {
      try {
        this.withdrawalForm.name = this.name
        this.withdrawalForm.myAccount = this.$route.params.id
        this.balance = this.$route.params.balance
        this.phone = this.$route.params.phone.substr(0, 3) + '****' + this.phone.substr(7)
      } catch (e) {
        this.phone = ''
      }
    },
    resetForm() {
      this.withdrawalForm = {
        name: this.name,
        myAccount: '',
        amount: '',
        verifyCode: ''
      }
      this.phone = ''
      this.balance = ''
      this.capitalMoney = ''
    },
    fetchAccountList() {
      getAccountList(this.id).then(response => {
        this.accountList = response.data
      })
    },
    setBalance(val) {
      const account = this.accountList.filter(account => account.id === val)[0]
      this.balance = account.balance.toFixed(2)
      this.phone = account.phone.toString()
      this.phone = this.phone.substr(0, 3) + '****' + this.phone.substr(7)
    },
    moneyToCapital(num) { // 数字转大写
      if (num === '0') {
        this.capitalMoney = '零'
        return null
      }
      if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(num)) { this.capitalMoney = ''; return null }
      let unit = '千百拾亿千百拾万千百拾元角分'
      let str = ''
      num += '00'
      const p = num.indexOf('.')
      if (p >= 0) { num = num.substring(0, p) + num.substr(p + 1, 2) }
      unit = unit.substr(unit.length - num.length)
      for (let i = 0; i < num.length; i++) { str += '零壹贰叁肆伍陆柒捌玖'.charAt(Number(num.charAt(i))) + unit.charAt(i) }
      this.capitalMoney = str.replace(/零[千百拾角]/g, '零')
        .replace(/(零)+/g, '零')
        .replace(/零[万亿元]/g, '$1')
        .replace(/(亿)万|壹(拾)/g, '$1$2')
        .replace(/^元零?|零分/g, '')
        .replace(/元$/g, '元整')
    },
    next() {
      if (this.stepActive === 0) {
        this.$refs['withdrawalForm'].validate((valid) => {
          if (valid) this.stepActive = 1
        })
      } else if (this.stepActive === 1) {
        console.log(this.withdrawalForm)
        this.$refs['withdrawalCode'].validate((valid) => {
          console.log(2, this.withdrawalForm)
          if (valid) {
            doWithdrawal(this.withdrawalForm).then(() => {
              this.resetForm()
            }).finally(() => {
              this.stepActive = 2
            })
          }
        })
      } else {
        console.log('根据操作结果显示不同图片')
        this.fetchAccountList()
        this.$refs['withdrawalForm'].clearValidate()
        this.$refs['withdrawalCode'].clearValidate()
        this.stepActive = 0
      }
    },
    back() {
      if (this.stepActive-- < 1) this.stepActive = 2
    },
    fetchVerifyCode() {
      getVerifyCode(this.id).then(response => {
        const date = new Date()
        const receivedAt = date.getHours() + ':' + date.getMinutes()
        this.$notify.info({
          title: receivedAt,
          message: '验证码为：' + response.data,
          duration: 0
        })
        console.log(response.data)
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.app-container {
  margin: 30px;
  border: 2px solid #eff0f3;
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

</style>
