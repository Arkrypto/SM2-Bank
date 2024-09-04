/******************************************************* 
* 
* 使用此JS脚本之前请先仔细阅读帮助文档! 
* 
* @author Fuly 
* @version 3.2.1 
* @date 2015/9/29 
* 
**********************************************************/



var split;
 
// Avoid running twice; that would break the `nativeSplit` reference
split = split || function (undef) {
 
    var nativeSplit = String.prototype.split,
        compliantExecNpcg = /()??/.exec("")[1] === undef, // NPCG: nonparticipating capturing group
        self;
 
    self = function (str, separator, limit) {
        // If `separator` is not a regex, use `nativeSplit`
        if (Object.prototype.toString.call(separator) !== "[object RegExp]") {
            return nativeSplit.call(str, separator, limit);
        }
        var output = [],
            flags = (separator.ignoreCase ? "i" : "") +
                    (separator.multiline  ? "m" : "") +
                    (separator.extended   ? "x" : "") + // Proposed for ES6
                    (separator.sticky     ? "y" : ""), // Firefox 3+
            lastLastIndex = 0,
            // Make `global` and avoid `lastIndex` issues by working with a copy
            separator = new RegExp(separator.source, flags + "g"),
            separator2, match, lastIndex, lastLength;
        str += ""; // Type-convert
        if (!compliantExecNpcg) {
            // Doesn't need flags gy, but they don't hurt
            separator2 = new RegExp("^" + separator.source + "$(?!\\s)", flags);
        }
        /* Values for `limit`, per the spec:
         * If undefined: 4294967295 // Math.pow(2, 32) - 1
         * If 0, Infinity, or NaN: 0
         * If positive number: limit = Math.floor(limit); if (limit > 4294967295) limit -= 4294967296;
         * If negative number: 4294967296 - Math.floor(Math.abs(limit))
         * If other: Type-convert, then use the above rules
         */
        limit = limit === undef ?
            -1 >>> 0 : // Math.pow(2, 32) - 1
            limit >>> 0; // ToUint32(limit)
        while (match = separator.exec(str)) {
            // `separator.lastIndex` is not reliable cross-browser
            lastIndex = match.index + match[0].length;
            if (lastIndex > lastLastIndex) {
                output.push(str.slice(lastLastIndex, match.index));
                // Fix browsers whose `exec` methods don't consistently return `undefined` for
                // nonparticipating capturing groups
                if (!compliantExecNpcg && match.length > 1) {
                    match[0].replace(separator2, function () {
                        for (var i = 1; i < arguments.length - 2; i++) {
                            if (arguments[i] === undef) {
                                match[i] = undef;
                            }
                        }
                    });
                }
                if (match.length > 1 && match.index < str.length) {
                    Array.prototype.push.apply(output, match.slice(1));
                }
                lastLength = match[0].length;
                lastLastIndex = lastIndex;
                if (output.length >= limit) {
                    break;
                }
            }
            if (separator.lastIndex === match.index) {
                separator.lastIndex++; // Avoid an infinite loop
            }
        }
        if (lastLastIndex === str.length) {
            if (lastLength || !separator.test("")) {
                output.push("");
            }
        } else {
            output.push(str.slice(lastLastIndex));
        }
        return output.length > limit ? output.slice(0, limit) : output;
    };
 
    // For convenience
    String.prototype.split = function (separator, limit) {
        return self(this, separator, limit);
    };
 
    return self;
 
}();


function mToken(obj){
	this.obj = obj;	
	
	this.SAR_OK										=	0;
	this.SAR_FALSE									= 	1;
	
	//分组加密算法标识
	this.SGD_SM1_ECB								=	0x00000101;
	this.SGD_SM1_CBC								=	0x00000102;
	this.SGD_SM1_CFB								=	0x00000104;
	this.SGD_SM1_OFB								=	0x00000108;
	this.SGD_SM1_MAC								=	0x00000110;
	this.SGD_SSF33_ECB								=	0x00000201;
	this.SGD_SSF33_CBC								=	0x00000202;
	this.SGD_SSF33_CFB								=	0x00000204;
	this.SGD_SSF33_OFB								=	0x00000208;
	this.SGD_SSF33_MAC								=	0x00000210;
	this.SGD_SM4_ECB								=	0x00000401;
	this.SGD_SM4_CBC								=	0x00000402;
	this.SGD_SM4_CFB								=	0x00000404;
	this.SGD_SM4_OFB								=	0x00000408;
	this.SGD_SM4_MAC								=	0x00000410;
	
	this.SGD_VENDOR_DEFINED							=	0x80000000;
	this.SGD_DES_ECB								= 	this.SGD_VENDOR_DEFINED  + 0x00000211
	this.SGD_DES_CBC								= 	this.SGD_VENDOR_DEFINED  + 0x00000212
	
	this.SGD_3DES168_ECB							= 	this.SGD_VENDOR_DEFINED  + 0x00000241
	this.SGD_3DES168_CBC							= 	this.SGD_VENDOR_DEFINED  + 0x00000242
	
	this.SGD_3DES112_ECB							= 	this.SGD_VENDOR_DEFINED  + 0x00000221
	this.SGD_3DES112_CBC							= 	this.SGD_VENDOR_DEFINED  + 0x00000222

	this.SGD_AES128_ECB								= 	this.SGD_VENDOR_DEFINED  + 0x00000111
	this.SGD_AES128_CBC								= 	this.SGD_VENDOR_DEFINED  + 0x00000112
	
	this.SGD_AES192_ECB								= 	this.SGD_VENDOR_DEFINED  + 0x00000121
	this.SGD_AES192_CBC								= 	this.SGD_VENDOR_DEFINED  + 0x00000122
	
	this.SGD_AES256_ECB								= 	this.SGD_VENDOR_DEFINED  + 0x00000141
	this.SGD_AES256_CBC								= 	this.SGD_VENDOR_DEFINED  + 0x00000142

	
	//非对称密码算法标识
	this.SGD_RSA									=	0x00010000;
	this.SGD_SM2_1									=	0x00020100; //ECC签名
	this.SGD_SM2_2									=	0x00020200; //ECC密钥交换
	this.SGD_SM2_3									=	0x00020400; //ECC加密
	
	//密码杂凑算法标识
	this.SGD_SM3									=	0x00000001;
	this.SGD_SHA1									=	0x00000002;
	this.SGD_SHA256									=	0x00000004;
	this.SGD_RAW									= 	0x00000080;
	this.SGD_MD5									= 	0x00000081;
	this.SGD_SHA384									= 	0x00000082;
	this.SGD_SHA512									= 	0x00000083;

	
	this.SGD_CERT_VERSION							=	0x00000001;
	this.SGD_CERT_SERIAL							= 	0x00000002;
	this.SGD_CERT_ISSUE								= 	0x00000005;
	this.SGD_CERT_VALID_TIME						= 	0x00000006;
	this.SGD_CERT_SUBJECT							= 	0x00000007;
	this.SGD_CERT_DER_PUBLIC_KEY					= 	0x00000008;
	this.SGD_CERT_DER_EXTENSIONS					= 	0x00000009;
	this.SGD_CERT_NOT_BEFORE						= 	0x00000010;
	this.SGD_CERT_ISSUER_CN							= 	0x00000021;
	this.SGD_CERT_ISSUER_O							= 	0x00000022;
	this.SGD_CERT_ISSUER_OU							= 	0x00000023;
	this.SGD_CERT_ISSUER_C							=	0x00000024;
	this.SGD_CERT_ISSUER_P							=	0x00000025;
	this.SGD_CERT_ISSUER_L							= 	0x00000026;
	this.SGD_CERT_ISSUER_S							=	0x00000027;
	this.SGD_CERT_ISSUER_EMAIL						=	0x00000028;

	this.SGD_CERT_SUBJECT_CN						= 	0x00000031;
	this.SGD_CERT_SUBJECT_O							= 	0x00000032;
	this.SGD_CERT_SUBJECT_OU						= 	0x00000033;
	this.SGD_CERT_SUBJECT_EMALL						= 	0x00000034;
	this.SGD_REMAIN_TIME							=	0x00000035;
	this.SGD_SIGNATURE_ALG							=	0x00000036;
	this.SGD_CERT_SUBJECT_C							=	0x00000037;
	this.SGD_CERT_SUBJECT_P							=	0x00000038;
	this.SGD_CERT_SUBJECT_L							=	0x00000039;
	this.SGD_CERT_SUBJECT_S							=	0x0000003A;

	this.SGD_CERT_CRL								= 	0x00000041;
                                            

	this.SGD_DEVICE_SORT							= 	0x00000201;
	this.SGD_DEVICE_TYPE							= 	0x00000202;
	this.SGD_DEVICE_NAME							= 	0x00000203;
	this.SGD_DEVICE_MANUFACTURER					= 	0x00000204;
	this.SGD_DEVICE_HARDWARE_VERSION				= 	0x00000205;
	this.SGD_DEVICE_SOFTWARE_VERSION				= 	0x00000206;
	this.SGD_DEVICE_STANDARD_VERSION				= 	0x00000207;
	this.SGD_DEVICE_SERIAL_NUMBER					= 	0x00000208;
	this.SGD_DEVICE_SUPPORT_SYM_ALG					= 	0x00000209;
	this.SGD_DEVICE_SUPPORT_ASYM_ALG				= 	0x0000020A;
	this.SGD_DEVICE_SUPPORT_HASH_ALG				= 	0x0000020B;
	this.SGD_DEVICE_SUPPORT_STORANGE_SPACE			= 	0x0000020C;
	this.SGD_DEVICE_SUPPORT_FREE_SAPCE				= 	0x0000020D;
	this.SGD_DEVICE_RUNTIME							= 	0x0000020E;
	this.SGD_DEVICE_USED_TIMES						= 	0x0000020F;
	this.SGD_DEVICE_LOCATION						= 	0x00000210;
	this.SGD_DEVICE_DESCRIPTION						= 	0x00000211;
	this.SGD_DEVICE_MANAGER_INFO					= 	0x00000212;
	this.SGD_DEVICE_MAX_DATA_SIZE					= 	0x00000213;
	
	this.TRUE										=	1;
	this.FALSE										=	0;
	
	this.GM3000PCSC									=	0;
	this.GM3000										=	1;
	this.K7											=	2;
	this.K5											=	3;
	this.TF											=	4;
	
	this.TYPE_UKEY									=	1; //普通UKEY
	this.TYPE_FPKEY									=	2; //指纹UKEY
	
	var g_mTokenPlugin = null;
	var g_deviceNames = null;
	
	
	this.SOF_GetLastError = function()
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_GetLastError();
	}
	
	function isIe()
	{
		return ("ActiveXObject" in window);
	}
	
	function isMobile()
	{
		var browser = {
			versions : function() {
				var u = navigator.userAgent, app = navigator.appVersion;
				return {//移动终端浏览器版本信息   
					trident : u.indexOf('Trident') > -1, //IE内核  
					presto : u.indexOf('Presto') > -1, //opera内核  
					webKit : u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核  
					gecko : u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核  
					mobile : !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端  
					ios : !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端  
					android : u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器  
					iPhone : u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器  
					iPad : u.indexOf('iPad') > -1, //是否iPad    
					webApp : u.indexOf('Safari') == -1
				//是否web应该程序，没有头部与底部  
				};
			}(),
			language : (navigator.browserLanguage || navigator.language).toLowerCase()
		}
		
		if ((browser.versions.mobile) && (browser.versions.ios || browser.versions.android || browser.versions.iPhone || browser.versions.iPad))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	this.SOF_LoadLibrary = function(type)
	{
		var ret;
		// console.log("g_mTokenPlugin："+g_mTokenPlugin);
		if(g_mTokenPlugin == null)
		{
			g_mTokenPlugin = new mTokenPlugin();
		}
		
		if(type == this.GM3000PCSC)
		{
			// console.log(isMobile());
			if(isMobile())
			{
				ret = g_mTokenPlugin.SOF_LoadLibrary("1", "mToken OTG", "com.longmai.security.plugin.driver.otg.OTGDriver");
			}
			else
			{
				ret = g_mTokenPlugin.SOF_LoadLibrary("mtoken_gm3000_pcsc.dll", "libgm3000_scard.1.0.so", "libgm3000_scard.1.0.dylib");
			}
		}
		else if(type == this.GM3000)
		{
			if(isMobile())
			{
				ret = g_mTokenPlugin.SOF_LoadLibrary("1", "mToken OTG", "com.longmai.security.plugin.driver.otg.OTGDriver");
			}
			else
			{
				ret = g_mTokenPlugin.SOF_LoadLibrary("mtoken_gm3000.dll", "libgm3000.1.0.so", "libgm3000.1.0.dylib");
			}
		}
		else if(type == this.K7)
		{
			ret = g_mTokenPlugin.SOF_LoadLibrary("mtoken_k7.dll", "libk7.1.0.so", "libk7.1.0.dylib");	
		}
		else if(type == this.K5)
		{
			if(isMobile())
			{
				ret = g_mTokenPlugin.SOF_LoadLibrary("2", "mToken K5 Bluetooth", "com.longmai.security.plugin.driver.ble.BLEDriver");
			}
			else
			{
				ret = g_mTokenPlugin.SOF_LoadLibrary("mtoken_k5.dll", "libk5.1.0.so", "libk5.1.0.dylib");
			}
		}
		else if(type == this.TF)
		{
			if(isMobile())
			{
				ret = g_mTokenPlugin.SOF_LoadLibrary("0", "mToken TF/SD Card", "com.longmai.security.plugin.driver.tf.TFDriver");
			}else
			{
				ret = g_mTokenPlugin.SOF_LoadLibrary("mtoken_tf.dll", "libtf.so", "libtf.dylib");;
			}
					
		}
		else
		{
			return -1;
		}
		
		if(ret != 0)
		{
			return -2;
		}
		return this.SAR_OK;
	};
	
	
	this.SOF_EnumDevice = function()
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		var array = g_mTokenPlugin.SOF_EnumDevice();
		if(array == null || array.length <= 0)
		{
			return null;
		}
		
		return array.split("||");
		
	};
	
	
	this.SOF_GetVersion = function()
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_GetVersion();
	};
	
	
	this.SOF_GetDeviceInstance = function(DeviceName, ApplicationName)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_GetDeviceInstance(DeviceName, ApplicationName);
	};
	
	this.SOF_ReleaseDeviceInstance = function()
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_ReleaseDeviceInstance()
	}
	
	
	this.SOF_GetUserList = function()
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		var array = g_mTokenPlugin.SOF_GetUserList();
		if(array == null || array.length <= 0)
			return null;
			
		var list = new Array();
		var user = array.split("&&&");
		var length = user.length;
		for(var i = 0; i < length; i++)
		{
			list[i] = user[i].split("||");
		}
		
		return list;
	};
	
	
	this.SOF_ExportUserCert = function(ContainerName, KeySpec)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_ExportUserCert(ContainerName, KeySpec);
	};
	
	
	this.SOF_GetDeviceInfo = function(Type)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_GetDeviceInfo(Type);
	};
	
	
	this.SOF_GetCertInfo = function(Base64EncodeCert, Type)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_GetCertInfo(Base64EncodeCert, Type);
	};
	
	this.SOF_GetCertInfoByOid = function(Base64EncodeCert, OID)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_GetCertInfoByOid(Base64EncodeCert, OID);
	}

	this.SOF_Login = function(PassWd)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_Login(PassWd);
	};
	
	
	this.SOF_LogOut = function()
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_LogOut();
	};
	
	
	this.SOF_GetPinRetryCount = function()
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_GetPinRetryCount();
	};
	
	
	this.SOF_ChangePassWd = function(OldPassWd, NewPassWd)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_ChangePassWd(OldPassWd, NewPassWd);
	};
	
	
	this.SOF_SetDigestMethod = function(DigestMethod)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_SetDigestMethod(DigestMethod);
	};
	
	
	this.SOF_SetUserID = function(UserID)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_SetUserID(UserID);
	};
	
	
	this.SOF_SetEncryptMethodAndIV = function(EncryptMethod, EncryptIV)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_SetEncryptMethodAndIV(EncryptMethod, EncryptIV);
	};
	
	
	this.SOF_EncryptFileToPKCS7 = function(Cert, InFile, OutFile, type)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_EncryptFileToPKCS7(Cert, InFile, OutFile, type);
	};
	
	
	this.SOF_SignFileToPKCS7 = function(ContainerName, InFile, OutFile)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_SignFileToPKCS7(ContainerName, InFile, OutFile);
	};
		
	this.SOF_VerifyFileToPKCS7 = function(strPkcs7Data, InFilePath)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_VerifyFileToPKCS7(strPkcs7Data, InFilePath);
	};
	
	this.SOF_DecryptFileToPKCS7 = function(ContainerName, keySpec, Pkcs7Data, InFile, OutFile, type)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_DecryptFileToPKCS7(ContainerName, keySpec, Pkcs7Data, InFile, OutFile, type);
	};
	
	this.SOF_DigestData = function(ContainerName, nData, InDataLen)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_DigestData(ContainerName, nData, InDataLen);
	};
	
	this.SOF_SignData = function(ContainerName, ulKeySpec, InData, InDataLen)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_SignData(ContainerName, ulKeySpec, InData, InDataLen);
	};
	//交互式签名
	this.SOF_SignDataInteractive = function(ContainerName, ulKeySpec, InData, InDataLen)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_SignDataInteractive(ContainerName, ulKeySpec, InData, InDataLen);
	}
	
	
	this.SOF_SignDataToPKCS7 = function(ContainerName, ulKeySpec, InData, ulDetached)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_SignDataToPKCS7(ContainerName, ulKeySpec, InData, ulDetached);
	};
	

	
	this.SOF_VerifyDataToPKCS7 = function(Base64EncodeCert, InData, SignedValue)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_VerifyDataToPKCS7(Base64EncodeCert, InData, SignedValue);
	};
	
	
	this.SOF_VerifySignedData = function(Base64EncodeCert, digestMethod, InData, SignedValue)
	{
		if(g_mTokenPlugin == null)
		{
			return -1;
		}
		
		return g_mTokenPlugin.SOF_VerifySignedData(Base64EncodeCert, digestMethod, InData, SignedValue);
	};
	
	
	this.SOF_EncryptDataPKCS7 = function(Base64EncodeCert, InData, InDataLen)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_EncryptDataPKCS7(Base64EncodeCert, InData, InDataLen);
	};
	
	
	this.SOF_DecryptDataPKCS7 = function(ContainerName, ulKeySpec, InData)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_DecryptDataPKCS7(ContainerName, ulKeySpec, InData);
	};
	
	
	this.SOF_GenRemoteUnblockRequest = function()
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_GenRemoteUnblockRequest();
	};
	
	
	this.SOF_GenResetpwdResponse = function(request, soPin, userPin)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_GenResetpwdResponse(request, soPin, userPin);
	};
	
	
	this.SOF_RemoteUnblockPIN = function(request)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_RemoteUnblockPIN(request);
	};
	
	
	this.SOF_SignDataToPKCS7 = function(ContainerName, ulKeySpec, InData, ulDetached)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_SignDataToPKCS7(ContainerName, ulKeySpec, InData, ulDetached);
	};


	this.SOF_VerifyDataToPKCS7 = function(strPkcs7Data, OriginalText, ulDetached)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_VerifyDataToPKCS7(strPkcs7Data, OriginalText, ulDetached);
	};
	
	//按expType导出的公钥，格式为国密规范指定的格式或DER格式
	this.SOF_ExportPubKey = function(containerName, cerType, expType)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_ExportPubKey(containerName, cerType, expType);
	}
	
	this.SOF_EncryptByPubKey = function(strPubKey, strInput, cerType)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_EncryptByPubKey(strPubKey, strInput, cerType);
	}
	
	this.SOF_DecryptByPrvKey = function(containerName, cerType, strAsymCipher)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_DecryptByPrvKey(containerName, cerType, strAsymCipher);
	}
	
	//RA客户端相关接口：P10及证书导入等
	this.SOF_CreateKeyPair = function(container, keySpec, asymAlg, keyLength)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_CreateKeyPair(container, keySpec, asymAlg, keyLength);
	}	
	
	this.SOF_EnumCertContiner = function()
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_EnumCertContiner();
	}
	
	this.SOF_FindContainer = function(certSN)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_FindContainer(certSN);
	}
	
	this.SOF_DeleteCert = function(certSN)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_DeleteCert(certSN);
	}
	
	this.SOF_DeleteContainer = function(certSN)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_DeleteContainer(certSN);
	}
	
	this.SOF_SignByCert = function(certSN, plain)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_SignByCert(certSN, plain);
	}
	
	this.SOF_VerifyByCert = function(certSN, plain, signature)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_VerifyByCert(certSN, plain, signature);
	}
	
	this.SOF_GenerateP10Request = function(container, dn, asymAlg, keySpec, keyLength)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_GenerateP10Request(container, dn, asymAlg, keySpec, keyLength);
	}
	
	this.SOF_ImportCert = function(container, cert, keySpec)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_ImportCert(container, cert, keySpec);
	}
	
	this.SOF_ImportCryptoCertAndKey = function(container, cert, nAsymAlg, EncryptedSessionKeyData, symAlg, EncryptedPrivateKeyData, mode)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_ImportCryptoCertAndKey(container, cert, nAsymAlg, EncryptedSessionKeyData, symAlg, EncryptedPrivateKeyData, mode);
	}
	
	this.SOF_VerifyByExtCert = function(cert, plain, signature, digestMethod)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_VerifyByExtCert(cert, plain, signature, digestMethod);
	}

	this.SOF_GenerateRandom = function(length)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_GenerateRandom(length);
	}	
	
	this.SOF_SymEncryptFile = function(sessionKey, srcfile, destfile, type)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_SymEncryptFile(sessionKey, srcfile, destfile, type);
	}
	
	this.SOF_SymDecryptFile = function(sessionKey, srcfile, destfile, type)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_SymDecryptFile(sessionKey, srcfile, destfile, type);
	}
	
	this.SOF_SymEncryptData = function(sessionKey, inData)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_SymEncryptData(sessionKey, inData);
	}
	
	this.SOF_SymDecryptData = function(sessionKey, inData)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_SymDecryptData(sessionKey, inData);
	}
	
	this.SOF_GenerateQRCode = function(qrcode_text)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_GenerateQRCode(qrcode_text);
	}

	
	this.SOF_VerifyPinMS = function (responseKey)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_VerifyPinMS(responseKey);
	}
	
	this.SOF_GetHardwareType = function ()
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_GetHardwareType();
	}
	
	this.SOF_VerifyFingerprint = function ()
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		return g_mTokenPlugin.SOF_VerifyFingerprint();
	}


	this.SOF_EnumFiles = function()
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		
		var array = g_mTokenPlugin.SOF_EnumFiles();
		if(array == null || array.length <= 0)
		{
			return null;
		}
		
		return array.split("||");
	}
	
	this.SOF_ReadFile = function(fileName, offset, length)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		return g_mTokenPlugin.SOF_ReadFile(fileName, Number(offset), Number(length));
	}
	
	this.SOF_WriteFile = function(fileName, offset, data)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		return g_mTokenPlugin.SOF_WriteFile(fileName, offset, data);
	}
	
	this.SOF_CreateFile = function(fileName, length, readRight, writeRight)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		return g_mTokenPlugin.SOF_CreateFile(fileName, length, readRight, writeRight);
	}
	
	this.SOF_DeleteFile = function(fileName)
	{
		if(g_mTokenPlugin == null)
		{
			return null;
		}
		return g_mTokenPlugin.SOF_DeleteFile(fileName);
	}
	
	
	}
	
	
	function mTokenPlugin(){
	
	var _curDevName = "";
	var _url = "http://127.0.0.1:51235/alpha";

	var _xmlhttp ;
	function AjaxIO(json) {
		if(_xmlhttp == null) {
			if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
				_xmlhttp = new XMLHttpRequest();
			} else {// code for IE6, IE5
				_xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
		}
		// if("https:" == document.location.protocol)
		// {
		// 	_url = "https://127.0.0.1:51245/alpha";
		// }
		_xmlhttp.open("POST", _url, false);
		_xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		_xmlhttp.send("json=" + json);
	}

	function GetHttpResult() {
		if(_xmlhttp.readyState == 4 && _xmlhttp.status == 200) {
			var obj = eval("(" + _xmlhttp.responseText + ")");
			return obj; 
		}
		else
		{
			return null;
		}
	}

	this.SOF_GetLastError = function()
	{
		var json = '{"function":"SOF_GetLastError"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	
	this.SOF_LoadLibrary = function(windows, linux, mac)
	{
		var json = '{"function":"SOF_LoadLibrary", "winDllName":"' + windows + '", "linuxSOName":"' + linux + '", "macDylibName":"' + mac + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}		
		return -2;
	};
	
	
	this.SOF_EnumDevice = function()
	{
		var json = '{"function":"SOF_EnumDevice"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
			
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.array;
		}
		
		return null;
	};
	
	
	this.SOF_GetVersion = function()
	{
		var json = '{"function":"SOF_GetVersion"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.version;
		}
		
		return -2;
	};
	
	
	this.SOF_GetDeviceInstance = function(DeviceName, ApplicationName)
	{
		var json = '{"function":"SOF_GetDeviceInstance", "deviceName":"' + DeviceName + '", "applicationName":"' + ApplicationName + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			if(ret.rtn == 0)
			{
				_curDevName = DeviceName;
			}
			return ret.rtn;
		}
		
		return -2;		
	};
	
	this.SOF_ReleaseDeviceInstance = function()
	{
		var json = '{"function":"SOF_ReleaseDeviceInstance", "deviceName":"' + _curDevName + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	
	this.SOF_GetUserList = function()
	{
		var json = '{"function":"SOF_GetUserList", "deviceName":"' + _curDevName + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.array;
		}
		
		return null;
	};
	
	
	this.SOF_ExportUserCert = function(ContainerName, KeySpec)
	{
		
		var json = '{"function":"SOF_ExportUserCert", "deviceName":"' + _curDevName + '", "containerName":"' + ContainerName + '", "keySpec":' + KeySpec + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.cert;
		}
		
		return null;
	};
	
	
	this.SOF_GetDeviceInfo = function(Type)
	{
		var json = '{"function":"SOF_GetDeviceInfo", "deviceName":"' + _curDevName + '", "type":' + Type + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.info;
		}
		
		return null;
		
	};
	
	
	this.SOF_GetCertInfo = function(Base64EncodeCert, Type)
	{
		var json = '{"function":"SOF_GetCertInfo", "base64EncodeCert":"' + Base64EncodeCert + '", "type":' + Type + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
	
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.info;
		}
		
		return null;
		
	};
	
	
	this.SOF_GetCertInfoByOid = function(Base64EncodeCert, OID)
	{
		var json = '{"function":"SOF_GetCertInfoByOid", "base64EncodeCert":"' + Base64EncodeCert + '", "oid":"' + OID + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
	
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.info;
		}
		
		return null;
		
	};
	
	
	
	this.SOF_Login = function(PassWd)
	{
		
		var json = '{"function":"SOF_Login", "deviceName":"' + _curDevName + '", "passWd":"' + PassWd + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;	
	};
	
	
	this.SOF_LogOut = function()
	{
		
		var json = '{"function":"SOF_LogOut", "deviceName":"' + _curDevName + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;	
	};
	
	
	this.SOF_GetPinRetryCount = function()
	{
		
		var json = '{"function":"SOF_GetPinRetryCount", "deviceName":"' + _curDevName + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.retryCount;
		}
		
		return -2;	
	};
	
	
	this.SOF_ChangePassWd = function(OldPassWd, NewPassWd)
	{
		
		var json = '{"function":"SOF_ChangePassWd", "deviceName":"' + _curDevName + '",  "oldUpin":"' + OldPassWd + '", "newUpin":"' + NewPassWd + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;	
	};
	
	
	this.SOF_SetDigestMethod = function(DigestMethod)
	{
		
		var json = '{"function":"SOF_SetDigestMethod","deviceName":"' + _curDevName + '", "digestMethod":' + DigestMethod + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;	
	};
	
	
	this.SOF_SetUserID = function(UserID)
	{
		
		var json = '{"function":"SOF_SetUserID","deviceName":"' + _curDevName + '", "userID":"' + UserID + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;	
	};
	
	
	this.SOF_SetEncryptMethodAndIV = function(EncryptMethod, EncryptIV)
	{
		
		var json = '{"function":"SOF_SetEncryptMethodAndIV","deviceName":"' + _curDevName + '", "encryptMethod":' + EncryptMethod + ', "encryptIV":"' + EncryptIV + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;	
	};
	
	
	this.SOF_EncryptFileToPKCS7 = function(Cert, InFile, OutFile, type)
	{
		
		var json = '{"function":"SOF_EncryptFileToPKCS7", "deviceName":"' + _curDevName + '", "cert":"' + Cert + '", "inFile":"' + InFile.replace(/\\/g, '\\\\') + '", "outFile":"' + OutFile.replace(/\\/g, '\\\\') + '", "type":' + type + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.envelopData;
		}
		
		return -2;	
	};
	
	
	this.SOF_SignFileToPKCS7 = function(ContainerName, KeySpec, InFile)
	{
		
		var json = '{"function":"SOF_SignFileToPKCS7", "deviceName":"' + _curDevName + '",  "containerName":"' + ContainerName + '", "KeySpec":' + KeySpec + ', "inFile":"' + InFile.replace(/\\/g, '\\\\') + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.signed;
		}
		
		return null;	
	};
	
	
	this.SOF_VerifyFileToPKCS7 = function(strPkcs7Data, InFilePath)
	{
		var json = '{"function":"SOF_VerifyFileToPKCS7", "deviceName":"' + _curDevName + '","strPkcs7Data":"' + strPkcs7Data + '", "inFile":"' + InFilePath.replace(/\\/g, '\\\\') + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;	
	};
	
	
	this.SOF_DecryptFileToPKCS7 = function(ContainerName, keySpec, Pkcs7Data, InFile, OutFile, type)
	{
		
		var json = '{"function":"SOF_DecryptFileToPKCS7", "deviceName":"' + _curDevName + '",  "containerName":"' + ContainerName + '", "keySpec":' + keySpec + ', "pkcs7Data":"' + Pkcs7Data + '", "inFile":"' + InFile.replace(/\\/g, '\\\\') + '", "outFile":"' + OutFile.replace(/\\/g, '\\\\') + '", "type":' + type + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;	
	};
	
	
	this.SOF_DigestData = function(ContainerName, InData, InDataLen)
	{
		
		var json = '{"function":"SOF_DigestData","deviceName":"' + _curDevName + '",  "containerName":"' + ContainerName + '", "inData":"' + InData + '", "inDataLen":' + InDataLen + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.digest;
		}
		
		return null;	
	};
	
	//交互式签名
	this.SOF_SignDataInteractive = function(ContainerName, ulKeySpec, inData, InDataLen)
	{
		var InDataBase64 =  inData; //对报文进行Base64编码
		var json = '{"function":"SOF_SignDataInteractive", "deviceName":"' + _curDevName + '",  "containerName":"' + ContainerName + '", "keySpec":' + ulKeySpec + ', "inData":"' + InDataBase64 + '", "inDataLen":' + InDataLen + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.signed;
		}
		
		return null;
	};
	
	this.SOF_SignData = function(ContainerName, ulKeySpec, InData, InDataLen)
	{
		
		var json = '{"function":"SOF_SignDataEx", "deviceName":"' + _curDevName + '", "containerName":"' + ContainerName + '", "keySpec":' + ulKeySpec + ', "inData":"' + InData + '", "inDataLen":' + InDataLen + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.signed;
		}
		
		return null;
	};
	
	
	this.SOF_VerifySignedData = function(Base64EncodeCert, digestMethod, InData, SignedValue)
	{
		var json = '{"function":"SOF_VerifySignedDataEx","deviceName":"' + _curDevName + '", "base64EncodeCert":"' + Base64EncodeCert + '", "digestMethod":' + digestMethod +', "inData":"' + InData + '", "signedValue":"' + SignedValue + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;	
	};
	
	
	this.SOF_EncryptDataPKCS7 = function(Base64EncodeCert, InData, InDataLen)
	{
		
		var json = '{"function":"SOF_EncryptDataPKCS7", "deviceName":"' + _curDevName + '", "base64EncodeCert":"' + Base64EncodeCert + '", "inData":"' + InData + '", "inDataLen":' + InDataLen + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.encrypedData;
		}
		
		return null;	
	};
	
	
	this.SOF_DecryptDataPKCS7 = function(ContainerName, ulKeySpec, InData)
	{
		
		var json = '{"function":"SOF_DecryptDataPKCS7", "deviceName":"' + _curDevName + '", "containerName":"' + ContainerName + '", "keySpec":' + ulKeySpec + ', "inData":"' + InData + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.decryptedData;
		}
		
		return null;	
	};
	
	
	this.SOF_GenRemoteUnblockRequest = function()
	{
		
		var json = '{"function":"SOF_GenRemoteUnblockRequest", "deviceName":"' + _curDevName + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.request;
		}
		
		return null;	
	};
	
	
	this.SOF_GenResetpwdResponse = function(request, soPin, userPin)
	{
		
		var json = '{"function":"SOF_GenResetpwdResponse", "deviceName":"' + _curDevName + '",  "request":"' + request + '", "soPin":"' + soPin + '", "userPin":"' + userPin + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.request;
		}
		
		return null;
	};
	
	
	this.SOF_RemoteUnblockPIN = function(request)
	{
		
		var json = '{"function":"SOF_RemoteUnblockPIN", "deviceName":"' + _curDevName + '", "request":"' + request + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	};
	
	
	this.SOF_SignDataToPKCS7 = function(ContainerName, ulKeySpec, InData, ulDetached)
	{
		
		var json = '{"function":"SOF_SignDataToPKCS7", "deviceName":"' + _curDevName + '", "containerName":"' + ContainerName + '", "keySpec":' + ulKeySpec + ', "inData":"' + InData + '",  "detached":' + ulDetached + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.pkcs7;
		}
		
		return null;
	};
	
	
	this.SOF_VerifyDataToPKCS7 = function(strPkcs7Data, OriginalText, ulDetached)
	{
		
		var json = '{"function":"SOF_VerifyDataToPKCS7", "deviceName":"' + _curDevName + '", "pkcs7":"' + strPkcs7Data + '", "original":"' + OriginalText + '", "detached":' + ulDetached + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	};

	//按expType导出的公钥，格式为国密规范指定的格式或DER格式或裸数据格式
	this.SOF_ExportPubKey = function(containerName, keySpec, expType) //expType=1: 国密格式； 2:DER; 3:RAW (SM2 public key ONLY: X|Y, X,Y各为32字节)
	{
		var json = '{"function":"SOF_ExportPubKeyEx","deviceName":"' + _curDevName + '",  "containerName":"' + containerName + '", "keySpec":' + keySpec + ', "expType":' + expType +'}';
		var retJSON = "";
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.pubKey;
		}
		
		return null;
	}
	
	//公钥加密
	this.SOF_EncryptByPubKey = function(strPubKey, strInput, cerType)
	{
		var json = '{"function":"SOF_EncryptByPubKey", "deviceName":"' + _curDevName + '", "pubKey":"' + strPubKey + '", "asymPlain":"' + strInput + '", "keySpec":' + cerType + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.asymCipher;
		}
		
		return null;
	}
	
	//私钥解密
	this.SOF_DecryptByPrvKey = function(containerName, cerType, strAsymCipher)
	{
		var json = '{"function":"SOF_DecryptByPrvKey", "deviceName":"' + _curDevName + '", "containerName":"' + containerName + '", "asymCipher":"' + strAsymCipher + '", "keySpec":' + cerType + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.asymPlain;
		}
		
		return null;
	}
	
	//RA客户端控件
	this.SOF_CreateKeyPair = function(container, keySpec, asymAlg, keyLength)
	{
		var json = '{"function":"SOF_CreateKeyPair","deviceName":"' + _curDevName + '",  "containerName":"' + container + '", "asymAlg":' + asymAlg + ', "keySpec":'+ keySpec + ',"keyLength":' + keyLength + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	
	this.SOF_EnumCertContiner = function()
	{
		var json = '{"function":"SOF_EnumCertContiner", "deviceName":"' + _curDevName + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.containerName;
		}
		
		return "";
	}
	
	this.SOF_FindContainer = function(certSN)
	{
		var json = '{"function":"SOF_FindContainer","deviceName":"' + _curDevName + '",  "certSN":"' + certSN + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.containerName;
		}
		
		return "";
	}
	
	this.SOF_DeleteCert = function(certSN)
	{
		var json = '{"function":"SOF_DeleteCert","deviceName":"' + _curDevName + '",  "certSN":"' + certSN + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	
	this.SOF_DeleteContainer = function(certSN)
	{
		var json = '{"function":"SOF_DeleteContainer", "deviceName":"' + _curDevName + '", "certSN":"' + certSN + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	
	this.SOF_SignByCert = function(certSN, plain)
	{
		var json = '{"function":"SOF_SignByCert", "deviceName":"' + _curDevName + '", "certSN":"' + certSN + '", "inData":"' + plain + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.signed;
		}
		
		return null;
	}

	this.SOF_VerifyByCert = function(certSN, plain, signature, digestMethod)
	{
		
		var json = '{"function":"SOF_VerifyByCert", "deviceName":"' + _curDevName + '", "certSN":"' + certSN + '","digestMethod":' + digestMethod +', "inData":"' + plain + '", "signed":"'+ signature + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
	
		return -2;
	}	
	
	this.SOF_VerifyByExtCert = function(cert, plain, signature, digestMethod)
	{
		var json = '{"function":"SOF_VerifyByExtCert","deviceName":"' + _curDevName + '",  "cert":"' + cert + '", "digestMethod":' + digestMethod +', "inData":"' + plain +'", "signed":"' + signature +'"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	
	this.SOF_GenerateP10Request = function(container, dn, asymAlg, keySpec, keyLength)
	{
		var json = '{"function":"SOF_GenerateP10Request", "deviceName":"' + _curDevName + '", "containerName":"' + container + '", "certDN":"' + dn + '", "asymAlg":' + asymAlg + ', "keySpec":'+ keySpec + ',"keyLength":' + keyLength + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.outData;
		}
		
		return null;
	}
	
	this.SOF_ImportCert = function(container, cert, keySpec)
	{
		var json = '{"function":"SOF_ImportCert", "deviceName":"' + _curDevName + '", "containerName":"' + container + '", "cert":"' + cert + '", "keySpec":'+ keySpec + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	
	this.SOF_ImportCryptoCertAndKey = function(container, cert, nAsymAlg, EncryptedSessionKeyData, symAlg, EncryptedPrivateKeyData, mode)
	{
		var json = '{"function":"SOF_ImportCryptoCertAndKey", "deviceName":"' + _curDevName + '", "containerName":"' + container + '", "cert":"' + cert + '", "asymAlg":'+ nAsymAlg + ', "sessionKey":"' + EncryptedSessionKeyData +'", "symAlg":"' + symAlg + '", "encrypedData":"' + EncryptedPrivateKeyData +', + "mode":'+mode+'}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	
	this.SOF_GenerateRandom = function(length)
	{
		var json = '{"function":"SOF_GenRandom", "deviceName":"' + _curDevName + '", "inDataLen":' + length + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.outData;
		}
		
		return -2;
	}
	
	this.SOF_SymEncryptFile = function(sessionKey, srcfile, destfile, type)
	{
		var json = '{"function":"SOF_SymEncryptFile", "deviceName":"' + _curDevName + '", "sessionKey":"' + sessionKey + '", "inFile":"' + srcfile.replace(/\\/g, '\\\\') + '", "outFile":"'+ destfile.replace(/\\/g, '\\\\') + '", "type":' + type + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	
	this.SOF_SymDecryptFile = function(sessionKey, srcfile, destfile, type)
	{
		var json = '{"function":"SOF_SymDecryptFile", "deviceName":"' + _curDevName + '", "sessionKey":"' + sessionKey + '", "inFile":"' + srcfile.replace(/\\/g, '\\\\') + '", "outFile":"'+ destfile.replace(/\\/g, '\\\\') + '", "type":' + type + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	
	this.SOF_SymEncryptData = function(sessionKey, inData)
	{
		var json = '{"function":"SOF_SymEncryptData", "deviceName":"' + _curDevName + '", "sessionKey":"' + sessionKey + '", "inData":"'+ inData + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.outData;
		}
		
		return null;
	}
	
	this.SOF_SymDecryptData = function(sessionKey, inData)
	{
		var json = '{"function":"SOF_SymDecryptData", "deviceName":"' + _curDevName + '", "sessionKey":"' + sessionKey + '", "inData":"'+ inData + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.outData;
		}
		
		return null;
	}
	
	/////////////////////////////////////////////////////////////////
	this.SOF_GenerateQRCode = function(qrcode_text_in)
	{
		var qrcode_text = qrcode_text_in.replace(/"/g, '\\"');
		var json = '{"module":"libQR","function":"SOF_GenerateQRCode", "inData":"' + qrcode_text + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.outData;
		}
		
		return null;
	}
	
	this.SOF_VerifyPinMS = function (responseKey)
	{
		var json = '{"function":"SOF_VerifyPinMS", "deviceName":"' + _curDevName + '", "passWd":"' + responseKey + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}
	/////////////////////////////////////////////////////////////////
	this.SOF_GetHardwareType = function()
	{
		var json = '{"function":"SOF_GetHardwareType", "deviceName":"' + _curDevName + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}

		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.type;
		}
		
		return -2;
	}
	
	this.SOF_VerifyFingerprint = function()
	{
		var json = '{"function":"SOF_VerifyFingerprint", "deviceName":"' + _curDevName + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
		
		return -2;
	}	
	
	this.SOF_EnumFiles = function()
	{
		var json = '{"function":"SOF_EnumFiles", "deviceName":"' + _curDevName + '"}';
	
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.array;
		}
		
		return -2;
	}
	
	this.SOF_ReadFile = function(fileName, offset, length)
	{
		var json = '{"function":"SOF_ReadFile", "deviceName":"' + _curDevName + '", "fileName":"' + fileName + '", "offset":' + offset + ',"length":' + length + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.outData;
		}
	}
	
	this.SOF_WriteFile = function(fileName, offset, data)
	{
		var json = '{"function":"SOF_WriteFile", "deviceName":"' + _curDevName + '", "fileName":"' + fileName + '", "offset":' + offset + ',"inData":"' + data + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
	}
	
	this.SOF_CreateFile = function(fileName, length, readRight, writeRight)
	{
		var json = '{"function":"SOF_CreateFile", "deviceName":"' + _curDevName + '","fileName":"' + fileName + '", "length" :' + length + ', "readRight":' + readRight + ',"writeRight":' + writeRight + '}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
	}
	
	this.SOF_DeleteFile = function(fileName)
	{
		var json = '{"function":"SOF_DeleteFile", "deviceName":"' + _curDevName + '","fileName":"' + fileName + '"}';
		try
		{
			AjaxIO(json);
		}
		catch (e)
		{
			return -3;
		}
		
		var ret = GetHttpResult();		
		if(ret) 
		{
			return ret.rtn;
		}
	}
	
}
export default new mToken("mTokenPlugin")