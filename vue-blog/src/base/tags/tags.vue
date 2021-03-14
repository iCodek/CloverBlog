<template>
  <div>
    <div class="tags">
    <!-- <transition-group name="cell" tag="div" class="tags"> -->
      <div :class="cls(tag.name)" v-for="(tag, index) in tags" :key="tag.name" :style="{backgroundColor : color[index]}" @click="filter(tag.name)">
        {{tag.name+' ('+tag.count+')'}}
      </div>
    <!-- </transition-group> -->
    </div>
  </div>
</template>

<script>
// import _ from 'lodash/lodash'
import {rainbowColor} from 'common/js/util'
import {mapGetters, mapMutations} from 'vuex'
export default {
  components: {},
  props: {},
  data () {
    return {
      tags: [],
      color: []
    }
  },
  watch: {
    blogs (newVal) {
      let objs = newVal.reduce((obj, item) => {
        if (!item.visible) return obj
        if ('全部' in obj) obj['全部']++
        else obj['全部'] = 1
        for (let tag of item.tags) {
          if (tag in obj) obj[tag]++
          else obj[tag] = 1
        }
        return obj
      }, {})
      let tags = []
      for (let tag in objs) {
        tags.push({'name': tag, 'count': objs[tag]})
      }
      this.tags = tags.sort((a, b) => b.count - a.count)
      this.rainBow()
    }
  },
  computed: {
    ...mapGetters([
      'blogs',
      'tagsOn',
      'user'
    ])
  },
  methods: {
    cls (t) {
      return this.tagsOn.includes(t) ? 'tag on' : 'tag'
    },
    rainBow () {
      this.color = rainbowColor(this.tags.length, 50, 255)
    },
    // shuffle () {
    //   this.tags = _.shuffle(this.tags)
    // },
    filter (tag) {
      if (this.$route.name !== 'index') {
        this.$router.push({
          path: '/' + this.user.id
        })
      }
      if (tag === '全部') {
        this.setTags([])
        return
      }
      let copy = this.tagsOn.slice(0, this.tagsOn.length)
      let index = this.tagsOn.indexOf(tag)
      if (index !== -1) {
        copy.splice(index, 1)
        this.setTags(copy)
      } else {
        copy.push(tag)
        this.setTags(copy)
      }
    },
    ...mapMutations({
      setTags: 'SET_TAGS'
    })
  },
  created () {},
  mounted () {}
}
</script>

<style  lang="scss" scoped>
.tags{
  position: relative;
  // height: 420px;
  height: auto;
  background-color: var(--color);
  border-radius: 10px;
  margin-top: 10px;
  padding-left: 8px;
  .tag {
    font-size: 15px;
    display: inline-block;
    margin: 4px;
    padding: 1px 8px;
    box-sizing: border-box;
    border: 1px solid rgba(134, 219, 131, 0.699);
    opacity: 0.7;
    position: relative;
    &:hover {
      opacity: 1;
    }
  }
  .on::before {
    content: "";
    position: absolute;
    left: -3px;
    top: -3px;
    width: calc(100% + 4px);
    height: calc(100% + 4px);
    border: 1px solid rgb(255, 0, 0);
    border-radius: 5px;
  }
}
// .cell-move {
//   transition: transform 1s;
// }
</style>
