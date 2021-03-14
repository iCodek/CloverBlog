<template>
  <div class="sliderwrap">
    <div class="slider" ref="slider">
      <div class="process" :style="{width}"></div>
      <div class="thunk" ref="trunk" :style="{left}">
        <div :class="blockClass"></div>
        <div class="tips" ref="tips">
          <span>{{parseInt(scale*100)}}</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
/*
 * min 进度条最小值
 * max 进度条最大值
 * v-model 对当前值进行双向绑定实时显示拖拽进度
 * */
export default {
  props: ['min', 'max', 'value'],
  data () {
    return {
      slider: null, // 滚动条DOM元素
      thunk: null, // 拖拽DOM元素
      per: this.value, // 当前值
      blockClass: 'block play'
    }
  },
  // 渲染到页面的时候
  mounted () {
    this.slider = this.$refs.slider
    this.thunk = this.$refs.trunk
    var _this = this
    if (!this._isMobile()) {
      this.thunk.onmousedown = function (e) {
        _this.$refs.tips.style.opacity = 1
        _this.blockClass = 'block'
        var width = parseInt(_this.width) / 100 * _this.slider.offsetWidth
        var disX = e.clientX
        document.onmousemove = function (e) {
          // value, left, width
          // 当value变化的时候，会通过计算属性修改left，width
          // 拖拽的时候获取的新width
          var newWidth = e.clientX - disX + width
          // 拖拽的时候得到新的百分比
          var scale = newWidth / _this.slider.offsetWidth
          _this.per = Math.ceil((_this.max - _this.min) * scale + _this.min)
          _this.per = Math.max(_this.per, _this.min)
          _this.per = Math.min(_this.per, _this.max)
          _this.$emit('input', _this.per)
        }
        document.onmouseup = function () {
          _this.$refs.tips.style.opacity = 0
          document.onmousemove = document.onmouseup = null
        }
        return false
      }
    } else {
      this.thunk.ontouchstart = function (e) {
        _this.$refs.tips.style.opacity = 1
        _this.blockClass = 'block'
        var width = parseInt(_this.width) / 100 * _this.slider.offsetWidth
        var disX = e.touches[0].clientX
        document.ontouchmove = function (e) {
          // value, left, width
          // 当value变化的时候，会通过计算属性修改left，width
          // 拖拽的时候获取的新width
          var newWidth = e.touches[0].clientX - disX + width
          // 拖拽的时候得到新的百分比
          var scale = newWidth / _this.slider.offsetWidth
          _this.per = Math.ceil((_this.max - _this.min) * scale + _this.min)
          _this.per = Math.max(_this.per, _this.min)
          _this.per = Math.min(_this.per, _this.max)
          _this.$emit('input', _this.per)
        }
        document.ontouchend = function () {
          _this.$refs.tips.style.opacity = 0
          document.ontouchmove = document.ontouchend = null
        }
      }
    }
  },
  computed: {
    // 设置一个百分比，提供计算slider进度宽度和trunk的left值
    // 对应公式为  当前值-最小值/最大值-最小值 = slider进度width / slider总width
    // trunk left =  slider进度width + trunk宽度/2
    scale () {
      return (this.per - this.min) / (this.max - this.min)
    },
    width () {
      if (this.slider) {
        return 100 * this.scale + '%'
      } else {
        return 0 + '%'
      }
    },
    left () {
      if (this.slider) {
        return (
          parseInt(this.scale * 100) + '%'
        )
      } else {
        return 0 + '%'
      }
    }
  },
  methods: {
    _isMobile () {
      let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
      return flag
    }
  }
}
</script>
<style lang="scss" scoped>
.sliderwrap {
  padding: 10px 5px 10px 5px;
  position: absolute;
}
.slider {
  position: relative;
  margin: 20px;
  width: 0;
  height: 10px;
  background: #e4e7ed;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.5s ease;
}
.sliderwrap:hover > .slider{
  width: 200px;
}
.sliderwrap:hover > .slider > .process{
  max-width: 200px;
}
.slider .process {
  position: absolute;
  left: 0;
  top: 0;
  max-width: 0;
  height: 10px;
  border-radius: 5px;
  background: #409eff;
  transition: max-width 0.5s ease;
}
.slider .thunk {
  position: absolute;
  margin-left: -10px;
  top: -7px;
  width: 20px;
  height: 20px;
}
.slider .block {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  border: 2px solid #409eff;
  background: rgba(255, 255, 255, 1);
  transition: 0.2s all;
  opacity: 0.2;
}
.play {
  animation: twinkle 3.6s ease infinite;
}
.slider .tips {
  position: absolute;
  left: -7px;
  bottom: 30px;
  min-width: 15px;
  text-align: center;
  padding: 4px 8px;
  background: #000;
  border-radius: 5px;
  height: 24px;
  color: #fff;
  opacity: 0;
}
.sliderwrap:hover > .slider > .thunk >.block{
  opacity: 1 !important;
}
@keyframes twinkle {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}
</style>
