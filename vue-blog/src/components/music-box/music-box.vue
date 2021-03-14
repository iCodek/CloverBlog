<template>
  <div class="music-box">
    <div class="title">
      音乐盒
      <div class="searchbox" ref="searchBox">
        <input class="inbox" :placeholder="placeholder" v-model="query" type="text" @focus="onFocus" @blur="offFocus">
        <svg class="icon" aria-hidden="true" v-show="query!=''" @click="clear">
          <use xlink:href="#icon-qingchu"></use>
        </svg>
      </div>
      <svg class="icon search" aria-hidden="true" v-show="!isSearch" @click="search">
        <use xlink:href="#icon-sousuo"></use>
        <title>搜索歌曲</title>
      </svg>
      <svg class="icon back" aria-hidden="true" v-show="isSearch" @click="searchReturn">
        <use xlink:href="#icon-romantic-music"></use>
        <title>我的歌单</title>
      </svg>
    </div>
    <div class="line"></div>
    <player :playlist='songlist'  v-if="songlist.length || searchResults.length" ref='player' @play='play' @nextSong='nextPlay' @callshouqi="shouqilist" @callzhankai="zhankailist" @like="like" @unlike="unlike"></player>
    <transition
      appear
      appear-class="fade-appear-class"
      appear-active-class="fade-appear-active-class">
      <div :class="scrollClass">
        <vue-scroll>
          <div>
            <transition name="fade-in">
              <ul class="songlist" v-show="!isSearch && songlist.length">
                <transition-group name="list" tag="div">
                  <li class="song" v-for="(song, index) in songlist" :key="song.id" @click="toPlay(song,$event)" :title="song.name + ' by ' + song.singerName">
                    <svg :class="iconClass" aria-hidden="true" v-if="index==playindex">
                      <use xlink:href="#icon-cp-copy-copy"></use>
                    </svg>
                    <svg :class="iconClass" aria-hidden="true" v-if="index==playindex">
                      <use xlink:href="#icon-yinle"></use>
                    </svg>
                    <div class="wrap">
                      <div class="index">{{index + 1}}</div>
                      <div class="name">{{song.name}}</div>
                      <div class="duration">{{song.duration / 1000 | format}}</div>
                    </div>
                    <div class="line"></div>
                  </li>
                </transition-group>
              </ul>
            </transition>
            <transition name="fade-out">
              <ul class="songlist songlist2" v-show="isSearch && searchResults.length">
                <li class="song"  v-for="(song, index) in searchResults" :key="song.id" @click="toPlay(song,$event)" :title="song.name">
                  <svg :class="iconClass" aria-hidden="true" v-if="index==searchplayindex">
                    <use xlink:href="#icon-cp-copy-copy"></use>
                  </svg>
                  <svg :class="iconClass" aria-hidden="true" v-if="index==searchplayindex">
                    <use xlink:href="#icon-yinle"></use>
                  </svg>
                  <div class="wrap">
                    <div class="index">{{index + 1}}</div>
                    <div class="name">{{song.name}}</div>
                    <div class="singer">{{song.singerName}}</div>
                    <div class="duration">{{song.duration / 1000 | format}}</div>
                  </div>
                  <div class="line"></div>
                </li>
              </ul>
            </transition>
            <div class="empty" v-if="!isSearch && !songlist.length">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-gonggongtubiao_kongkongruye-copy"></use>
              </svg>
              <div class="text">
                音乐盒里空空如也
                <br>赶快去添加喜欢的歌曲吧
              </div>
            </div>
          </div>
        </vue-scroll>
      </div>
    </transition>
  </div>
</template>

<script>
import {ERR_OK} from 'common/js/config'
import {copySong} from 'common/js/song'
import {getSongDetail} from 'api/song'
import Player from 'base/player/player'
import {debounce} from 'common/js/util'
import {getSearchSongs} from 'api/search'
// import {saveFavorite, deleteFavorite, loadFavorite} from 'common/js/cache'
import {saveSong} from 'api/user'
import {mapGetters, mapMutations} from 'vuex'
export default {
  components: {
    Player
  },
  props: {},
  data () {
    return {
      songs: [],
      songlist: [],
      spread: true,
      playindex: 0,
      scrollClass: 'scrollbox',
      iconClass: 'icon',
      placeholder: '搜搜你想听的歌吧',
      query: '',
      isSearch: false,
      searchplayindex: -1,
      searchResults: [],
      loaded: false
    }
  },
  watch: {
    song (newVal) {
      this.songs = newVal.map((i) => parseInt(i))
      if (!this.loaded) {
        this.loaded = true
        this._getSongList()
      }
    },
    searchResults (val) {
      if (val) {
        this.searchplayindex = -1
        this.isSearch = true
      }
    }
  },
  computed: {
    ...mapGetters([
      'song',
      'user'
    ])
  },
  methods: {
    _getSongList () {
      let songlist = []
      let pros = []
      this.songs.forEach((id, index) => {
        pros.push(getSongDetail(id).then((res) => {
          if (res.status === ERR_OK) {
            songlist.push({'id': res.data.songs[0].id, 'name': res.data.songs[0].name, 'pic': res.data.songs[0].al.picUrl, 'index': index, 'duration': res.data.songs[0].dt, 'source': 'Normal', 'islike': true})
          }
        }))
      })
      Promise.all(pros).then(() => {
        songlist.sort((a, b) => a.index - b.index)
        this.songlist = songlist
      })
    },
    toPlay (item, e) {
      if (this.songs.includes(item.id)) item.islike = true
      else item.islike = false
      this.$refs.player.setPlay(item)
      if (item.source === 'Normal') {
        this.playindex = item.index
        this.searchplayindex = -1
      } else if (item.source === 'Search') {
        this.searchplayindex = item.index
        this.playindex = this.songs.indexOf(item.id)
      }
    },
    nextPlay (item) {
      if (item.source === 'Normal') {
        let nextIndex = item.index === this.songlist.length - 1 ? 0 : item.index + 1
        this.$refs.player.setPlay(this.songlist[nextIndex])
        this.playindex = nextIndex
        this.searchplayindex = -1
      } else if (item.source === 'Search') {
        let nextIndex = item.index === this.searchResults.length - 1 ? 0 : item.index + 1
        this.$refs.player.setPlay(this.searchResults[nextIndex])
        this.searchplayindex = nextIndex
        this.playindex = -1
      }
    },
    shouqilist () {
      this.spread = false
      this.scrollClass = 'scrollbox scrollboxhide'
    },
    zhankailist () {
      this.spread = true
      this.scrollClass = 'scrollbox'
    },
    play (isPlay) {
      if (isPlay) {
        this.iconClass = 'icon play'
      } else {
        this.iconClass = 'icon play pause'
      }
    },
    onFocus () {
      this.placeholder = ''
    },
    like (item) {
      if (this.songs.includes(item.id)) return
      let copy = copySong(item)
      copy.source = 'Normal'
      copy.index = this.songs.length
      this.songs.push(copy.id)
      this.songlist.push(copy)
      this.playindex = copy.index
      saveSong(this.user.id, this.songs).then((res) => {
        // this.setUser(res.data.data)
      })
      // saveFavorite(copy.id)
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
      saveSong(this.user.id, this.songs).then((res) => {
        // this.setUser(res.data.data)
      })
      // deleteFavorite(item.id)
    },
    offFocus () {
      if (!this.query) this.placeholder = '搜搜你想听的歌吧'
    },
    clear () {
      this.query = ''
    },
    search () {
      if (this.searchResults.length) this.isSearch = true
    },
    searchReturn () {
      this.isSearch = false
    },
    singerName (arr) {
      let name = []
      name = arr.map((item) => {
        return item.name
      })
      return name.join('/')
    },
    ...mapMutations({
      setUser: 'SET_USER'
    })
  },
  created () {},
  mounted () {
    this.$watch('query', debounce((newQuery) => {
      if (newQuery) {
        let searchResults = []
        getSearchSongs(newQuery, 0).then((res) => {
          if (res.status === ERR_OK) {
            let songs = res.data.result.songs
            if (songs.length > 0) {
              songs.forEach((song, index) => {
                searchResults.push({'id': song.id, 'name': song.name, 'pic': undefined, 'index': index, 'duration': song.duration, 'singerName': this.singerName(song.artists), 'source': 'Search', 'islike': false})
              })
              this.searchResults = searchResults
            }
          }
        })
      }
    }, 300))
  }
}
</script>
<style  lang="scss" scoped>
.music-box{
  position: relative;
  // height: 420px;
  height: auto;
  background-color: var(--color);
  margin-bottom: 20px;
  border-radius: 9px;
  display: flex;
  flex-direction: column;
  user-select: none;
  // align-items: center;
  .title {
    margin-left: 16px;
    margin-right: 10px;
    color: #000;
    padding-top: 10px;
    font-weight: 700;
    font-size: 22px;
    margin-bottom: 5px;
    display: flex;
    height: 32px;
    .searchbox {
      position: relative;
      margin-top: -4px;
      margin-left: 16px;
      border-radius: 8px;
      flex: 1;
      height: 36px;
      text-align: center;
      box-sizing: border-box;
      background-color: rgba($color: #caf0cf, $alpha: 0.8);
      &:hover > .inbox{
          border-radius: 6px;
        }
      .inbox {
        margin-top: 3px;
        width: 93%;
        height: 80%;
        border: none;
        outline:none;
        font-size: 16px;
        padding-left: 3px;
      }
      input::-webkit-input-placeholder {
        color: rgba(164, 196, 233, 0.6);
      }
      .icon {
        position: absolute;
        font-size: 25px;
        top: 6px;
        right: 7px;
        opacity: 0.2;
        &:hover {
          opacity: 0.8;
        }
      }
    }
    .search {
      margin-top: -4px;
      font-size: 36px;
    }
    .back {
      margin-top: -4px;
      font-size: 36px;
    }
  }
  .line {
    width: 92%;
    height: 2px;
    background-color: #000;
    margin: 0 auto 2px auto;
  }
  .scrollboxhide {
    height: 0 !important;
    margin-bottom: 0px !important;
  }
  .scrollbox {
    height: 276px;
    margin-bottom: 8px;
    transition: all 0.5s ease;
    .songlist {
      box-shadow:  0 0 4px 0 rgba(0,0,0,0.5);
      padding-left: 2px;
      padding-right: 2px;
      border-radius: 10px;
      margin: 10px;
      margin-top: 0px;
      margin-right: 14px;
      user-select: none;
      border:2px solid rgba($color: #000000, $alpha: 0.4);;
      li:first-child {
        margin-top: 2px;
        line-height: 34px;
        border-top-left-radius: 8px;
        border-top-right-radius: 8px;
      }
      li:last-child {
        margin-bottom: 2px;
        line-height: 34px;
        border-bottom-left-radius: 8px;
        border-bottom-right-radius: 8px;
      }
      .song {
        overflow: hidden;
        padding-left: 28px;
        line-height: 36px;
        font-size: 20px;
        list-style: none;
        cursor: pointer;
        position: relative;
        svg:first-child{
          position: absolute;
          font-size: 28px;
          left: -1px;
          top: 6px;
          &.play {
            animation: rotate 10s linear infinite;
          }
          &.pause {
            animation-play-state: paused;
          }
          }
        svg:nth-child(2){
          position: absolute;
          font-size: 28px;
          left: 13px;
          top: 1px;
          transform-origin: 14px 4px;
          transition: transform 0.4s;
          &.play {
            transform: rotate(22deg);
          }
          &.pause {
            transform: rotate(0deg);
          }
        }
        .wrap {
          // white-space: nowrap;
          width: 300px;
          overflow: hidden;
          display: flex;
          height: 36px;
          .index {
            text-align: center;
            width: 26px;
          }
          .name {
            flex: 1;
          }
          .singer {
            float: right;
            font-size: 16px;
            opacity: 0.6;
            line-height: 36px;
            margin: 1px;
          }
          .duration {
            float: right;
            margin: 2px;
            width: 28px;
            font-size: 14px;
            opacity: 0.3;
          }
        }
        .line {
          height: 1px;
          width: 98%;
          background-color: rgba($color: #000000, $alpha: 0.4);
        }
        &:hover {
          background-color: rgb(231, 231, 231);
        }
        & :hover > .duration {
          opacity: 1;
        }
      }
      .song:last-child .line{
        height: 0;
      }
    }
    .songlist2 {
      position: absolute;
      top: 0;
    }
    .empty {
      position: absolute;
      top: 0;
      width: 100%;
      height: 100%;
      opacity: var(--opacity);
      .icon {
        width: 100%;
        height: 78%;
        opacity: var(--opacity);
      }
      .text {
        color: #4367a8b0;
        line-height: 28px;
        font-size: 22px;
        text-align: center;
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
  .fade-out-enter-active {
    transition: all 0.4s ease;
  }
  .fade-out-leave-active {
    transition: all 0.4s ease;
  }
  .fade-out-enter, .fade-out-leave-to {
    transform: translateX(360px);
    opacity: 0;
  }
  .fade-in-enter-active {
    transition: all 0.4s ease;
  }
  .fade-in-leave-active {
    transition: all 0.4s ease;
  }
  .fade-in-enter, .fade-in-leave-to {
    transform: translateX(-360px);
    opacity: 0;
  }
  .fade-appear-class {
    transform: rotateY(60deg);
    opacity: 0;
  }
  .fade-appear-active-class {
    transition: all 0.6s cubic-bezier(1.0, 0.2, 0.6, 0);
  }
  .list-enter-active, .list-leave-active {
    transition: all 0.6s ease;
  }
  .list-enter, .list-leave-to
  {
    opacity: 0;
    transform: translateX(30px);
  }
}
</style>
