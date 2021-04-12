package com.jiong.www.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Mono
 */
public class Md5Utils {

    public  String toMD5(String password) {
        String securePassword = null;
        //加密的密码
        try {
            //md5实现类实例化
            byte[] ret = MessageDigest.getInstance("md5").digest(password.getBytes());
            //将获取的byte数组值转为16进制的字符串
            String md5Code = new BigInteger(1, ret).toString(16);
            //获取的字符串不足32位的用“0”补齐
            for (int i = 0; i < 32 - md5Code.length(); i++) {
                md5Code = "0" + md5Code;
            }
            securePassword = md5Code;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return securePassword;
    }
}
