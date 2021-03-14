import axios from 'axios'
import {BlogHost} from 'common/js/config'

export function getBlogSearch (userId, keyword) {
  const url = BlogHost + `/search?userId=${userId}&keyword=${keyword}`
  return axios.get(url)
}
