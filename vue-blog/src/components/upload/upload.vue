<template>
  <div class="wrapper">
      <div class="all">
        <div class="upload">
          <div class="left">
            <div class="mdin" @dragleave.prevent.stop="" @drop.prevent.stop="ondrop($event,0)" @dragover.prevent="isIn" @dragleave.prevent="isOut" ref="select">
              将博客.md文件拖入此处，<br>若只上传图片/视频则无需拖入.md文件
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-blog"></use>
              </svg>
            </div>
            <div class="mdlist">
              <p v-if="fileList.length<1" style="text-align:center;font-size:30px;margin-top:40px">暂无文件</p>
              <ol>
                <li v-for="(item, i) in fileList" :key="i">{{i+1}}. {{item.name}}</li>
              </ol>
            </div>
          </div>
          <div class="right">
            <div class="imgin" @dragleave.prevent.stop="" @drop.prevent.stop="ondrop($event,1)" @dragover.prevent="isImgIn" @dragleave.prevent="isImgOut" ref="selectImg">
              <div v-if="imgList.length<1" class="imgtext">将图片/视频拖入此处
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#icon-tupian1"></use>
                </svg>
              </div>
              <div class="img" :style="{backgroundImage:`url(${img})`}" v-for="(img, i) in imgUrl" :key="i">
                <div class="x" @click="remove(i)">
                  <svg class="icon" aria-hidden="true">
                    <use xlink:href="#icon-qingchu1"></use>
                  </svg>
                </div>
              </div>
            </div>
            <div class="imglist">
              <p v-if="imgList.length<1" style="text-align:center;font-size:30px;margin-top:40px">暂无图片</p>
              <ol>
                <li v-for="(item, i) in imgList" :key="i">{{i+1}}. {{item.name}}</li>
              </ol>
            </div>
          </div>
        </div>
        <div class="footer">
          <div class="desc">
            <textarea class="description" placeholder="在此填写博客摘要" v-model="blog.preview"></textarea>
            <textarea class="tags" placeholder="在此填写博客标签,以,分隔(例如:Springboot,java,python)" v-model="blog.tags"></textarea>
          </div>
          <div class="right">
            <input class="title" placeholder="博客标题" v-model="blog.title"/>
            <input class="title" list="category" placeholder="博客分类" v-model="blog.category"/>
            <datalist id="category">
              <option v-for="(b,i) in category" :key="i" :value="b"></option>
            </datalist>
            <div class="bt">
              所有人可见
              <input type="checkbox" v-model="blog.visible">
              <div class="btn" @click="save()">上传</div>
            </div>
          </div>
        </div>
        <div class="close" @click="back">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icon-siyecaox"></use>
          </svg>
        </div>
      </div>
  </div>
</template>

<script>
import {mapGetters, mapMutations} from 'vuex'
import {VueCropper} from 'vue-cropper'
import {saveBlog, saveFile} from 'api/user'
export default {
  components: {
    VueCropper
  },
  props: {},
  data () {
    return {
      fileList: [],
      imgList: [],
      imgUrl: [],
      imgName: [],
      formData: new FormData(),
      blog: {'visible': true, 'tags': ''}
    }
  },
  watch: {},
  computed: {
    category () {
      let array = []
      for (let i = 0; i < this.blogs.length; i++) {
        if (array.indexOf(this.blogs[i].category) === -1) {
          array.push(this.blogs[i].category)
        }
      }
      return array
    },
    ...mapGetters([
      'user',
      'blogs'
    ])
  },
  methods: {
    back () {
      this.$emit('back')
    },
    ondrop (e, num) {
      this.$refs.select.style.border = ''
      this.$refs.selectImg.style.border = ''
      let data = e.dataTransfer.files
      if (data.length < 1) {
        return
      }
      if (num === 0) {
        if (e.dataTransfer.files[0].name.indexOf('md') === -1) {
          alert('只允许上传.md文件')
          return
        }
        this.formData.append('mds', e.dataTransfer.files[0], e.dataTransfer.files[0].name)
        this.fileList = [e.dataTransfer.files[0]]
      } else if (num === 1) {
        let windowURL = window.url || window.webkitURL
        for (let i = 0; i < e.dataTransfer.files.length; i++) {
          if (e.dataTransfer.files[i].size > 20000000) {
            alert('文件最大为20MB')
            continue
          }
          // if (!/.(gif|jpg|jpeg|png)$/.test(e.dataTransfer.files[i].name)) {
          //   alert('媒体文件类型必须是gif,jpeg,jpg,png,mp4中的一种')
          //   return
          // }
          if (!this.imgName.includes(e.dataTransfer.files[i].name)) {
            this.imgUrl.push(windowURL.createObjectURL(e.dataTransfer.files[i]))
            this.formData.append('imgs', e.dataTransfer.files[i], e.dataTransfer.files[i].name)
            this.imgList.push(e.dataTransfer.files[i])
            this.imgName.push(e.dataTransfer.files[i].name)
          }
        }
      }
      // console.log(e.dataTransfer.files)
      // 本博客站点的后端部分也基本完成，踩个点介绍下更新内容，后续会侧重于博客文章的更新。
      // const formData = new FormData()
      // for (let i = 0; i < e.dataTransfer.files.length; i++) {
      //   // console.log(e.dataTransfer.files[i])
      //   if (e.dataTransfer.files[i].name.indexOf('md') === -1) {
      //     alert('只允许上传.md文件')
      //     return
      //   }
      //   formData.append('blogs', e.dataTransfer.files[i], e.dataTransfer.files[i].name)
      //   this.fileList.push(e.dataTransfer.files[i])
      // }
      // this.fileList = this.fileList.concat(e.dataTransfer.files[0])
      // saveBlog(this.user.id, formData).then((res) => {
      //   // this.setUser(res.data.data)
      // })
    },
    isIn () {
      this.$refs.select.style.border = '2px dashed red'
    },
    isOut () {
      this.$refs.select.style.border = ''
    },
    isImgIn () {
      this.$refs.selectImg.style.border = '2px dashed red'
    },
    isImgOut () {
      this.$refs.selectImg.style.border = ''
    },
    remove (i) {
      this.imgUrl.splice(i, 1)
      this.imgList.splice(i, 1)
      this.imgName.splice(i, 1)
    },
    save () {
      if (!this.fileList.length && this.imgUrl.length) {
        let cf = confirm('将只上传图片/视频')
        if (!cf) return
        this.formData.append('blog', new Blob([JSON.stringify({userId: this.user.id})], {type: 'application/json'}))
        saveFile(this.formData).then((res) => {
          alert(res.data.msg)
          if (res.data.state) {
            let s = '各文件的URL如下（已复制至剪贴板）\n'
            let t = ''
            let map = res.data.data
            for (let fileName in map) {
              t += fileName + ': ' + map[fileName] + '\n'
            }
            this.copyText(s + t)
          }
        })
        return
      } else if (!(this.fileList.length && this.blog.title && this.blog.category && this.blog.preview)) {
        alert('md文件、标题、摘要、分类不允许为空')
        return
      }

      this.blog.userId = this.user.id
      if (this.blog.tags && !Array.isArray(this.blog.tags)) {
        let syb = [',', '，', ';', '、', '；']
        let symbol = ' '
        for (let s of syb) {
          if (this.blog.tags.includes(s)) {
            symbol = s
            break
          }
        }
        let tags = this.blog.tags.split(symbol).filter(i => i && i.trim()).map(i => i.trim())
        this.blog.tags = tags
      } else {
        this.blog.tags = this.blog.tags || []
      }
      this.formData.append('blog', new Blob([JSON.stringify(this.blog)], {type: 'application/json'}))
      saveBlog(this.formData).then((res) => {
        console.log(res.data)
        if (res.data.state) {
          this.setBlogs(res.data.data)
          this.clear()
        } else if (res.data.msg === '相同博客文件在服务器已存在,是否覆盖') {
          console.log(1111)
          if (!confirm(res.data.msg)) return
          this.formData.delete('blog')
          this.blog.likes = 1
          this.save()
          return
        }
        alert(res.data.msg)
      })
    },
    clear () {
      this.fileList = []
      this.imgList = []
      this.imgUrl = []
      this.imgName = []
      this.formData = new FormData()
      this.blog = {'visible': true, 'tags': ''}
    },
    ...mapMutations({
      setUser: 'SET_USER',
      setBlogs: 'SET_BLOGS'
    })
  },
  created () {},
  mounted () {}
}
</script>
<style  lang="scss" scoped>
.wrapper{
  position: fixed;
  top: 0;
  right: 0;
  height: 100%;
  width: 100%;
  z-index: 1800;
  textarea::-webkit-input-placeholder, input::-webkit-input-placeholder {
    color: rgba(101, 173, 221, 0.8);
  }
  .all {
    position: absolute;
    height: 60%;
    width: 50%;
    top: 15%;
    left: 50%;
    transform: translate(-50%,0);
    background-color: rgb(65, 64, 64);
    box-shadow: 0 0 20px 10px rgb(255, 255, 255);
    border-radius: 12px;
    overflow: hidden;
    max-width: 1000px;
    min-width: 300px;
    min-height: 450px;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 22px;
      // &::-webkit-scrollbar {
      //   width: 10px;
      //   // height: 10px;
      // }
      // &::-webkit-scrollbar-thumb {
      //   border-radius: 10px;
      //   box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
      //   background: #999;
      // }
    .upload {
      // height: 80%;
      flex: 1;
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      .left {
        height: 100%;
        width: calc(50% - 10px);
        background-color: beige;
        border-radius: 10px;
        .mdin {
          text-align: center;
          border-top-left-radius: 10px;
          border-top-right-radius: 10px;
          height: 60%;
          width: 100%;
          background-color: skyblue;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          font-size: 25px;
          box-sizing: border-box;
          svg {
            font-size: 80px;
            margin-top: 20px;
          }
        }
        .mdlist {
          overflow: auto;
          font-size: 20px;
          li {
            list-style: none;
          }
        }
      }
      .right {
        height: 100%;
        width: calc(50% - 10px);
        background-color: beige;
        border-radius: 10px;
        .imgin {
          border-top-left-radius: 10px;
          border-top-right-radius: 10px;
          height: 60%;
          width: 100%;
          background-color: skyblue;
          font-size: 0;
          box-sizing: border-box;
          overflow: auto;
          padding: 2px;
          position: relative;
          .img {
            display: inline-block;
            width: calc(25% - 2px);
            height: 33.3%;
            margin: 1px;
            background-size: cover;
            background-position: center center;
            position: relative;
            .x {
              position: absolute;
              top: 0;
              right: 0;
              opacity: 0;
              transition: opacity 0.2s;
              svg {
                font-size: 30px;
              }
            }
            &:hover > .x{
              opacity: 1;
            }
          }
          .imgtext {
            font-size: 24px;
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            svg {
              font-size: 80px;
              margin-top: 20px;
            }
          }
        }
        .imglist {
          padding-left: 10px;
          height: 40%;
          overflow: auto;
          overflow-y: auto;;
          font-size: 20px;
          li {
            list-style: none;
            white-space: nowrap;
          }
        }
      }
    }
    .footer {
      padding: 4px;
      padding-top: 0;
      height: 140px;
      min-height: 140px;
      width: 100%;
      background-color: #b7e091;
      margin-top: 10px;
      border-radius: 10px;
      display: flex;
      overflow: auto;
      .desc {
        flex: 1;
        margin-right: 10px;
        display: flex;
        flex-direction: column;
        .description {
          margin-top: 5px;
          flex: 1;
          resize: none;
          padding: 5px;
          font-size: 24px;
          border-radius: 5px;
        }
        .tags {
          margin-top: 5px;
          height: 30px;
          font-size: 16px;
          line-height: 30px;
          // background-color: rgb(236, 236, 236);
          resize: none;
          padding-left: 5px;
          border-radius: 5px;
        }
      }
      .right {
        width: 30%;
        height: 100%;
        display: flex;
        min-width: 200px;
        flex-direction: column;
        .title {
          margin-top: 5px;
          width: calc(100% - 15px);
          resize: none;
          flex: 1;
          padding: 5px;
          font-size: 20px;
          margin-bottom: 5px;
          border-radius: 5px;
        }
        .bt {
          display: flex;
          align-items: center;
          font-size: 18px;
          white-space: nowrap;
          input {
            margin: 5px;
            margin-right: 30px;
            width: 16px;
            height: 16px;
          }
        }
        .btn {
          flex: 1;
          line-height: 36px;
          height: 36px;
          white-space: nowrap;
          cursor: pointer;
          background: #fff;
          border: 1px solid #000000;
          color: #1f2d3d;
          text-align: center;
          box-sizing: border-box;
          outline: none;
          font-size: 25px;
          border-radius: 4px;
          color: #fff;
          background-color: #50bfff;
          transition: all .2s ease;
          text-decoration: none;
          user-select: none;
        }
      }
    }
    .close {
      position: absolute;
      width: 30px;
      height: 30px;
      right: 0;
      top: 0;
      svg {
        font-size: 30px;
      }
    }
  }
}
</style>
