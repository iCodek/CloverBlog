import _ from 'lodash/lodash'
// 截流函数
export function debounce (func, delay) {
  let timer
  return function (...args) {
    if (timer) {
      clearTimeout(timer)
    }
    timer = setTimeout(() => {
      func.apply(this, args)
    }, delay)
  }
}
// 排序数组
export function sortList (n) {
  let foo = []
  for (let i = 0; i < n; i++) {
    foo.push(i)
  }
  return foo
}

// 彩虹色
export function rainbowColor (len, min, max, t = 0.8) {
  if (min < 0 || max > 255) return
  if (len === 1) return ['#40aad6']
  let arr = sortList(len)
  let bgc = new Array(len).fill('rgba(')
  // let color = arr.map((a) => Math.floor(255 * (a + 1) / len))
  let color = arr.map((a) => Math.floor((max - min) * a / (len - 1)) + min)
  for (let i = 0; i < 3; i++) {
    let shu = _.shuffle(arr)
    for (let j = 0; j < shu.length; j++) {
      bgc[j] += color[shu[j]] + ','
      if (i === 2) {
        bgc[j] += t + ')'
      }
    }
  }
  return bgc
}
