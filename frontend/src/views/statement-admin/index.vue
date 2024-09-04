<template>
  <div class="app-container">
    <div class="header-search">
      <el-form :inline="true" :model="queryForm" label-width="80px">
        <el-form-item label="账户">
          <el-select v-model="queryForm.objAccount" clearable placeholder="选择账户">
            <el-option v-for="account in accountList" :key="account.id" :label="account.id" :value="account.id">
              <span style="float: left">{{ account.id }}</span>
              <span style="float: right; color: #8492a6;">
                <el-divider direction="vertical" />
                {{ account.bankName }}
              </span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间" prop="startDate">
          <el-date-picker v-model="queryForm.startDate" type="datetime" placeholder="起始" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%;" />
        </el-form-item>
        <i class="el-icon-minus el-input__icon" />
        <el-form-item prop="endDate">
          <el-date-picker v-model="queryForm.endDate" type="datetime" placeholder="结束" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchTransactionList">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="resetQueryForm">重置</el-button>
        </el-form-item>
        <br>
        <el-form-item label="交易金额">
          <el-input v-model="queryForm.minAmount" placeholder="0.00" style="width: 100%;" />
        </el-form-item>
        <i class="el-icon-minus el-input__icon" />&nbsp;
        <el-form-item>
          <el-input v-model="queryForm.maxAmount" placeholder="30000.00" style="width: 100%;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="downloadExcel">交易明细下载</el-button> <!--TODO:下载csv-->
        </el-form-item>
      </el-form>
    </div>
    <el-divider />

    <el-table
      id="statementAdminTable"
      v-loading="transactionListLoading"
      :data="pageList"
      element-loading-text="加载中..."
      fit
      stripe
      highlight-current-row
      :default-sort="{prop: 'gmtCreate', order: 'descending'}"
      class="table-panel"
    >
      <el-table-column label="交易时间" prop="gmtCreate" width="100" sortable :formatter="dateFormatter" />
      <el-table-column label="支出方" prop="fromUser" min-width="10%" />
      <el-table-column label="支出账户" prop="fromAccount" min-width="25%" />
      <el-table-column label="交易金额" prop="amount" min-width="15%" sortable /> <!--fixme: 显示两位小数-->
      <el-table-column label="收入方" prop="toUser" min-width="10%" />
      <el-table-column label="收入账户" prop="toAccount" min-width="25%" />
      <el-table-column label="备注" prop="description" min-width="20%" />
      <el-table-column label="状态" prop="status" min-width="10%">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status|statusFilter(0)">{{ scope.row.status|statusFilter(1) }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      layout="prev, pager, next"
      hide-on-single-page
      :page-size="queryForm.limit"
      :total="transactionListTotal"
      class="pagination-class"
      @current-change="getPageList"
    />

    <div class="footer-info">
      <span> 当日交易明细存在变动，账务核对请以历史账单为准。 </span>
    </div>

  </div>
</template>

<script>
import { getAllAccount, getTransactionList } from '@/api/account'
import { export_table_to_excel } from '@/utils/excelHelper'

export default {
  filters: {
    statusFilter(status, index) {
      const statusMap = [['danger', '失败'], ['success', '成功']]
      return statusMap[status][index]
    }
  },
  data() {
    return {
      transactionList: [],
      pageList: [],
      transactionListTotal: 0,
      transactionListLoading: true,
      accountList: [],
      queryForm: {
        objAccount: '',
        startDate: null,
        endDate: null,
        minAmount: '',
        maxAmount: '',
        limit: 20,
        offset: 0
      }
    }
  },
  created() {
    this.setQueryForm()
    this.fetchAllAccount()
  },
  methods: {
    setQueryForm() {
      this.queryForm.objAccount = this.$route.params.account
      this.fetchTransactionList()
    },
    resetQueryForm() {
      this.queryForm = {
        objAccount: '',
        startDate: null,
        endDate: null,
        minAmount: '',
        maxAmount: '',
        limit: 20,
        offset: 0
      }
      this.fetchTransactionList()
    },
    fetchAllAccount() {
      getAllAccount().then(response => {
        this.accountList = response.data
      })
    },
    fetchTransactionList() {
      this.transactionListLoading = true
      getTransactionList(this.queryForm).then(response => {
        this.transactionList = response.data.transactionList
        this.transactionListTotal = response.data.total
        this.getPageList()
        this.transactionListLoading = false
      })
    },
    getPageList(currentPage) {
      currentPage = currentPage || 1
      this.queryForm.offset = (currentPage - 1) * this.queryForm.limit
      this.pageList = this.transactionList.filter((item, index) =>
        index < this.queryForm.offset + this.queryForm.limit &&
        index >= this.queryForm.offset)
    },
    dateFormatter(row, column) {
      const date = row[column.property].replace('T', ' ')
      return date.substring(0, date.lastIndexOf('.'))
    },
    downloadExcel() {
      let fileName
      if (this.queryForm.objAccount) {
        fileName = '卡号' + this.queryForm.objAccount + '交易明细'
      } else {
        fileName = '交易明细'
      }
      export_table_to_excel('statementAdminTable', fileName)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  margin: 30px;
  border: 2px solid #eff0f3;
}
.header-search{
  //border: 1px solid red;
}
.table-panel{
  padding: 5px;
  //border: 1px solid red;
}
.pagination-class{
  margin-top: 20px;
}
.footer-info{
  text-align: center;
  margin-top: 20px;
  color: #999;
}
</style>
