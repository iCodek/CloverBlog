export function parseLyric (data) {
  const lyricList = []
  const lyricTime = []
  let array = data.split('\n')
  let timeReg = /\[(\d*:\d*\.\d*)\]/
  for (let element of array) {
    if (element.indexOf(']') === -1) continue
    let lyr = element.split(']')[1]
    let res = timeReg.exec(element)
    if (res == null || lyr.length <= 1) continue
    let timeStr = res[1]
    let min = parseInt(timeStr.split(':')[0]) * 60
    let sec = parseFloat(timeStr.split(':')[1])
    let time = +Number(min + sec).toFixed(2)
    lyricTime.push(time)
    lyricList.push(lyr)
  }
  // console.log([lyricList, lyricTime])
  return [lyricList, lyricTime]
}

export function curLyricIndex (lyricTime, curTime) {
  let id = 0
  let i = 0
  for (let element of lyricTime) {
    id = i
    if (element > (curTime + 0.2)) {
      // console.log(element, curTime, id - 1)
      break
    }
    id += 1
    i++
  }
  if (id <= 0) return 0
  return id - 1
}
