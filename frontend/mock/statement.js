const Mock = require('mockjs')

const data = Mock.mock({
  'statementList|30': [{
    myAccount: '@natural',
    tradeTime: '@datetime',
    'tradeType|1': ['转入', '转出'],
    amount: '@float(60, 100, 0, 2)',
    fee: '@float(0, 10, 0, 2)',
    tradeAccount: '@natural',
    tradeUser: '@cname',
    description: '@cparagraph(2)',
    'status|1': [0, 1]
  }]
})

module.exports = [
  {
    url: '/statement/list',
    type: 'get',
    response: config => {
      const statementList = data.statementList
      return {
        code: 20000,
        data: {
          total: statementList.length,
          statementList: statementList
        }
      }
    }
  }
]
