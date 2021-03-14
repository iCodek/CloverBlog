# 1 标题1

# 2 标题2

## 2.1 二级

## 2.2 二级

对方对方的

```js
like (item) {
  if (this.songs.includes(item.id)) return
  let copy = copySong(item)
  copy.source = 'Normal'
  copy.index = this.songs.length
  this.songs.push(copy.id)
  this.songlist.push(copy)
  this.playindex = copy.index
  saveFavorite(copy.id)
},
unlike (item) {
  let id = this.songs.indexOf(item.id)
  if (id !== -1) {
  this.songlist.splice(id, 1)
  this.songs.splice(id, 1)
  }
  this.playindex = -1
  this.songlist.forEach((item, index) => {
  item.index = index
  })
  deleteFavorite(item.id)
},
```



# 3 标题3

# 4 标题4

# 5 标题5

# 7 标题5

# 8 标题5

# 9 标题5

# 10 标题5

# 11 标题6

# 12 标题6

# 13 标题6

# 14 标题6

# 15 标题6