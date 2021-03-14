import axios from 'axios'
import {UserHost, WebMasterHost} from 'common/js/config'

export function getAllUser () {
  const url = WebMasterHost + `/allUser`
  return axios.get(url)
}
export function getUserInfo (id) {
  const url = WebMasterHost + `/info?id=${id}`
  return axios.get(url)
}
export function visit () {
  const url = WebMasterHost + `/visit`
  return axios.get(url)
}
export function register (code, key, user) {
  const url = UserHost + `/register?code=${code}&key=${key}`
  return axios.post(url, user)
}
export function login (user) {
  const url = UserHost + `/login`
  return axios.post(url, user)
}
export function getCodeImage () {
  const url = UserHost + `/getImage`
  return axios.get(url)
}
export function saveAvatar (id, file) {
  const url = UserHost + `/saveAvatar?id=${id}`
  return axios.post(url, file)
}
export function saveBg (id, file) {
  const url = UserHost + `/saveBg?id=${id}`
  return axios.post(url, file)
}
export function saveTop (id, file) {
  const url = UserHost + `/saveTop?id=${id}`
  return axios.post(url, file)
}
export function saveContact (id, name, json) {
  const url = UserHost + `/saveContact?id=${id}&name=${name}`
  return axios.post(url, json)
}
export function saveSong (id, song) {
  const url = UserHost + `/saveSong?id=${id}`
  return axios.post(url, song)
}
export function saveNotice (id, notice) {
  const url = UserHost + `/saveNotice?id=${id}`
  return axios.post(url, notice)
}
export function saveApi (id, api) {
  const url = UserHost + `/saveApi?id=${id}`
  return axios.post(url, api)
}
export function saveBlog (file) {
  const url = UserHost + `/saveBlog`
  return axios.post(url, file)
}
export function autoLogin () {
  const url = UserHost + `/saveLogin`
  return axios.post(url)
}
export function saveFile (file) {
  const url = UserHost + `/saveFile`
  return axios.post(url, file)
}
export function saveVisible (blogId, visible) {
  const url = UserHost + `/saveVisible?blogId=${blogId}&visible=${visible}`
  return axios.get(url)
}
