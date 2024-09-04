<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <el-row type="flex" :gutter="20" justify="space-between">
        <el-col :span="12">
          <el-input
            v-model="queryUsername"
            placeholder="输入操作人姓名查询"
            prefix-icon="el-icon-search"
            @input="findLogByUsername(queryUsername)"
          />
        </el-col>
        <el-col :span="6" style="text-align: right">
          <el-popconfirm
            title="能力越大，责任越大！"
            confirm-button-text="确认清空"
            icon="el-icon-warning"
            icon-color="red"
            @confirm="delAllLog"
          >
            <el-button slot="reference" type="danger">清空日志</el-button>
          </el-popconfirm>
        </el-col>
      </el-row>
    </div>
    <el-divider />
    <el-table
      :data="pageList"
      style="width: 100%"
      stripe
      :default-sort="{prop: 'gmtCreate', order: 'descending'}"
      :header-cell-style="{'text-align':'center'}"
      :cell-style="{'text-align':'center'}"
    >
      <el-table-column label="操作时间" prop="gmtCreate" width="100" sortable :formatter="dateFormatter" />
      <el-table-column label="操作人" prop="username" min-width="15%" />
      <el-table-column label="操作类型" prop="type" min-width="15%">
        <template slot-scope="scope">
          <el-tag size="mimi" :type="scope.row.type|typeFilter(0)">{{ scope.row.type|typeFilter(1) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作对象" prop="obj" min-width="20%" />
      <el-table-column label="操作结果" prop="result" min-width="15%">
        <template slot-scope="scope">
          <el-tag size="mimi" :type="scope.row.result|resultFilter(0)">{{ scope.row.result|resultFilter(1) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="具体描述" prop="description" min-width="25%" />
      <el-table-column label="操作" min-width="13%">
        <template slot-scope="scope">
          <el-popconfirm title="确定删除该条日志？" @confirm="delLogById(scope.row)">
            <el-button slot="reference" size="mini" type="danger">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getLogList, delLog, delAll } from '@/api/log'

export default {
  name: 'Log',
  filters: {
    resultFilter(status, index) {
      const statusMap = [['danger', '失败'], ['success', '成功']]
      return statusMap[status][index]
    },
    typeFilter(status, index) {
      const statusMap = [
        ['', '创建'],
        ['danger', '删除'],
        ['warning', '修改'],
        ['info', '读取']]
      return statusMap[status][index]
    }
  },
  data() {
    return {
      logList: [],
      pageList: [],
      queryUsername: ''
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'token'
    ])
  },
  created() {
    this.fetchLogList()
  },
  methods: {
    fetchLogList() {
      getLogList(this.token).then(response => {
        this.logList = response.data
        this.pageList = this.logList
      })
    },
    delLogById(row) {
      delLog(row.id).then(response => {
        this.$notify({
          title: 'OK',
          message: response.message,
          type: 'success'
        })
        this.fetchLogList()
      })
    },
    delAllLog() {
      delAll().then(response => {
        this.$notify({
          title: 'OK',
          message: response.message,
          type: 'success'
        })
        this.fetchLogList()
      })
    },
    findLogByUsername(queryUsername) {
      this.pageList = this.logList.filter(log => {
        return (log.username.toLowerCase().indexOf(queryUsername.toLowerCase()) >= 0)
      })
    },
    dateFormatter(row, column) {
      const date = row[column.property].replace('T', ' ')
      return date.substring(0, date.lastIndexOf('.'))
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
