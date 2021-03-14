<template>
  <div ref="select_frame" ondragstart="return false;" style="width:500px;height:600px;background-color:black" class="a">
  </div>
</template>

<script>
// 这是一个拖动文件夹上传文件的demo组件
export default {
  props: {},
  data () {
    return {
    }
  },
  created () {},
  mounted () {
    this.$refs.select_frame.ondragleave = e => {
      e.preventDefault() // 阻止离开时的浏览器默认行为
    }
    this.$refs.select_frame.ondrop = e => {
      e.stopPropagation()
      e.preventDefault()

      let files = []
      let domFiles = e.dataTransfer.files
      let len = domFiles.length
      if (domFiles.length < 1) {
        return // 检测是否有文件拖拽到页面
      }

      for (let i = 0; i < len; i++) {
        files[i] = domFiles[i]
      }
      if (files.length) {
        let items = e.dataTransfer.items
        if (items && items.length && items[0].webkitGetAsEntry != null) {
          addFilesItems(items)
        }
      }
      function addFilesItems (items) {
        return (function () {
          var ret = []
          for (var i = 0; i < items.length; i++) {
            var item = items[i]
            var entry

            if (item.webkitGetAsEntry && (entry = item.webkitGetAsEntry())) {
              if (entry.isFile) {
              } else if (entry.isDirectory) {
                ret.push(addFilesFormDirectory(entry, entry.name))
              }
            }
          }
        })()
      }

      function addFilesFormDirectory (directory, path) {
        const dirReader = directory.createReader()
        console.log(directory, path)
        const readEntries = function readEntries () {
          return dirReader.readEntries(function (entries) {
            entries.forEach(function (entry) {
              if (entry.isFile) {
                entry.file(function (fileData) {
                  console.log(fileData)
                  fileData.fullPath = path + '/' + fileData.name
                  console.log(fileData)
                })
              } else if (entry.isDirectory) {
                console.log(entry.isDirectory)
                addFilesFormDirectory(entry, path + '/' + entry.name)
              }
            })
          })
        }
        readEntries(directory)
      }
    }
    this.$refs.select_frame.ondragenter = e => {
      e.preventDefault() // 阻止拖入时的浏览器默认行为
    }
    this.$refs.select_frame.ondragover = e => {
      e.preventDefault() // 阻止拖来拖去的浏览器默认行为
    }
  }
}
</script>

<style  lang="scss" scoped>
.a {
  position: fixed;
  top: 0;
  left: 0;
}
</style>
