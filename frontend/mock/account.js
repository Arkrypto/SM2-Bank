const Mock = require('mockjs')

const data = Mock.mock({
  'accountList|4': [{
    bankName: '@region()' + '银行',
    accountId: '@natural',
    // phone: /^[1]([3-9])[0-9]{9}$/,
    phone: '1' + '@natural(30, 99)' + '****' + '@natural(1000, 9999)',
    balance: '@float(60, 100, 0, 2)'
  }],
  'friendAccountList|4': [{
    bankName: '@region()' + '银行',
    accountId: '@natural()' + ' ', // fixme: 非字符串报错。。
    friendName: '@cname()'
  }]
})

module.exports = [
  {
    url: '/account/list',
    type: 'get',
    response: config => {
      const accountList = data.accountList
      return {
        code: 20000,
        data: {
          total: accountList.length,
          accountList
        }
      }
    }
  },
  {
    url: '/account/friend',
    type: 'get',
    response: config => {
      const friendAccountList = data.friendAccountList
      return {
        code: 20000,
        data: {
          total: friendAccountList.length,
          friendAccountList
        }
      }
    }
  }
]
