# SM-Bank-Demo

一个网银系统 Demo，采用 UKey 和传统密码的双因素认证，防护登录和转账过程，所使用加密算法为 SGD_SM3_SM2，权限控制由 SpringSecurity 实现

后端主要改写了双因素的认证过程（即 SM3_SM2 的解密和认证）、时间戳服务器的调用

前端采用 Vue 模板 [PanJiaChen/vue-admin-template: a vue2.0 minimal admin template (github.com)](https://github.com/PanJiaChen/vue-admin-template)，对登录、转账过程的 SM2 加密进行改写

- WebSocket 对 UKey 的读取有点问题，这里前端采用的异步回调函数非常不熟悉

