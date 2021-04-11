package com.jiong.www.util;

/**
 * @author Mono
 */
public class StringUtil {
    public boolean isEmail(String email){
        int k =0;
        //判断是否有仅有一个@且不能在开头或结尾
        if(email.indexOf("@") > 0 && email.indexOf('@') == email.lastIndexOf('@') && email.indexOf('@') < email.length()-1) {
            k++;
        }

        //判断"@"之后必须有"."且不能紧跟
        if(email.indexOf('.',email.indexOf('@')) > email.indexOf('@')+1 ) {
            k++;
        }
        //判断"@"之前或之后不能紧跟"."
        if(email.indexOf('.') < email.indexOf('@')-1 || email.indexOf('.') > email.indexOf('@')+1 ) {
            k++;
        }
        //@之前要有6个字符
        if(email.indexOf('@') > 5 ) {
            k++;
        }

        if(email.endsWith("com") || email.endsWith("org") || email.endsWith("cn") ||email.endsWith("net")) {
            k++;
        }
        return k == 5;

    }
    public boolean isPassword(String password){
        //要求一定要是8位以上的密码，且一定有且只有字母和数字
        char[] chars = password.toCharArray();
        boolean flag = false;
        int numCount = 0, charNumber = 0;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i]) || Character.isLetter(chars[i])) {
                if (Character.isDigit(chars[i])){
                    numCount++;
                    //数字的数目
                }
                else{
                    charNumber++;
                    //字母的数目
                }
            } else{
                break;
            }
        }
        if (numCount >= 2 && (charNumber+numCount) >= 8&&charNumber>0){
            flag = true;
        }
        return flag;
    }
    }

