 import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'
import mToken from '@/utils/mToken'

const getDefaultState = () => {
  return {
    token: getToken(),
    id: '',
    name: '',
    role: '',
    gender: '',
    age: '',
    email: '',
    avatar: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_ID: (state, id) => {
    state.id = id
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_ROLE: (state, role) => {
    state.role = role
  },
  SET_EMAIL: (state, email) => {
    state.email = email
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_GENDER: (state, gender) => {
    state.gender = gender
  },
  SET_AGE: (state, age) => {
    state.age = age
  },
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password,code,signature,iniData } = userInfo
    return new Promise((resolve, reject) => {
      // alert(username)
      // alert(password)
      login({ username: username.trim(), password: password ,code: code,signature: signature,iniData:iniData}).then(response => {
        commit('SET_TOKEN', response.data)
        setToken(response.data)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        commit('SET_ID', data.id)
        commit('SET_NAME', data.username)
        commit('SET_ROLE', data.role)
        commit('SET_EMAIL', data.email)
        commit('SET_AVATAR', data.avatar)
        commit('SET_GENDER', data.gender)
        commit('SET_AGE', data.age)

        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

