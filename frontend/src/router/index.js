import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '网上银行', icon: 'bank' }
    }]
  },
  { path: '/information',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'index',
        name: 'Information',
        component: () => import('@/views/information/index'),
        meta: { title: '个人信息', icon: 'form' }
      }
    ]
  }

]

export const asyncRoutes = [
  { path: '/statement',
    component: Layout,
    children: [
      { path: 'index',
        name: 'Statement',
        component: () => import('@/views/statement/index'),
        meta: { title: '交易明细', icon: 'statement', roles: [0] }
      }
    ]
  },
  { path: '/admin/statement',
    component: Layout,
    children: [
      { path: 'index',
        name: 'StatementAdmin',
        component: () => import('@/views/statement-admin/index'),
        meta: { title: '交易明细', icon: 'statement', roles: [1] }
      }
    ]
  },
  { path: '/transaction',
    component: Layout,
    children: [
      { path: 'index',
        name: 'Transaction',
        component: () => import('@/views/transaction/index'),
        meta: { title: '转账', icon: 'transfer', roles: [0] }
      }
    ]
  },
  { path: '/log',
    component: Layout,
    children: [
      { path: 'index',
        name: 'Log',
        component: () => import('@/views/log/index'),
        meta: { title: '日志', icon: 'log', roles: [1] }
      }
    ]
  },
  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://e.mybank.cn/demo/index.htm#/',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
