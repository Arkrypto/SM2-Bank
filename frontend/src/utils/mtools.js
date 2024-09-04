
import {_Base64encode,_Base64decode} from "./base64"
import mToken from "./mToken"



/**
 * 加载运行库
 */
var ret =  mToken.SOF_LoadLibrary(mToken.GM3000PCSC)
var ret1 = 0
if(ret !== 0)
{
    ret1= mToken.SOF_LoadLibrary(mToken.GM3000)
}

if(ret1 !== 0)
{
     var erro =  mToken.SOF_GetLastError()
     alert("加载运行库失败")
     console.log(erro)
}else{
    /**
     * 运行库加载成功后检测有无key
    */
    var device = mToken.SOF_EnumDevice()
    if(device === null){
         alert("请插入key")
    }
    else{

        /**
         * 检测到key 后再检测设备实例化是否成功
        */
        var devInstance =  mToken.SOF_GetDeviceInstance(device[0],"")
        if(devInstance != 0 )
        {
            var erro =  mToken.SOF_GetLastError()
            alert("设备实例化失败！"+erro)
        }else{                    
            var con = getContainer()
            if(con==null)
            {
                alert("请检查容器是否存在")
            }
        }
    }
}


 export function verifyKey(key)
 {
     var strRes = ""
    var ret =   mToken.SOF_Login(key)

    if(ret != 0)
    {	
        var lastErr = mToken.SOF_GetLastError();
        var retryCount = mToken.SOF_GetPinRetryCount();	
        alert(strRes + " 错误码:" + lastErr+"\n剩余登陆次数："+ retryCount);
    }

    return ret
 }
export function signData(inData)
{
    var container = mToken.SOF_EnumCertContiner()
    var userId = "12345678"
    var ret4 = mToken.SOF_SetDigestMethod(1)
    var ret5 = mToken.SOF_SetUserID(userId)
    var signed = mToken.SOF_SignData(container,1 , _Base64encode(inData), inData.length)
    console.log("signData: "+signed)
    return signed
}

export function verifySignature(inData,signd)
{   
    var container = mToken.SOF_EnumCertContiner()
    var cert  =  mToken.SOF_ExportUserCert(container,1);
    var ret =  mToken.SOF_VerifySignedData(cert, 1, _Base64encode(inData), signd)
    console.log("verifySignature: "+ret)
    return ret
}

export function getContainer(){
    var container =  mToken.SOF_EnumCertContiner()
    console.log("getContainer: "+container)
   return container ;
}

export function exportCert(container, type)
{

               //获取设备实例
  
    var cert  =  mToken.SOF_ExportUserCert(container,type);
    console.log("exportCert: "+ cert)
    return cert
}