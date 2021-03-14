import Vue from 'vue'
import Router from 'vue-router'
import {loadToken, loadAuto} from 'common/js/cache'
const MainContent = (resolve) => {
  import('cpnts/main-content/main-content').then((module) => {
    resolve(module)
  })
}
const MdView = (resolve) => {
  import('cpnts/md-view/md-view').then((module) => {
    resolve(module)
  })
}
const Login = (resolve) => {
  import('cpnts/login/login').then((module) => {
    resolve(module)
  })
}
const BlogPage = (resolve) => {
  import('cpnts/blog-page/blog-page').then((module) => {
    resolve(module)
  })
}
Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/',
      redirect: to => {
        if (loadAuto() && loadToken()) {
          return '/' + JSON.parse(decodeURIComponent(escape(window.atob(loadToken().split('.')[1].replace(/-/g, '+').replace(/_/g, '/'))))).id
        }
        return '/0'
      }
    },
    {
      path: '/:id',
      component: BlogPage,
      children: [
        {
          path: '/',
          name: 'index',
          component: MainContent
        },
        {
          path: '/:id/:blogid',
          name: 'blog',
          component: MdView
        }
      ]
    },
    {
      path: '*',
      redirect: '/'
    }
  ]
})
