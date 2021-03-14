<template>
  <div class="wrapper">
    <div class="setting">
      <div class="left">
        <div class="set" v-for="(v,k) in setting" :key="k" :style="{paddingRight:v?'10px':''}" @click="active(k)">
          {{k}}
        </div>
      </div>
      <div class="right">
        <div class="img" v-if="setting['头像']">
          <div class="avatar">
            <div class="userhead" :style="{backgroundImage:preview?`url(${srcAvatar})`:`url(${user.avatar})`}"></div>
            <label class="btn" for="uploads">更换头像</label>
            <input type="file" id="uploads" style="display: none;" accept="image/png, image/jpeg, image/gif, image/jpg"
              @change="uploadImg($event, 'avatar')">
          </div>
          <div>
            <div style="width: 200px;height: 200px;">
            <vue-cropper
              ref="cropper"
              :img="tempAvatar"
              :outputSize="option.size"
              :outputType="option.outputType"
              :fixed="option.fixed"
              :fixedBox="option.fixedBox"
              :centerBox="option.centerBox"
              :autoCrop="option.autoCrop"
            ></vue-cropper>
            </div>
            <button class="btn" @click="view('avatar')">预览</button>
            <button class="btn" @click="save('avatar')">保存</button>
          </div>
        </div>

        <div class="blogbg" v-if="setting['博客背景']">
          <div class="bgimg" :style="{backgroundImage:srcBg?`url(${srcBg})`:`url(${user.userBg})`}"></div>
          <div>
            <label class="btn" for="uploads">上传图片</label>
            <input type="file" id="uploads" style="display: none;" accept="image/png, image/jpeg, image/gif, image/jpg"
              @change="uploadImg($event, 'bg')">
            <button class="btn" @click="save('bg')">保存</button>
          </div>
        </div>

        <div class="blogtop" v-if="setting['顶部背景']">
          <div class="topimg" :style="{backgroundImage:srcTop?`url(${srcTop})`:`url(${user.userTop})`}"></div>
          <div>
            <label class="btn" for="uploads">上传图片（底部对齐）</label>
            <input type="file" id="uploads" style="display: none;" accept="image/png, image/jpeg, image/gif, image/jpg"
              @change="uploadImg($event, 'top')">
            <button class="btn" @click="save('top')">保存</button>
          </div>
        </div>

        <div class="contact" v-if="setting['基本信息']">
          <div class="oneline" :key="-1">
            <span>昵称</span>
            <input type="text" v-model="user.userName">
          </div>
          <div class="oneline" v-for="(val,i) in contactList" :key="i">
            <span>{{toChinese[i]}}</span>
            <input type="text" v-model="contactList[i]">
          </div>
          <button class="btn" @click="saveContact">保存</button>
        </div>

        <div class="board" v-if="setting['公告栏']">
          <div class="top">公告栏（支持html标签）</div>
          <textarea v-model="user.notice"></textarea>
          <button class="btn" @click="saveNotice">保存</button>
        </div>

        <div class="apiboard" v-if="setting['图片接口']">
          <div class="apitop">博客框图片接口(空即默认)</div>
          <div class="apiimg" :style="{backgroundImage : user.api ? `url(${user.api})` : 'url(https://www.luckyclover.top/myapi/blog/getImage)'}"></div>
          <input v-model="user.api">
          <button class="btn" @click="saveApi">保存</button>
        </div>

        <div class="blogmng" v-if="setting['博客管理']">
          <div class="blogmngtop">博客列表</div>
          <table>
            <tr>
              <th>序号</th>
              <th>标题</th>
              <th>分类</th>
              <th>上传时间</th>
              <th>所有人可见</th>
              <th>下载博客</th>
            </tr>
            <tr v-for="(blog,index) in blogs" :key="blog.blogId">
              <td>{{index+1}}</td>
              <td>{{blog.title}}</td>
              <td>{{blog.category}}</td>
              <td>{{blog.uploadTime}}</td>
              <td><input type="checkbox" v-model="blog.visible"></td>
              <td><a :href="downUrl(blog)">下载</a></td>
            </tr>
          </table>
          <button class="btn" @click="saveMng">保存</button>
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
import {saveAvatar, saveBg, saveTop, saveContact, saveNotice, saveApi, saveVisible} from 'api/user'
import {BlogHost} from 'common/js/config'
export default {
  components: {
    VueCropper
  },
  props: {},
  data () {
    return {
      setting: {
        '头像': true,
        '博客背景': false,
        '顶部背景': false,
        '基本信息': false,
        '公告栏': false,
        '图片接口': false,
        '博客管理': false
      },
      option: {
        img: ``,
        size: 1,
        full: false,
        fixed: true,
        outputType: 'png',
        fixedBox: false,
        original: false,
        canMoveBox: true,
        autoCrop: true,
        centerBox: false
      },
      tempAvatar: '', // cropper图片blob地址
      tempBg: null, // blob对象
      tempTop: null,
      preview: false,
      srcAvatar: '', // 预览图片
      srcBg: '', // 预览地址
      srcTop: '',
      contactList: [],
      toChinese: [
        '邮箱',
        'QQ',
        'GitHub',
        'B站主页',
        '知乎主页'
      ]
    }
  },
  watch: {},
  computed: {
    ...mapGetters([
      'user',
      'blogs'
    ])
  },
  methods: {
    back () {
      this.$emit('back')
    },
    active (k) {
      for (let i in this.setting) {
        this.setting[i] = false
      }
      this.setting[k] = true
      this.preview = false
    },
    view (type) {
      this.finish(type)
      this.preview = true
    },
    uploadImg (e, type) {
      if (e.target.value === '') return
      let file = e.target.files[0]
      if (!/\.(gif|jpg|jpeg|png|bmp|GIF|JPG|PNG)$/.test(e.target.value)) {
        alert('图片类型必须是.gif,jpeg,jpg,png,bmp中的一种')
        return false
      }
      if (file.size >= 1048576) {
        alert('图片大小需小于1M')
        return
      }
      let reader = new FileReader()
      reader.onload = (e) => {
        let blob = new Blob([e.target.result])
        let data = window.URL.createObjectURL(blob)
        if (type === 'avatar') {
          this.tempAvatar = data
        } else if (type === 'bg') {
          this.srcBg = data
          this.tempBg = blob
        } else if (type === 'top') {
          this.srcTop = data
          this.tempTop = blob
        }
      }
      reader.readAsArrayBuffer(file)
    },
    finish (type) {
      this.$refs.cropper.getCropBlob((data) => {
        let img = window.URL.createObjectURL(data)
        if (type === 'avatar') this.srcAvatar = img
      })
    },
    save (type) {
      this.preview = false
      let formData = new FormData()
      if (type === 'bg') {
        let file = new File([this.tempBg], 'bg.jpg', {type: 'image/jpg'})
        formData.append('file', file)
        saveBg(this.user.id, formData).then((res) => {
          if (res.data.state) this.setUser(res.data.data)
          alert(res.data.msg)
        })
      } else if (type === 'top') {
        let file = new File([this.tempTop], 'top.jpg', {type: 'image/jpg'})
        formData.append('file', file)
        saveTop(this.user.id, formData).then((res) => {
          if (res.data.state) this.setUser(res.data.data)
          alert(res.data.msg)
        })
      } else if (type === 'avatar') {
        this.$refs.cropper.getCropBlob((data) => {
          let file = new File([data], 'awatar.png', {type: 'image/png'})
          formData.append('file', file)
          saveAvatar(this.user.id, formData).then((res) => {
            if (res.data.state) this.setUser(res.data.data)
            alert(res.data.msg)
          })
        })
      }
    },
    saveContact () {
      let json = {}
      let names = ['email', 'qq', 'github', 'bili', 'zhihu']
      for (let i = 0; i < this.contactList.length; i++) {
        if (this.contactList[i]) json[names[i]] = this.contactList[i]
      }
      saveContact(this.user.id, this.user.userName, json).then((res) => {
        if (res.data.state) this.setUser(res.data.data)
        alert(res.data.msg)
      })
    },
    saveNotice () {
      saveNotice(this.user.id, this.user).then((res) => {
        if (res.data.state) this.setUser(res.data.data)
        alert(res.data.msg)
      })
    },
    saveApi () {
      saveApi(this.user.id, this.user).then((res) => {
        if (res.data.state) this.setUser(res.data.data)
        alert(res.data.msg)
      })
    },
    saveMng () {
      let pros = []
      for (let blog of this.blogs) {
        pros.push(saveVisible(blog.blogId, blog.visible))
      }
      Promise.all(pros).then(() => {
        alert('博客信息保存成功')
      })
    },
    downUrl (blog) {
      return BlogHost + 'img/' + blog.userId + '/' + blog.blogFile
    },
    ...mapMutations({
      setUser: 'SET_USER'
    })
  },
  created () {
    this.tempAvatar = this.user.avatar
    this.contactList = [this.user.contact.email, this.user.contact.qq, this.user.contact.github, this.user.contact.bili, this.user.contact.zhihu]
  },
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
  z-index: 1501;
  // background-color: rgba(128, 128, 128, 0.479);
  .setting {
    position: absolute;
    height: 60%;
    width: 50%;
    top: 15%;
    left: 50%;
    transform: translate(-50%,0);
    border-radius: 12px;
    overflow: hidden;
    max-width: 1000px;
    min-width: 300px;
    min-height: 450px;
    display: flex;
    box-shadow: 0 0 30px 10px rgba(0, 0, 0, 0.4);
  }
  .left {
    user-select: none;
    width: 120px;
    height: 100%;
    background-color: rgb(206, 206, 206);
    padding: 8px;
    overflow: auto;
    font-size: 20px;
    .set {
      margin-bottom: 5px;
      height: 50px;
      width: 100%;
      background-color: rgb(255, 255, 255);
      border: gray 1px solid;
      text-align: center;
      line-height: 50px;
      transition: padding-right 0.4s ease;
      &:hover {
        background-color: rgb(146, 224, 166);
      }
    }
    &::-webkit-scrollbar {
      width: 0;
    }
    @media screen and (max-width:1200px){
      width: 60px;
      font-size: 14px;
    }
  }
  .right {
    overflow: auto;
    flex: 1;
    width: 100%;
    height: 100%;
    background-color: rgb(255, 255, 255);
    .img {
      height: 100%;
      display: flex;
      justify-content: space-around;
      align-items: center;
      text-align: center;
      min-width: 420px;
      .avatar {
        .userhead {
        width: 200px;
        height: 200px;
        border-radius: 50%;
        background-position: center center;
        background-size: cover;
        box-shadow:0 0 30px 10px rgba(148, 146, 146, 0.36);
        }
      }
    }
    .blogbg {
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      text-align: center;
      min-width: 420px;
      .bgimg {
      width: 400px;
      height: 400px;
      background-position: center center;
      background-size: contain;
      background-repeat: no-repeat;
      }
    }
    .blogtop {
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      text-align: center;
      min-width: 420px;
      .topimg {
      width: 800px;
      height: 100px;
      background-size: cover;
      background-repeat: no-repeat;
      background-position: center bottom;
      }
    }
    .contact {
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      min-width: 450px;
      .oneline {
        width: 420px;
        text-align: center;
        padding-left: 10px;
        font-size: 20px;
        margin-top: 20px;
        margin-bottom: 10px;
        line-height: 30px;
        height: 30px;
        background-color: #96d4a594;
        border-radius: 9px;
        box-shadow:0 0 3px 1px rgb(0, 0, 0);
        display: flex;
        overflow: hidden;
        position: relative;
        span {
          width: 80px;
        }
        input {
          padding-right: 5px;
          padding-left: 5px;
          height: 28px;
          margin-left: 10px;
          margin-top: 1px;
          border: none;
          outline:none;
          flex: 1;
          line-height: 25px;
          font-size: 20px;
        }
      }
    }
    .board {
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      text-align: center;
      min-width: 420px;
      .top {
        font-size: 30px;
        margin: 5px;
      }
      textarea {
        width: 80%;
        height: 50%;
        font-size: 20px;
        resize: none;
      }
    }
    .blogmng {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      text-align: center;
      min-width: 420px;
      .blogmngtop {
        font-size: 30px;
        margin: 5px;
      }
      table {
        text-align: left;
        margin: 0 auto;
        width:80%;
      }
      th {
        height:50px;
      }
    }
    .apiboard {
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      text-align: center;
      min-width: 420px;
      .apitop {
        font-size: 30px;
        margin: 5px;
      }
      .apiimg {
        width: 80%;
        padding-top: 32%;
        margin-bottom: 20px;
        background-position: center 36%;
        background-size: cover;
      }
      input {
        width: 80%;
        height: 30px;
        font-size: 25px;
        resize: none;
        padding-left: 5px;
      }
    }
    .btn {
      display: inline-block;
      line-height: 1;
      white-space: nowrap;
      cursor: pointer;
      background: #fff;
      border: 1px solid #c0ccda;
      color: #1f2d3d;
      text-align: center;
      box-sizing: border-box;
      outline: none;
      margin:20px 5px 0px 5px;
      padding: 9px 15px;
      font-size: 16px;
      border-radius: 4px;
      color: #fff;
      background-color: #50bfff;
      border-color: #50bfff;
      transition: all .2s ease;
      text-decoration: none;
      user-select: none;
    }
  }
  .close {
    position: absolute;
    width: 40px;
    height: 40px;
    right: 0;
    top: 0;
  }
}
</style>
