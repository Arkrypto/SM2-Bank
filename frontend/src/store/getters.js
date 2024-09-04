import mToken from "@/utils/mToken"

const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  id: state => state.user.id,
  name: state => state.user.name,
  role: state => state.user.role,
  gender: state => state.user.gender,
  age: state => state.user.age,
  email: state => state.user.email,
  avatar: state => state.user.avatar,
  permission_routes: state => state.permission.routes,
}
export default getters
