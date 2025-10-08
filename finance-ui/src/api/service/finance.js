import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listData(query) {
  return request({
    url: '/finance/data/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getData(id) {
  return request({
    url: '/finance/data/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addData(data) {
  return request({
    url: '/finance/data',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateData(data) {
  return request({
    url: '/finance/data',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delData(id) {
  return request({
    url: '/finance/data/' + id,
    method: 'delete'
  })
}
