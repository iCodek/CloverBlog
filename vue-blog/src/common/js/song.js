export function copySong (obj) {
  if (typeof obj !== 'object') return obj
  let newobj = {}
  for (var attr in obj) {
    newobj[attr] = copySong(obj[attr])
  }
  return newobj
}
