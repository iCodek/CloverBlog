import storage from 'good-storage'

const USER = '__user__'
const TOKEN = '__token__'
const AUTOLOGIN = '__auto__'
const BOXWIDTH = '__width__'
const SEARCH = '__search__'

export function saveUser (user) {
  let temp = storage.get(USER, [])
  if (!temp.includes(user)) temp.push(user)
  storage.set(USER, temp)
}

export function loadUser () {
  return storage.get(USER, [])
}

export function saveToken (token) {
  storage.set(TOKEN, token)
}

export function loadToken () {
  if (!storage.has(TOKEN)) return ''
  return storage.get(TOKEN, String)
}

export function saveAuto (auto) {
  storage.set(AUTOLOGIN, auto)
}

export function loadAuto () {
  if (!storage.has(AUTOLOGIN)) return false
  return storage.get(AUTOLOGIN, Boolean)
}
export function reset () {
  storage.remove(AUTOLOGIN)
  storage.remove(TOKEN)
}
export function saveWidth (width) {
  storage.set(BOXWIDTH, width)
}
export function loadWidth () {
  if (!storage.has(BOXWIDTH)) return 48
  return storage.get(BOXWIDTH, Number)
}
export function saveSearch (text) {
  let temp = storage.get(SEARCH, [])
  if (!temp.includes(text)) temp.unshift(text)
  temp.length = temp.length > 5 ? 5 : temp.length
  storage.set(SEARCH, temp)
}
export function loadSearch () {
  return storage.get(SEARCH, [])
}
