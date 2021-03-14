import axios from 'axios'
import {BlogHost} from 'common/js/config'

export function getBlogFile (blogId) {
  const url = BlogHost + `/getBlog?blogId=${blogId}`
  return axios.post(url)
}
export function getBlogList (id) {
  const url = BlogHost + `/getBlogList?id=${id}`
  return axios.post(url)
}
export function changeLikes (blogId, add) {
  const url = BlogHost + `/changeLikes?blogId=${blogId}&add=${add}`
  return axios.get(url)
}
