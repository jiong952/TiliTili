package com.jiong.www.util;

/**
 * @author Mono
 */
public class StringUtils {
    static final char EMAIL_CHAR ='@';
    static final char EMAIL_MIDDLE ='.';
    static final String EMAIL_END ="com";
    static final int EMAIL_NUMBER =5;
    static final int NUM_COUNT =2;
    static final int ALL_COUNT =8;
    public boolean isEmail(String email){
        int judge =0;
        //判断是否有仅有一个@且不能在开头或结尾
        if(email.indexOf(EMAIL_CHAR) > 0 && email.indexOf(EMAIL_CHAR) == email.lastIndexOf(EMAIL_CHAR) && email.indexOf(EMAIL_CHAR) < email.length()-1) {
            judge++;
        }

        //判断"@"之后必须有"."
        if(email.indexOf(EMAIL_MIDDLE,email.indexOf(EMAIL_CHAR)) > email.indexOf(EMAIL_CHAR)+1 ) {
            judge++;
        }
        //判断"@"之前或之后不能紧跟"."
        if(email.indexOf(EMAIL_MIDDLE) < email.indexOf(EMAIL_CHAR)-1 || email.indexOf(EMAIL_MIDDLE) > email.indexOf(EMAIL_CHAR)+1 ) {
            judge++;
        }
        //@之前要有6个字符
        if(email.indexOf(EMAIL_CHAR) > EMAIL_NUMBER ) {
            judge++;
        }
        //结尾的格式
        if(email.endsWith(EMAIL_END)) {
            judge++;
        }
        return judge == 5;

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
        if (numCount >= NUM_COUNT && (charNumber+numCount) >= ALL_COUNT &&charNumber>0){
            flag = true;
        }
        return flag;
    }
    }

