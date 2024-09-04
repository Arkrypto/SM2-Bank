package com.xidian.bankdemo.util;

import org.apache.commons.lang3.StringUtils;

public class CodeUtils {

    public  static String idCardMark(String idCardNum)
    {
        String res ="";
        if(!StringUtils.isBlank(idCardNum))
        {
            StringBuilder stringBuilder = new StringBuilder(idCardNum);
            res+=stringBuilder.replace(6,14,"********").toString();
        }
        return res;

    }

    public  static String phoneNum(String phone)
    {
        String res ="";
        if(!StringUtils.isBlank(phone))
        {
            StringBuilder stringBuilder = new StringBuilder(phone);
            res+=stringBuilder.replace(3,7,"****").toString();
        }
        return res;
    }

    public static void main(String[] args) {

        String s = CodeUtils.idCardMark("111111111111111111111");
        System.out.println(s);
    }
}
