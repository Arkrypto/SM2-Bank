# SM-Bank-Demo

一个网银系统 Demo，采用 UKey 和传统密码的双因素认证，防护登录和转账过程，所使用加密算法为 SGD_SM3_SM2，权限控制由 SpringSecurity 实现

后端主要改写了双因素的认证过程（即 SM3_SM2 的解密和认证）、时间戳服务器的调用

前端采用 Vue 模板 [PanJiaChen/vue-admin-template: a vue2.0 minimal admin template (github.com)](https://github.com/PanJiaChen/vue-admin-template)，对登录、转账过程的 SM2 加密进行改写

前端 UKEY 加密这里有问题：

1. 我在两个地方需要用到 UKEY 加密，一个是登陆的时候，一个是转账的时候，显然转账这一操作在登录之后

2. 由于前端的 API 调用采用 WebSocket 异步回调函数的方式执行，所以我完整的加密过程是这样的

   1. 连接 WebSocket：若失败，返回 false；若成功，调用检查插件的回调
   2. 检查插件：若不通过，返回 false；若通过，调用获取密钥信息的回调
   3. 获取密钥信息：若不通过，返回 false；若通过，调用加密的回调
   4. 加密：失败或成功

   也就是说，这个过程是一个连贯、整体的，另外，每一次页面刷新会导致 WebSocket 的重连，为了保证刷新不影响用户操作，我必须在每一次载入页面的时候（即在钩子函数中）完整的执行上述过程

3. 但是，在登录时使用的 WebSocket 连接是一个长连接，而在第二步的第一小步，即连接 WebSocket，若已有连接，这一步是会报错返回 false 的，也就是说，登录到转账如果是一个连贯的流程，在转账页面将无法正常加密导致转账失败。但同时，上面也说了，为了避免用户刷新导致页面 bug，我必须把他作为一个完整的过程在钩子函数中执行


我想到了三种处理方案

1. 在登陆成功后，我直接将密钥信息存在用户本地 session，这样转账就不需要再一次读取 UKEY 了，但显然这样很不安全（这意味着用户只需要在登陆的时候插入 UKEY，而转账不进行 UKEY 的检测，并且恶意用户可以在浏览器的 localStorge 中找到部分密钥信息）
2. 在转账的时候进行判定，若 WebSocket 已连接并导致了报错，那么在这个异常处理中，直接进行“检查插件、获取密钥信息、加密”的操作（因为 WebSocket 已经连接了，这样的操作肯定是被允许的）
3. 在登录成功后，我手动关闭 WebSocket 连接，这样在转账页面的时候，他必定会重新连接 WebSocket，从而重新获取完整的密钥信息，坏处是增加了计算量

唉，太怪了，我肯定首选第二个方案，然后在这里做了处理，若已连接，则直接调用插件检查的函数并进行后续的加密

```js
envCheck(){
    if(ntlsUtil.wsObj){
        this.check_plugin_exist();
    } else {
        ntlsUtil.websocketInit(this.check_plugin_exist, null, this.check_plugin_exist);
    }
},
```

但还是有问题，不明原因，为了交付（表面上看起来没问题），只能临时采用第一种解决方案了

- 登陆的时候把`keyindex`和`containerID`存入 localStorage
- 转账的时候如果初始化参数为空（只有一种可能失败，就是 WebSocket 已连接，直接进行读取但没读到），直接从 localStorage 中读，进行加密

TMD，是哪个脑瘫想出来的用 WebSocket 交互做前端接口，真有他的

回头再看，第二种方案有不明原因的问题，第一种方案有无法容忍的安全问题，还是采用第三种方案比较好，于是改成了第三种

在登陆成功时，断开 WebSocket 连接

```js
doLogin(message){
    if(!message || !message.data){
        alert("签名失败, 请检查UKey是否插入或PIN码是否正确, 或PIN码是否被锁定");
        this.loading = false;
        return false;
    }
    // console.log("签名获取成功: " + message.data);
    console.log("签名所用证书为: " + this.cert  + "\n前端签名原文为" + this.encodeBase64(this.loginForm.iniData) + "\n前端签名所得密文为: " + message.data)
    this.loginForm.signature = message.data;

    this.$store.dispatch('user/login', this.loginForm).then(() => {
        // localStorage.setItem("pin", this.loginForm.key);
        // localStorage.setItem("keyindex", this.keyindex);
        // localStorage.setItem("container", this.container);
        
        // 断开连接
        ntlsUtil.close();
        this.$router.push({ path: this.redirect || '/' })

        this.loading = false
    }).catch(() => {
        this.loading = false
    })
},
```

但正确性有待验证，反正我交付的是第二版，第三版是为了说服我自己（补药写屎山啊）
