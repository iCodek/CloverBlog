<template>
  <div class="blog-page">
    <bg :rate="rate * 0.97" :url="user.userBg"></bg>
    <div class="scrollbox">
      <vue-scroll :ops="ops" ref="vs">
        <div>
          <m-header :url="user.userTop"></m-header>
          <center></center>
        </div>
        <bottom></bottom>
      </vue-scroll>
    </div>
  </div>
</template>

<script>
import MHeader from 'cpnts/m-header/m-header'
import bg from 'cpnts/bg/bg'
import center from 'cpnts/center/center'
import {mapGetters, mapMutations} from 'vuex'
import {getUserInfo, autoLogin} from 'api/user'
import {getBlogList} from 'api/blog'
import {loadToken, reset} from 'common/js/cache'
import Bottom from 'cpnts/bottom/bottom'
export default {
  components: {
    bg,
    MHeader,
    center,
    Bottom
  },
  data () {
    return {
      ops: {
        vuescroll: {},
        scrollPanel: {},
        rail: {
          size: '12px'
        },
        bar: {
          size: '12px'
        }
      },
      rate: 0
    }
  },
  watch: {
    $route (r) {
      if (r.name === 'index') {
        let id = this.$route.params.id
        let i = parseInt(id)
        if (i + '' === id) this.setUserInfo(id)
        else {
          this.$router.push({path: '/'})
        }
      }
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  methods: {
    Scroll () {
      let _this = this
      const v = _this.$refs.vs.getScrollProcess().v
      _this.rate = v
    },
    setUserInfo (id) {
      getUserInfo(id).then((res) => {
        if (res.data.state === true) {
          if (res.data.state) {
            this.setUser(res.data.data)
            document.title = res.data.data.userName + '的博客'
            let interval
            let rep = 0
            document.addEventListener('visibilitychange', function () {
              let isHidden = document.hidden
              if (isHidden) {
                interval = setInterval(function () {
                  rep = ++rep % 4
                  document.title = res.data.data.userName + '的博客' + ' 💤' + ' .'.repeat(rep)
                }, 1000)
              } else {
                interval && clearInterval(interval)
                document.title = res.data.data.userName + '的博客'
                rep = 0
              }
            })
          }
        } else alert(res.data.msg)
      })
      getBlogList(id).then((res) => {
        this.setBlogs(res.data.data)
      })
    },
    ...mapMutations({
      setUser: 'SET_USER',
      setBlogs: 'SET_BLOGS',
      setLogin: 'SET_LOGIN'
    })
  },
  created () {
    let id = this.$route.params.id
    // let blogid = this.$route.params.blogid
    let i = parseInt(id)
    if (i + '' === id) {
      this.setUserInfo(id)
      if (loadToken()) {
        try {
          if (JSON.parse(decodeURIComponent(escape(window.atob(loadToken().split('.')[1].replace(/-/g, '+').replace(/_/g, '/'))))).id === id) {
            autoLogin().then((res) => {
              // this.setUser(res.data.data)
              this.setLogin(true)
            })
          }
        } catch (e) {
          reset()
        }
        // if (blogid + '' === blogid) this.$router.push({name: 'blog', params: { id: id, blogid: blogid }})
      }
    } else {
      this.$router.push({path: '/'})
    }
  },
  mounted () {
    let docEle = document.querySelector('.scrollbox')
    docEle.addEventListener('scroll', this.Scroll, true)
  }
}
</script>

<style lang="scss">
  .blog-page {
    height: 100%;
    width: 100%;
    .scrollbox {
      overflow: auto;
      height: 100%;
    }
  }
</style>
