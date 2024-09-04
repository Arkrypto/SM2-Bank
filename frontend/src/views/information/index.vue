<template>
  <div class="app-container">
    <el-row type="flex" justify="center" class="form-panel">
      <el-col :span="15">
        <el-tabs tab-position="left">
          <el-tab-pane label="修改信息">
            <el-card class="card-panel">
              <div slot="header" style="text-align: center">
                <span>修改个人信息</span>
              </div>
              <el-form ref="infoForm" label-position="right" label-width="80px" :model="infoForm" :rules="infoFormRules">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="infoForm.username" readonly />
                </el-form-item>
                <el-form-item label="年龄" prop="age">
                  <el-input v-model.number="infoForm.age" />
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                  <el-radio v-model="infoForm.gender" label="1">男</el-radio>
                  <el-radio v-model="infoForm.gender" label="0">女</el-radio>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="infoForm.email" />
                </el-form-item>
                <el-form-item style="text-align:right">
                  <el-button @click="onSubmitInfo">提交</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>
          <el-tab-pane label="修改密码">
            <el-card class="card-panel">
              <div slot="header" style="text-align: center">
                <span>修改密码</span>
              </div>
              <el-form ref="pwdForm" status-icon label-position="right" label-width="80px" :model="pwdForm" :rules="pwdRules">
                <el-form-item label="旧密码" prop="oldPwd">
                  <el-input v-model="pwdForm.oldPwd" type="password" />
                </el-form-item>
                <el-form-item label="新密码" prop="newPwd">
                  <el-input v-model="pwdForm.newPwd" type="password" />
                </el-form-item>
                <el-form-item label="请确认" prop="newPwdRepeat">
                  <el-input v-model="pwdForm.newPwdRepeat" type="password" />
                </el-form-item>
                <el-form-item style="text-align:right">
                  <el-button @click="onSubmitPwd">提交</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-tab-pane>



          <el-tab-pane label="忘记支付密码">
            <el-card class="card-panel">
              <div slot="header" style="text-align: center">
                <span>重置支付密码</span>
              </div>
              <el-form ref="ppwdForm" status-icon label-position="right" label-width="80px" :model="ppwdForm" :rules="ppwdRules">

                <el-form-item label="身份证号" prop="idCard">
                  <el-input v-model="ppwdForm.idCard" type="text" />
                </el-form-item>

                <el-form-item label="新密码" prop="payPassword">
                  <el-input v-model="ppwdForm.payPassword" type="password" show-password />
                </el-form-item>

                <el-form-item label="请确认" prop="newPayPassword">
                  <el-input v-model="ppwdForm.newPayPassword" type="password" show-password />
                </el-form-item>

                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="ppwdForm.phone" type="text" />
                </el-form-item>

                <el-form-item label="验证码" prop="verifyCode">
                  <el-input v-model.number="ppwdForm.verifyCode">
                    <template slot="append">
                      <a @click="fetchVerifyCode">获取验证码</a>
                    </template>
                  </el-input>
                </el-form-item>

                <el-form-item style="text-align:right">
                  <el-button @click="onSubmitPpwd">提交</el-button>
                </el-form-item>
              </el-form>

            </el-card>
          </el-tab-pane>



        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { updateInfo, verifyPwd, updatePwd,reSetPpwd,getVerifyCode, verifyCode } from '@/api/user'
import { mapGetters } from 'vuex'

export default {
  data() {
    const validateAge = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('年龄不能为空'))
      } else if (!Number.isFinite(value)) {
        callback(new Error('年龄必须为数字值!'))
      } else if (value < 1 || value > 100) {
        callback(new Error('年龄必须在1-100之间!'))
      } else {
        callback()
      }
    }
    const validateOldPwd = (rule, value, callback) => {
      verifyPwd({ 'username': this.name, 'password': this.pwdForm.oldPwd }).then(response => {
        if (response.data) {
          callback()
        }
        callback(new Error('密码错误!'))
      }).catch(() => {
        callback(new Error('验证错误!'))
      })
    }
    const validateiCard = (rule, value, callback) => {
      const re = /(^\d{18}$)|(^\d{17}(\d|X|x)$)/
      if (!re.test(value)) {
        callback(new Error('身份证号必须是18位数字,只有最后一位可以是X'))
      } else {
        callback()
      }
    }
    const validateNewPwd = (rule, value, callback) => {
      const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/

      if (!re.test(value)) {
        callback(new Error('密码至少包含大小写字母和数字，且不少于8位'))
      } else {
        if (this.pwdForm.newPwdRepeat !== '') {
          this.$refs.pwdForm.validateField('newPwdRepeat')
        }
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
    const validatePwdRepeat = (rule, value, callback) => {
      if (value !== this.pwdForm.newPwd) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    const validatePPwdRepeat = (rule, value, callback) => {
      if (value !== this.ppwdForm.payPassword) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    const validatePhone = (rule, value, callback) => {
      const re =/^1[3|4|5|7|8]\d{9}$/
      if (!re.test(value)) {
        callback(new Error('请正确输入手机号码'))
      } else {
        callback()
      }
    }
      const validateCode = (rule, value, callback) => {
      verifyCode({ 'name': this.name, 'verifyCode': this.ppwdForm.verifyCode }).then(response => {
        if (response.data) { // todo: 后端二次验证
          callback()
        } else {
          callback(new Error('验证码错误'))
        }
      })
    }
    return {
      infoForm: {
        username: '',
        age: '',
        gender: '',
        email: ''
      },
      pwdForm: {
        username: '',
        oldPwd: '',
        newPwd: '',
        newPwdRepeat: ''
      },
      ppwdForm:{
        idCard:'123456789987654321',
        payPassword:'123456',
        newPayPassword:'123456',
        verifyCode:'',
        phone:'18700107709'
      },
      infoFormRules: {
        username: [{ required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' }],
        age: [{ required: true, validator: validateAge, trigger: 'blur' }],
        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
        email: [{ required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }]
      },
      pwdRules: {
        oldPwd: [{ required: true, message: '请输入旧密码', trigger: 'blur' },
          { validator: validateOldPwd, trigger: 'blur' }],
        newPwd: [{ required: true, message: '新密码不能为空', trigger: 'blur' },
          { validator: validateNewPwd, trigger: 'blur' }],
        newPwdRepeat: [{ required: true, message: '请重复新密码', trigger: 'blur' },
          { validator: validatePwdRepeat, trigger: 'blur' }],
         
          },

        ppwdRules: {
        idCard: [{ required: true, message: '输入身份证号', trigger: 'blur' },
          { validator: validateiCard, trigger: 'blur' }],

        payPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' },
          { validator: validatePpw, trigger: 'blur' }],

        newPayPassword: [{ required: true, message: '请重复新密码', trigger: 'blur' },
          { validator: validatePPwdRepeat, trigger: 'blur' }],

        phone:[{required:true,message:"请输入预留的手机号",trigger:'blur'},
        {validator:validatePhone,trigger:'blur'}],

        verifyCode:[{required:true,validator: validateCode,trigger:'blur'}]
        
      }
    }
  },
  computed: {
    ...mapGetters([
      'id',
      'name',
      'age',
      'gender',
      'email'
    ])
  },
  created() {
    this.setInfoForm()
    this.setPwdForm()

  },
  methods: {
    setInfoForm() {
      this.infoForm.username = this.name
      this.infoForm.gender = this.gender.toString()
      this.infoForm.age = this.age
      this.infoForm.email = this.email
    },
    setPwdForm() {
      this.pwdForm.username = this.name
    },
    onSubmitInfo() {
      if (this.infoForm.username === this.name &&
          this.infoForm.gender === this.gender.toString() &&
          this.infoForm.age === this.age &&
          this.infoForm.email === this.email) {
        this.$message('您未作任何更改，未能提交！')
      } else {
        this.$refs['infoForm'].validate((valid) => {
          if (valid) {
            updateInfo(this.infoForm).then(() => {
              this.$store.dispatch('user/getInfo')
              this.$notify({
                title: 'OK',
                message: '修改成功',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      }
    },
    onSubmitPwd() {
      this.$refs['pwdForm'].validate((valid) => {
        if (valid) {
          if (this.pwdForm.oldPwd.indexOf(this.pwdForm.newPwd) >= 0) {
            this.$message({
              message: '新密码不能是旧密码的一部分！',
              type: 'warning'
            })
          } else {
            updatePwd({ 'username': this.name, 'password': this.pwdForm.newPwd }).then(() => {
              this.$alert('新密码已生效，请重新登录！', '修改成功', {
                confirmButtonText: '确定'
              }).finally(() => {
                this.$store.dispatch('user/logout').then(() => {
                  this.$router.push(`/login?redirect=${this.$route.fullPath}`)
                })
              })
            })
          }
        }
      })
    },
      onSubmitPwd() {
      this.$refs['ppwdForm'].validate((valid) => {
        if (valid) {
          if (this.pwdForm.oldPwd.indexOf(this.pwdForm.newPwd) >= 0) {
            this.$message({
              message: '新密码不能是旧密码的一部分！',
              type: 'warning'
            })
          } else {
            updatePwd({ 'username': this.name, 'password': this.pwdForm.newPwd }).then(() => {
              this.$alert('新密码已生效，请重新登录！', '修改成功', {
                confirmButtonText: '确定'
              }).finally(() => {
                this.$store.dispatch('user/logout').then(() => {
                  this.$router.push(`/login?redirect=${this.$route.fullPath}`)
                })
              })
            })
          }
        }
      })
    },
    onSubmitPpwd() {
      this.$refs['ppwdForm'].validate((valid) => {
        reSetPpwd({username:this.infoForm.username,idCard:this.ppwdForm.idCard,payPassword:this.ppwdForm.payPassword}).then(response=>
        { 
          if(response.code == 2000)
          {
            this.$alert("修改密码成功")
            setTimeout(()=>{this.$router.go(-1)},2000)
            // this.$router.push(`/login?redirect=${this.$route.fullPath}`)
          }            
          

        })
        
      })
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

  }
}
</script>

<style lang="scss" scoped>
.form-panel{
  margin-top: 40px;
}
.card-panel{
  margin-left: 30px;
  font-weight: bold;
  font-size: 24px;
}
</style>

