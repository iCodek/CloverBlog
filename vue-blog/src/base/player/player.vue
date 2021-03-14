<template>
  <div class="player">
    <transition name="fade-in">
      <div class="record" v-if="nowplay">
        <div class="bgvideo">
          <video muted  src="../../assets/bgvideo480.mp4" autoplay="autoplay" loop="loop"></video>
        </div>
        <img :class="playFlag" :src="nowplay.pic" width="110" height="110">
        <div class="iconplay" @click="togglePlay">
          <svg class="icon" aria-hidden="true"  v-show="!isPlay">
              <use xlink:href="#icon-bofang"></use>
          </svg>
          <svg class="icon" aria-hidden="true"  v-show="isPlay">
              <use xlink:href="#icon-zanting"></use>
          </svg>
        </div>
        <div class="rightpart">
          <div class="song_lyric_container">
            <div class="mask">
              <ul :class="ulClass" ref="ul" style="margin-top: 25px" v-show="!songLyric.uncollected"
              @mousedown.prevent="lyricTouchStart"
              @touchstart.prevent="lyricTouchStart">
                <li class="lyric" v-for="(lyr, index) in songLyric.lyricList" :key="index">
                  {{lyr}}
                </li>
              </ul>
            </div>
          </div>

          <div class="boxfoot">
            <span class="time">
              {{currentTime | format}} / {{duration | format}}
            </span>
            <div class="barwrap" ref="progressBar"
              @click="progressClick"
              @mousedown.prevent="progressTouchStart"
              @touchstart.prevent="progressTouchStart">
              <div class="progressbar">
                <div class="progress" :style="{width : barLength * 100 + '%'}">
                  <div class="progressdot"></div>
                </div>
              </div>
            </div>
            <div class="btns">
              <svg class="icon" aria-hidden="true" v-if="nowplay.islike" @click="tounlike">
                  <use xlink:href="#icon-xihuan"></use>
              </svg>
              <svg class="icon" aria-hidden="true" v-else @click="tolike">
                  <use xlink:href="#icon-buxihuan-copy-copy"></use>
              </svg>
              <div class="volume" @click="volumeClick" @mousedown.prevent="volumeMoveStart">
                <svg class="icon" aria-hidden="true" v-show="volume>0" @click.stop="tomute">
                    <use xlink:href="#icon-yinliang-copy"></use>
                </svg>
                <svg class="icon" aria-hidden="true" v-show="volume<=0" @click.stop="cancelmute">
                    <use xlink:href="#icon-mute-copy"></use>
                </svg>
                <div class="volume_wrap">
                  <div class="volume_bg" ref="vlmpgs">
                    <div class="volume_progress" :style="{height : volume * 100 + '%'}">
                      <div class="dot"></div>
                    </div>
                  </div>
                </div>
              </div>
              <svg class="icon" aria-hidden="true" v-show="spread" @click="shouqi">
                  <use xlink:href="#icon-bofang1"></use>
              </svg>
              <svg class="icon" aria-hidden="true" v-show="!spread" @click="zhankai">
                  <use xlink:href="#icon-bofang1-copy"></use>
              </svg>
            </div>
          </div>
        </div>
      </div>
    </transition>
    <audio id="music-audio" ref="audio" @canplay="updateDuration" @timeupdate="updateTime" @ended="nextSong"></audio>
  </div>
</template>

<script>
import {playMode, ERR_OK} from 'common/js/config'
import {getSong, getLyric, getSongDetail} from 'api/song'
import {parseLyric, curLyricIndex} from 'common/js/lyric'
export default {
  components: {},
  props: {
    playlist: {
      type: Array,
      default () {
        return [{}]
      }
    }
  },
  data () {
    return {
      nowplay: undefined,
      isPlay: false,
      playmode: playMode.sequence,
      playUrl: undefined,
      songLyric: {move: false, uncollected: true},
      duration: 0,
      currentTime: 0,
      touch: {move: false},
      spread: true,
      lastvolume: 0.4,
      volume: 0.4,
      transitionOn: true
    }
  },
  watch: {
    playUrl (newUrl, oldUrl) {
      this.$refs.audio.src = newUrl
      if (newUrl === undefined) {
        this.duration = 0
        this.isPlay = false
        return
      }
      if (oldUrl === undefined && this.nowplay.source === 'Normal') {
        return
      }
      this.$refs.audio.play()
      this.isPlay = true
    },
    nowplay (newVal) {
      if (!newVal) return
      getSong(this.nowplay.id).then((res) => {
        if (res.status === ERR_OK) {
          if (res.data.data[0].url) {
            this.playUrl = res.data.data[0].url
          } else {
            this.playUrl = undefined
            alert('暂无版权哦～')
          }
        }
      })
      getLyric(this.nowplay.id).then((res) => {
        if (res.status === ERR_OK) {
          if (res.data.lrc) {
            let ly = parseLyric(res.data.lrc.lyric)
            this.songLyric.lyricList = ly[0]
            this.songLyric.lyricTime = ly[1]
            this.songLyric.uncollected = false
          } else {
            this.songLyric.uncollected = true
            this.songLyric.lyricList = []
            this.songLyric.lyricTime = []
          }
        }
      })
    },
    isPlay (newVal, oldVal) {
      this.$emit('play', newVal)
    },
    volume (newVal) {
      this.$refs.audio.volume = newVal
    }
  },
  computed: {
    playFlag () {
      return this.isPlay ? 'play' : 'play pause'
    },
    barLength () {
      return this.duration > 0 ? this.currentTime / this.duration : 0
    },
    ulClass () {
      return this.transitionOn ? '' : 'pause'
    }
  },
  methods: {
    _isMobile () {
      let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
      return flag
    },
    setPlay (item) {
      if ((item !== this.nowplay) && (this.nowplay === undefined || (item.id !== this.nowplay.id))) {
        this.nowplay = item
        if (!item.pic) {
          getSongDetail(item.id).then((res) => {
            if (res.status === ERR_OK) {
              this.nowplay.pic = res.data.songs[0].al.picUrl
            }
          })
        }
      } else {
        this.togglePlay()
      }
    },
    togglePlay () {
      if (this.playUrl === undefined) {
        alert('暂无版权哦～')
        return
      }
      this.isPlay = !this.isPlay
      if (this.isPlay) {
        this.$refs.audio.play()
      } else {
        this.$refs.audio.pause()
      }
    },
    updateTime (e) {
      if (this.touch.move) return
      this.currentTime = e.target.currentTime
      if (!this.songLyric.uncollected) {
        let lineIndex = curLyricIndex(this.songLyric.lyricTime, this.currentTime)
        if (this.songLyric.move) return
        this.$refs.ul.style.marginTop = lineIndex * -22 + 25 + 'px'
      }
    },
    shouqi (e) {
      this.spread = false
      this.$emit('callshouqi')
    },
    zhankai (e) {
      this.spread = true
      this.$emit('callzhankai')
    },
    nextSong () {
      this.$emit('nextSong', this.nowplay)
    },
    tolike (e) {
      this.nowplay.islike = true
      this.$emit('like', this.nowplay)
    },
    tounlike (e) {
      this.nowplay.islike = false
      this.$emit('unlike', this.nowplay)
    },
    tomute (e) {
      this.lastvolume = this.volume
      this.volume = 0
    },
    cancelmute (e) {
      this.volume = this.lastvolume
    },
    updateDuration () {
      this.duration = this.$refs.audio.duration
    },
    progressClick (e) {
      let rect = this.$refs.progressBar.getBoundingClientRect()
      // rect.left 元素距离左边的距离
      // e.pageX 点击距离左边的距离
      let offsetWidth = this._isMobile() ? e.touches[0].clientX - rect.left : e.clientX - rect.left
      // const barWidth = this.$refs.progressBar.clientWidth
      let barWidth = rect.right - rect.left
      let percent = offsetWidth / barWidth
      this.currentTime = this.duration * percent
      this.$refs.audio.currentTime = this.duration * percent
    },
    progressTouchStart (e) {
      this.touch.move = true
      let rect = this.$refs.progressBar.getBoundingClientRect()
      // const barWidth = this.$refs.progressBar.clientWidth
      let barWidth = rect.right - rect.left
      let offsetWidth = this._isMobile() ? e.touches[0].clientX - rect.left : e.clientX - rect.left
      let percent = offsetWidth / barWidth
      if (this._isMobile()) {
        this.currentTime = this.duration * percent
        this.$refs.audio.currentTime = this.duration * percent
      }
      this.touch.rect = rect
      let $this = this
      if (!this._isMobile()) {
        document.onmousemove = function (e) {
          $this.progressTouchMove(e)
        }
        document.onmouseup = function (e) {
          document.onmousemove = null
          document.onmouseup = null
          $this.progressTouchEnd(e)
        }
      } else {
        document.ontouchmove = function (e) {
          $this.progressTouchMove(e)
        }
        document.ontouchend = function (e) {
          document.ontouchmove = null
          document.ontouchend = null
          $this.progressTouchEnd(e)
        }
      }
    },
    progressTouchMove (e) {
      if (!this.touch.move) {
        return
      }
      let endX = this._isMobile() ? e.touches[0].clientX : e.clientX
      let percent = (endX - this.touch.rect.left) / (this.touch.rect.right - this.touch.rect.left)
      this.currentTime = this.duration * Math.min(Math.max(0, percent), 1)
    },
    progressTouchEnd (e) {
      this.touch.move = false
      this.$refs.audio.currentTime = this.currentTime
    },
    lyricTouchStart (e) {
      this.songLyric.move = true
      this.songLyric.startY = this._isMobile() ? e.touches[0].clientY : e.clientY
      this.songLyric.marginTop = this.$refs.ul.style.marginTop
      let dom = this.$refs.ul
      let height = window.getComputedStyle(dom).height
      this.songLyric.height = height
      let $this = this
      if (!this._isMobile()) {
        document.onmousemove = function (e) {
          $this.lyricTouchMove(e)
        }
        document.onmouseup = function (e) {
          document.onmousemove = null
          document.onmouseup = null
          $this.lyricTouchEnd()
        }
      } else {
        document.ontouchmove = function (e) {
          $this.lyricTouchMove(e)
        }
        document.ontouchend = function (e) {
          document.ontouchmove = null
          document.ontouchend = null
          $this.lyricTouchEnd()
        }
      }
    },
    lyricTouchMove (e) {
      if (!this.songLyric.move) return
      let endY = this._isMobile() ? e.touches[0].clientY : e.clientY
      this.transitionOn = false
      let top = Math.min(parseInt(this.songLyric.marginTop) + endY - this.songLyric.startY, 25)
      let bottom = Math.max(-parseInt(this.songLyric.height) + 45, top)
      this.$refs.ul.style.marginTop = bottom + 'px'
    },
    lyricTouchEnd (e) {
      this.songLyric.move = false
      this.transitionOn = true
    },
    volumeClick (e) {
      let rect = this.$refs.vlmpgs.getBoundingClientRect()
      let offsetHeight = rect.bottom - e.clientY
      let barHeight = rect.bottom - rect.top
      this.volume = Math.min(Math.max(offsetHeight / barHeight, 0), 1)
    },
    volumeMoveStart (e) {
      let rect = this.$refs.vlmpgs.getBoundingClientRect()
      let $this = this
      document.onmousemove = function (e) {
        let offsetHeight = rect.bottom - e.clientY
        let barHeight = rect.bottom - rect.top

        $this.volume = Math.min(Math.max(offsetHeight / barHeight, 0), 1)
      }
      document.onmouseup = function (e) {
        document.onmousemove = null
        document.onmouseup = null
      }
    }
  },
  created () {},
  mounted () {
    this.nowplay = this.playlist[0]
  }
}
</script>
<style  lang="scss" scoped>
.player{
    user-select: none;
    .record {
    max-height: 114px;
    margin: 0px auto;
    margin-bottom: 5px;
    position: relative;
    padding: 0;
    overflow:hidden;
    width: 96%;
    border-radius: 10px;
    background-color: rgb(50, 81, 145);
    display: flex;
    align-items: flex-end;
    &:hover > .iconplay .icon:last-child{
      opacity: 0.8;
    }
    .bgvideo {
      bottom: -4px;
      position: absolute;
      video {
        width: 100%;
      }
    }
    img {
      vertical-align:top;
      margin: 2px;
      border-radius: 50%;
      &.play {
        animation: rotate 10s linear infinite;
      }
      &.pause {
        animation-play-state: paused;
      }
    }
     img[src=""],img:not([src]){
      opacity:0;
     }
    .iconplay {
      bottom: 37px;
      left: 37px;
      position: absolute;
      opacity: 0.8;
      cursor: pointer;
      .icon:last-child {
        opacity: 0;
      }
      &:hover {
        opacity: 1;
      }
    }
    .rightpart {
      flex: 1;
      display: flex;
      height: 100px;
      flex-direction: column;
      .song_lyric_container {
        // background-color: skyblue;
        text-align: center;
        flex: 1;
        overflow: hidden;
        margin: 2px;
        border-radius: 8px;
        position: relative;
        // mask-image:-webkit-gradient(linear,left top,left bottom,from(rgba(255,255,255,0)),to(rgba(255,255,255,0)),color-stop(0.2,rgba(255,255,255,1)),color-stop(0.8,rgba(255,255,255,1)));
        .mask {
          position: absolute;
          width: 100%;
          height: 100%;
          color: transparent;
          -webkit-background-clip: text;
          background-image: linear-gradient(0deg, rgba(121, 121, 121, 0.8) 29%, white 41%, white 58%, rgba(121, 121, 121, 0.8) 70%);;
        }
        .pause {
          transition: none;
        }
        ul {
          transition: all 0.6s ease;
          li {
            white-space: nowrap;
            list-style: none;
            cursor: pointer;
            line-height: 22px;
            font-size: 16px;
          }
        }
      }
    }
    .boxfoot {
      z-index: 0;
      display: flex;
      padding: 2px;
      flex-direction: row;
      align-items: center;
      margin-bottom: 0px;
      box-shadow: 0px 0px 10px 5px rgba(0, 0, 0, 0.2);
      background-color: rgba(0, 0, 0, 0.2);
      border-radius: 5px;
      .time {
        padding-left: 2px;
        padding-right: 2px;
        color: rgba(255, 255, 255, 1);
        border-radius: 2px;
        white-space: nowrap;
        position: absolute;
        bottom: 9px;
        font-size: 10px;
        width: 60px;
        @media screen and (max-width:1440px){
          display: none;
        }
      }
      .barwrap {
        flex: 1;
        margin-bottom: -15px;
        cursor: pointer;
        margin-right: 2px;
        &:hover > .progressbar > .progress > .progressdot {
            opacity: 1;
        }
        .progressbar {
          height: 3px;
          background-color: rgba(255, 255, 255, 0.8);
          border-radius: 5px;
          flex: 1;
          margin-top: 6px;
          margin-bottom: 10px;
          .progress {
            margin-top: 0px;
            width: 0%;
            height: 100%;
            border-radius: 5px;
            background:linear-gradient(90deg,#ff0000 0%,#ffb600 14%,#fff600 28%,#a5ff00 38%,#00ff37 58%,#00eeff 65%,#00a0fc 74%,#1055b1 82%,#9d0ec9 92%,#7700ff 100%);
            // background-color:#00ff24;
            // box-shadow:rgba(255,255,255,0.7)0 5px 5px inser,rgba(255,255,255,0.7)0 -5px 5px inset;
            position: relative;
            .progressdot {
              position: absolute;
              width: 7px;
              height: 7px;
              right: -3px;
              top: -2px;
              border-radius: 50%;
              background-color: rgb(255, 255, 255);
              opacity: 0;
            }
          }
        }
      }
      .btns {
        width: 77px;
        height: 10px;
        display: inline-block;
        height: 20px;
        position: relative;
        svg {
          cursor: pointer;
          font-size: 16px;
          margin-left: 5px;
          opacity: 0.85;
          &:hover {
            opacity: 1;
          }
          &:nth-child(n+3):nth-child(-n+4){
            font-size: 15px;
            margin-bottom: 1px;
          }
          &:nth-child(-n+2){
            font-size: 18px;
          }
        }
        .volume {
          position: relative;
          display: inline-block;
          height: 30px;
          &:hover > .volume_wrap {
            height: 54px;
          }
          .volume_wrap {
            overflow: hidden;
            height: 0px;
            width: 20px;
            position: absolute;
            bottom: 21px;
            left: 3px;
            transition: all 0.6s;
            .volume_bg {
            position: relative;
            cursor: pointer;
            border-radius: 5px;
            width: 2px;
            height: 36px;
            margin: 10px auto;
            // border: 1px solid;
            background-color: white;
            .volume_progress {
              position: absolute;
              bottom: 0;
              width: 100%;
              border-radius: 4px;
              background-color: greenyellow;
              .dot {
                position: relative;
                margin-top: -2px;
                margin-left: -2px;
                border-radius: 50%;
                width: 6px;
                height: 6px;
                background-color: white;
                }
              }
            }
          }
        }
      }
    }
  }
  @keyframes rotate {
    0% {
      transform: rotate(0);
    }
    100% {
      transform: rotate(360deg);
    }
  }
  .fade-in-enter-active {
    transition: all 0.6s ease;
  }
  .fade-in-enter {
    max-height: 0;
    opacity: 0;
  }
}
</style>
