<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <el-row type="flex" :gutter="20" justify="space-between">
        <el-col :span="12">
          <el-input
            v-model="queryUserName"
            placeholder="输入卡号查询"
            prefix-icon="el-icon-search"
            @input="findUserByName(queryUserName)"
          />
        </el-col>
        <el-col :span="6" style="text-align: right">
          <el-button type="primary" @click="showUserForm">新增用户</el-button>
        </el-col>
      </el-row>
    </div>
    <el-divider />

    <div>
      <el-table
        ref="userTable"
        :data="pageList"
        :row-key="getRowKey"
        :expand-row-keys="singleRow"
        style="width: 100%"
        stripe
        @expand-change="expandUser"
      >
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-table
              :data="accountList"
              border
              size="small"
              :header-cell-style="{'text-align':'center'}"
              :cell-style="{'text-align':'center'}"
            >
              <el-table-column label="姓名" min-width="10%">{{ props.row.username }}</el-table-column>
              <el-table-column label="银行卡号" prop="id" width="200px" />
              <el-table-column label="开户银行" prop="bankName" min-width="20%" />
              <el-table-column label="可用余额" prop="balance" min-width="20%" />
              <el-table-column label="用户电话" prop="phone" min-width="20%" />
              <el-table-column label="状态" prop="status" min-width="10%">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.status|statusFilter(0)">{{ scope.row.status|statusFilter(1) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="20%">
                <template slot="header" style="text-align: center">
                  <el-button type="primary" size="small" @click="showAccountForm">新增账户</el-button>
                </template>
                <template slot-scope="scope">
                  <el-button size="mini" type="info" @click="toStatement(scope.row.id)">账单</el-button>
                  <el-button size="mini" type="warning" @click="showAccountForm(scope.row)">编辑</el-button>
                  <el-popconfirm title="确定删除该账户？" @confirm="delAccount(scope.row)">
                    <el-button slot="reference" size="mini" type="danger">删除</el-button>
                  </el-popconfirm>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <el-table-column label="ID" prop="id" min-width="7%" />
        <el-table-column label="用户姓名" prop="username" min-width="15%" />
        <el-table-column label="用户角色" prop="role" min-width="15%">
          <template slot-scope="scope">
            <el-tag size="mimi" :type="scope.row.role|roleFilter(0)">{{ scope.row.role|roleFilter(1) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="用户年龄" prop="age" min-width="15%" />
        <el-table-column label="用户性别" prop="gender" min-width="15%">
          <template slot-scope="scope">
            <el-tag size="mimi" :type="scope.row.gender|genderFilter(0)">{{ scope.row.gender|genderFilter(1) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="用户邮箱" prop="email" min-width="25%" />
        <el-table-column label="用户状态" prop="status" min-width="15%">
          <template slot-scope="scope">
            <el-tag size="mimi" :type="scope.row.status|statusFilter(0)">{{ scope.row.status|statusFilter(1) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="用户头像" prop="avatar" />
        <el-table-column label="操作" min-width="23%">
          <template slot-scope="scope">
            <el-button size="mini" @click="showUserForm(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除该用户？" @confirm="delUser(scope.row)">
              <el-button slot="reference" size="mini" type="danger">删除</el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog title="填写用户信息" :visible.sync="userFormVisible" width="35%">
      <el-form ref="userForm" :model="userForm" label-width="80px" :rules="userRules">
        <el-form-item v-show="userReadonly" label="用户ID" prop="id">
          <el-input v-model="userForm.id" readonly />
        </el-form-item>
        <el-form-item label="用户姓名" prop="username">
          <el-input v-model="userForm.username" :readonly="userReadonly" />
        </el-form-item>
        <el-form-item label="用户密码" prop="password">
          <el-input v-model="userForm.password" />
        </el-form-item>
        <el-form-item label="用户角色" prop="role">
          <el-radio v-model="userForm.role" label="1">管理员</el-radio>
          <el-radio v-model="userForm.role" label="0">普通用户</el-radio>
        </el-form-item>
        <el-form-item label="用户年龄" prop="age">
          <el-input v-model.number="userForm.age" />
        </el-form-item>
        <el-form-item label="用户性别" prop="gender">
          <el-radio v-model="userForm.gender" label="1">男</el-radio>
          <el-radio v-model="userForm.gender" label="0">女</el-radio>
        </el-form-item>
        <el-form-item label="用户邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
        <el-form-item label="用户头像" prop="avatar">
          <el-input v-model="userForm.avatar" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio v-model="userForm.status" label="1">冻结</el-radio>
          <el-radio v-model="userForm.status" label="0">正常</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="userFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitUser">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="填写账户信息" :visible.sync="accountFormVisible" width="35%">
      <el-form ref="accountForm" :model="accountForm" label-width="80px" :rules="accountRules">
        <el-form-item label="银行卡号" prop="id">
          <el-input v-model="accountForm.id" :readonly="accountReadonly" />
        </el-form-item>
        <el-form-item label="开户银行" prop="bankName">
          <el-autocomplete
            v-model="accountForm.bankName"
            class="inline-input"
            :fetch-suggestions="fetchSuggestedBank"
            placeholder="请输入开户银行"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="账户余额" prop="balance">
          <el-input v-model="accountForm.balance" oninput="value=value.replace(/[^0-9.]/g,'')" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="accountForm.phone" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio v-model="accountForm.status" label="1">冻结</el-radio>
          <el-radio v-model="accountForm.status" label="0">正常</el-radio>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="accountFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAccount">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getUserList, addUser, editUser, delUser } from '@/api/user'
import { getAccountList, addAccount, editAccount, delAccount } from '@/api/account'

const bankSuggestion = [
  { value: '中国银行' },
  { value: '交通银行' },
  { value: '建设银行' },
  { value: '招商银行' },
  { value: '花旗银行' },
  { value: '农业银行' }
]
export default {
  name: 'AdminDashboard',
  filters: {
    statusFilter(status, index) {
      const statusMap = [['success', '正常', '正常'], ['danger', '冻结', '未激活']]
      return statusMap[status][index]
    },
    roleFilter(status, index) {
      const statusMap = [['info', '普通'], ['warning', '管理员']]
      return statusMap[status][index]
    },
    genderFilter(status, index) {
      const statusMap = [['danger', '女'], ['primary', '男']]
      return statusMap[status][index]
    }
  },
  data() {
    const validateAccount = (rule, value, callback) => {
      // 普通借记卡一般是18-21位，信用卡都是16位的
      // https://www.cardbaobao.com/cardnews/cardnews_21967.shtml
      const re = /^[1-9][0-9]{15,20}$/
      if (value.toString().length !== 17 && re.test(value)) {
        callback()
      } else {
        callback(new Error('请输入正确的银行卡号'))
      }
    }
    const validatePhone = (rule, value, callback) => {
      const re = /^(13[0-9]|14[5|7]|15[0-9]|18[0-9])\d{8}$/
      if (re.test(value)) {
        callback()
      }
      callback(new Error('请输入正确的手机号码'))
    }
    const validateAge = (rule, value, callback) => {
      if (value < 1 || value > 100) {
        callback(new Error('年龄必须在1-100之间!'))
      } else {
        callback()
      }
    }
    const validateNewPwd = (rule, value, callback) => {
      const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,16}$/
      if (!re.test(value)) {
        callback(new Error('密码至少包含大小写字母和数字，且不少于8位'))
      } else {
        callback()
      }
    }
    return {
      singleRow: [],
      getRowKey(row) {
        return row.id
      },
      userList: [],
      pageList: [],
      accountList: [],

      accountFormVisible: false,
      submitAccountAction: '',
      accountReadonly: false,
      accountForm: {
        id: '',
        bankName: '',
        phone: '',
        balance: '0.00',
        status: '0',
        ownerId: this.id
      },
      accountRules: {
        id: [{ required: true, message: '请输入银行卡号', trigger: 'blur' },
          { validator: validateAccount, trigger: 'blur' }],
        bankName: [{ required: true, message: '请输入开户银行', trigger: ['blur', 'change'] }],
        phone: [{ required: true, message: '请输入预留手机号码', trigger: 'change' },
          { validator: validatePhone, trigger: 'blur' }],
        balance: [{ required: true, message: '请输入余额', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      },

      userFormVisible: false,
      submitUserAction: '',
      userReadonly: false,
      queryUserName: '',
      userForm: {
        id: '',
        username: '',
        password: '',
        role: '0',
        age: '',
        gender: '1',
        email: '',
        status: '0',
        avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif'
      },
      userRules: {
        username: [{ required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 5, message: '长度在 2 到 5 个字符', trigger: 'blur' }],
        password: [{ required: true, message: '新密码不能为空', trigger: 'blur' },
          { validator: validateNewPwd, trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }],
        age: [{ required: true, message: '请输入年龄', trigger: 'blur' },
          { type: 'number', message: '请输入数字值', trigger: 'blur' },
          { validator: validateAge, trigger: 'blur' }],
        gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
        email: [{ required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }],
        avatar: [{ required: true, message: '请输入头像地址', trigger: 'blur' }] }
    }
  },
  computed: {
    ...mapGetters([
      'id',
      'token',
      'name'
    ])
  },
  created() {
    this.fetchUserList()
  },
  methods: {
    expandUser(row, expandedRows) {
      this.singleRow = []
      if (expandedRows.length > 0) {
        row ? this.singleRow.push(row.id) : ''
      }
      this.fetchAccountList(row.id)
    },
    fetchUserList() {
      getUserList(this.token).then(response => {
        this.userList = response.data
        this.pageList = this.userList
      })
    },
    fetchAccountList(uid) {
      getAccountList(uid).then(response => {
        this.accountList = response.data
      })
    },
    findUserByName(queryUserName) {
      this.pageList = this.userList.filter(user => {
        return user.username.indexOf(queryUserName) >= 0
      })
    },
    fetchSuggestedBank(bankName, cb) {
      cb(bankName ? bankSuggestion.filter(item => {
        return (item.value.toLowerCase().indexOf(bankName.toLowerCase()) === 0)
      }) : bankSuggestion)// 调用 callback 返回建议列表的数据
    },
    showAccountForm(row) {
      if (row.id) { // edit
        this.accountForm.id = row.id
        this.accountForm.bankName = row.bankName
        this.accountForm.balance = row.balance
        this.accountForm.phone = row.phone
        this.accountForm.status = row.status.toString()
        this.userReadonly = true
        this.submitAccountAction = editAccount
      } else { // add
        this.accountForm.ownerId = this.singleRow[0]
        this.userReadonly = false
        this.submitAccountAction = addAccount
      }
      this.accountFormVisible = true
    },
    submitAccount() {
      this.$refs['accountForm'].validate((valid) => {
        if (valid) {
          this.submitAccountAction(this.accountForm).then(response => {
            this.$notify({
              title: 'OK',
              message: response.message,
              type: 'success'
            })
            this.fetchAccountList(this.singleRow[0])
          }).finally(() => {
            this.accountFormVisible = false
          })
        }
      })
    },
    delAccount(row) {
      delAccount(row.id).then(response => {
        this.$notify({
          title: 'OK',
          message: response.message,
          type: 'success'
        })
        this.fetchAccountList(this.singleRow[0])
      })
    },

    showUserForm(row) {
      if (row.id) { // edit
        this.userForm.id = row.id
        this.userForm.username = row.username
        this.userForm.role = row.role.toString()
        this.userForm.age = row.age
        this.userForm.gender = row.gender.toString()
        this.userForm.email = row.email
        this.userForm.status = row.status.toString()
        this.userForm.avatar = row.avatar
        this.userReadonly = true
        this.submitUserAction = editUser
      } else { // add
        this.userReadonly = false
        this.submitUserAction = addUser
      }
      this.userFormVisible = true
    },
    submitUser() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          this.submitUserAction(this.userForm).then(response => {
            this.$notify({
              title: 'OK',
              message: response.message,
              type: 'success'
            })
            this.fetchUserList()
          }).finally(() => {
            this.userFormVisible = false
          })
        }
      })
    },
    delUser(row) {
      delUser(row.id).then(response => {
        this.$notify({
          title: 'OK',
          message: response.message,
          type: 'success'
        })
        this.fetchUserList()
      })
    },
    toStatement(account) {
      this.$router.push({ name: 'StatementAdmin', params: { account }})
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
    //border: 1px solid #ff0000;
  }
  &-header {
    margin-bottom: 20px;
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
