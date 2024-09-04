import request from '@/utils/request'

export function getLogList(token) {
  return request({
    url: '/log/list',
    method: 'get',
    params: { token }
  })
}

export function delLog(id) {
  return request({
    url: '/log/del',
    method: 'delete',
    params: { id }
  })
}
export function delAll() {
  return request({
    url: '/log/del/all',
    method: 'delete'
  })
}
