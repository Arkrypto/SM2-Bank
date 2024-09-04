import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}
export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    params: { token }
  })
}
export function getUserList(token) {
  return request({
    url: '/user/admin/list',
    method: 'get',
    params: { token }
  })
}
export function addUser(data) {
  return request({
    url: '/user/admin/new',
    method: 'post',
    data
  })
}
export function editUser(data) {
  return request({
    url: '/user/admin/edit',
    method: 'post',
    data
  })
}
export function delUser(id) {
  return request({
    url: '/user/admin/del',
    method: 'delete',
    params: { id }
  })
}

export function updateInfo(data) {
  return request({
    url: '/user/info',
    method: 'post',
    data
  })
}
export function updatePwd(data) {
  return request({
    url: '/user/pwd',
    method: 'post',
    data
  })
}
export function reSetPpwd(data) {
  return request({
    url: '/user/ppwd',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function getVerifyCode(id) {
  return request({
    url: '/user/code',
    method: 'get',
    params: { id }
  })
}
export function verifyCode(params) {
  return request({
    url: '/user/verify/code',
    method: 'get',
    params
  })
}
export function verifyPwd(data) {
  return request({
    url: '/user/verify/pwd',
    method: 'post',
    data
  })
}
