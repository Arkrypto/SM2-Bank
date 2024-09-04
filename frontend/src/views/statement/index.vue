<template>
  <div class="app-container">
    <div class="header-search">
      <el-form :inline="true" :model="queryForm" label-width="80px">
        <el-form-item label="账户">
          <el-select v-model="queryForm.myAccount" placeholder="选择账户">
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
          <el-button type="primary" @click="fetchStatementList">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="resetQueryForm">重置</el-button>
        </el-form-item>
        <br>
        <el-form-item label="对方账户">
          <el-autocomplete
            v-model="queryForm.objAccount"
            class="inline-input"
            :fetch-suggestions="fetchFriendAccountSuggestions"
            placeholder="请输入对方账户"
            style="width: 100%;"
          />
        </el-form-item>
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
      id="statementTable"
      v-loading="statementListLoading"
      :data="pageList"
      element-loading-text="加载中..."
      fit
      stripe
      highlight-current-row
      :default-sort="{prop: 'tradeTime', order: 'descending'}"
      class="table-panel"
    >
      <el-table-column label="交易时间" prop="tradeTime" width="200" sortable :formatter="dateFormatter" />
      <el-table-column label="交易类型" prop="tradeType" width="100" sortable />
      <el-table-column label="交易金额" prop="amount" width="100" sortable />
      <!-- TODO: 增加余额记录列-->
      <el-table-column label="对方账户" prop="tradeAccount" width="200" />
      <el-table-column label="对方户名" prop="tradeUser" width="100" />
      <el-table-column label="备注" prop="description"  width="" />
      <el-table-column label="状态" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status|statusFilter(0)">{{ scope.row.status|statusFilter(1) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="80">
        <template slot-scope="scope">
          <el-button type="text" @click="showDetail(scope.$index, scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      layout="prev, pager, next"
      hide-on-single-page
      :page-size="queryForm.limit"
      :total="statementListTotal"
      class="pagination-class"
      @current-change="getPageList"
    />

    <div class="footer-info">
      <span> 当日交易明细存在变动，账务核对请以历史账单为准。 </span>
    </div>

    <el-dialog title="详细信息" :visible.sync="dialogDetailVisible" width="40%">
      <el-form :model="detailInfo" label-width="80px">
        <el-form-item label="账户">
          <el-input v-model="detailInfo.myAccount" readonly />
        </el-form-item>
        <el-form-item label="交易时间">
          <el-input v-model="detailInfo.tradeTime" readonly />
        </el-form-item>
        <el-form-item label="交易类型">
          <el-input v-model="detailInfo.tradeType" readonly />
        </el-form-item>
        <el-form-item label="交易金额">
          <el-input v-model="detailInfo.amount" readonly />
        </el-form-item>
        <el-form-item label="手续费">
          <el-input v-model="detailInfo.fee" readonly />
        </el-form-item>
        <el-form-item label="对方名称">
          <el-input v-model="detailInfo.tradeUser" readonly />
        </el-form-item>
        <el-form-item label="对方账户">
          <el-input v-model="detailInfo.tradeAccount" readonly />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="detailInfo.description" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" readonly />
        </el-form-item>
        <el-form-item label="交易签名">
          <el-input v-model="detailInfo.signature" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" readonly />
        </el-form-item>
        <el-form-item label="服务端签名">
          <el-input v-model="detailInfo.serverSignature" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" readonly />
        </el-form-item>
        <el-form-item label="交易时间戳">
          <el-input v-model="detailInfo.timeStamp" type="textarea" :autosize="{ minRows: 2, maxRows: 4}" readonly />
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="detailInfo.status" readonly />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="dialogDetailVisible = false">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAccountList, getFriendAccountList, getStatementList } from '@/api/account'
import { export_table_to_excel } from '@/utils/excelHelper'
import { mapGetters } from 'vuex'

export default {
  filters: {
    statusFilter(status, index) {
      const statusMap = [['danger', '失败'], ['success', '成功']]
      return statusMap[status][index]
    }
  },
  data() {
    return {
      statementList: [],
      pageList: [],
      statementListTotal: 0,
      statementListLoading: true,
      dialogDetailVisible: false,
      accountList: [],
      friendAccountList: [],
      queryForm: {
        myAccount: null,
        objAccount: null,
        startDate: null,
        endDate: null,
        minAmount: '',
        maxAmount: '',
        limit: 20,
        offset: 0
      },
      detailInfo: ''
    }
  },
  computed: {
    ...mapGetters([
      'id'
    ])
  },
  created() {
    this.fetchAccountList()
    this.fetchFriendAccountList()
  },
  methods: {
    setQueryForm() {
      this.queryForm.myAccount = this.$route.params.id || this.accountList[0].id
      this.fetchStatementList()
    },
    resetQueryForm() {
      this.queryForm = {
        myAccount: '',
        objAccount: null,
        startDate: null,
        endDate: null,
        minAmount: '',
        maxAmount: '',
        limit: 20,
        offset: 0
      }
      this.setQueryForm()
    },
    fetchAccountList() {
      // this.id = "xzt1"
      console.log("id: " + this.id)
      getAccountList(this.id).then(response => {
        this.accountList = response.data
        console.log(response.data[0])
        if (this.accountList) {
          this.setQueryForm()
        } else {
          setTimeout(() => {
            this.statementListLoading = false
          }, 2000)
        }
      })
    },
    fetchFriendAccountList() {
      getFriendAccountList(this.id).then(response => {
        this.friendAccountList = JSON.parse(
          JSON.stringify(response.data)
            .replace(/account/g, 'value'))
      })
    },
    fetchFriendAccountSuggestions(queryAccount, cb) {
      cb(queryAccount ? this.friendAccountList.filter(friendAccount => {
        return (friendAccount.value.toLowerCase().indexOf(queryAccount.toLowerCase()) === 0)
      }) : this.friendAccountList)// 调用 callback 返回建议列表的数据
    },
    fetchStatementList() {
      this.statementListLoading = true
      getStatementList(this.queryForm).then(response => {
        // console.log(response) // fixme: delete this
        this.statementList = response.data.statementList
        // console.log(this.statementList)
        this.statementListTotal = response.data.total
        this.getPageList()
        this.statementListLoading = false
      })
    },
    getPageList(currentPage) {
      currentPage = currentPage || 1
      this.queryForm.offset = (currentPage - 1) * this.queryForm.limit
      this.pageList = this.statementList.filter((item, index) =>
        index < this.queryForm.offset + this.queryForm.limit &&
        index >= this.queryForm.offset)
    },
    dateFormatter(row, column) {
      const date = row[column.property].replace('T', ' ')
      // console.log(date)
      console.log(row[column.property])
      return date.substring(0, date.lastIndexOf('.'))
    },
    showDetail(index, row) {
      const statusFilter = ['失败', '成功']
      this.dialogDetailVisible = true
      this.detailInfo = {
        myAccount: row.myAccount,
        tradeTime: row.tradeTime.substring(0, row.tradeTime.lastIndexOf('.')).replace('T', ' '),
        tradeType: row.tradeType,
        amount: row.amount,
        fee: row.fee || 0.00,
        tradeUser: row.tradeUser,
        tradeAccount: row.tradeAccount,
        description: row.description,
        status: statusFilter[row.status],
        signature: row.signature,
        serverSignature: row.server_signature,
        timeStamp: row.timeStamp
      }
    },
    downloadExcel() {
      const fileName = '卡号' + this.queryForm.myAccount + '交易明细'
      export_table_to_excel('statementTable', fileName)
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
