import request from '@/utils/request'

export function getAccountList(id) {
  return request({
    url: '/account/list',
    method: 'get',
    params: { id }
  })
}

export function getAllAccount() {
  return request({
    url: '/account/admin/list',
    method: 'get'
  })
}
export function addAccount(data) {
  return request({
    url: '/account/admin/new',
    method: 'post',
    data
  })
}
export function editAccount(data) {
  return request({
    url: '/account/admin/edit',
    method: 'post',
    data
  })
}
export function delAccount(id) {
  return request({
    url: '/account/admin/del',
    method: 'delete',
    params: { id }
  })
}
export function getFriendAccountList(id) {
  return request({
    url: '/account/friend',
    method: 'get',
    params: { id }
  })
}


export function doWithdrawal(data) {
  return request({
    url: '/account/withdrawal',
    method: 'post',
    data
  })
}

export function doTransaction(data) {
  return request({
    url: '/account/transaction',
    method: 'post',
    data
  })
}

export function getStatementList(query) {
  return request({
    url: '/account/statement',
    method: 'get',
    params: query
  })
}
export function getTransactionList(query) {
  return request({
    url: '/account/admin/statement',
    method: 'get',
    params: query
  })
}
