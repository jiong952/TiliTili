package com.jiong.www.util;

import java.text.SimpleDateFormat;

/**
 * @author Mono
 * 判断日期合法
 */
public  class DateUtils {
    public static boolean isDate(String date) {
        boolean judge = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            format.setLenient(false);
            //严格过滤日期，否则2-31会通过然后变成3-2
            format.parse(date);
        }catch (Exception e){
            judge = false;
        }
        return judge;
    }
}
