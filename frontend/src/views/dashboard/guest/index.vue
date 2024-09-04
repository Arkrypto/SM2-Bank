<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <el-row type="flex" :gutter="20" justify="space-between">
        <el-col :span="12">
          <el-input
            v-model="queryBankId"
            placeholder="输入卡号查询"
            prefix-icon="el-icon-search"
            @input="findBankById(queryBankId)"
          />
        </el-col>
      </el-row>
    </div>
    <el-divider />

    <div>
      <el-row :gutter="20">
        <el-col v-for="account in pageList" :key="account.id" :span="8">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>{{ account.bankName }}</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="toStatement(account)">查看明细</el-button>
            </div>
            <div class="box-card-body">
              <div class="box-card-body-item">
                <span class="box-card-body-label">账户</span>
                <span>{{ account.id }}</span>
              </div>
              <div class="box-card-body-item">
                <span class="box-card-body-label">状态</span>
                <el-tag :type="account.status|statusFilter(0)" size="mini">{{ account.status|statusFilter(1) }}</el-tag>
              </div>
              <div class="box-card-body-item">
                <span class="box-card-body-label">余额</span>
                <span>{{ account.balance.toFixed(2) }}</span>
                <el-button style="float: right;" icon="el-icon-s-finance" circle @click="toTransaction(account)" />
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getAccountList } from '@/api/account'

export default {
  name: 'GuestDashboard',
  filters: {
    statusFilter(status, index) {
      const statusMap = [['primary', '正常'], ['danger', '冻结']]
      return statusMap[status][index]
    }
  },
  data() {
    return {
      accountList: [],
      pageList: [],
      queryBankId: ''
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
  },
  methods: {
    fetchAccountList() {
      getAccountList(this.id).then(response => {
        this.accountList = response.data
        this.pageList = this.accountList
      })
    },
    findBankById(queryBankId) {
      this.pageList = this.accountList.filter(account => {
        return (account.id.toLowerCase().indexOf(queryBankId.toLowerCase()) >= 0)
      })
    },
    toStatement(account) {
      this.$router.push({ name: 'Statement', params: account })
    },
    toTransaction(account) {
      this.$router.push({ name: 'Transaction', params: account })
    }
    // toWithdrawal(account) { // hint: 已弃用
    //   this.$router.push({ name: 'Withdrawal', params: account })
    // }
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
.box-card{
  margin: 20px 0;
}

.box-card-body {
  font-weight: bold;
  &-item {
    margin: 20px;
  }
  &-label {
    color: #9f9fa0;
    padding-right: 20px;
  }
}
</style>
