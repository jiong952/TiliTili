package com.jiong.www.test;

import com.jiong.www.view.TilitiliView;
import com.jiong.www.view.Welcome;

import java.text.*;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws ParseException {
        //存进去：用StringBuilder按照格式拼接string，把string转化为java.sql.Date可以直接存进去
        //取出来：把java.sql.Date用Calendar类来获取年月日
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("2021");
        stringBuilder.append("-");
        stringBuilder.append("12");
        stringBuilder.append("-");
        stringBuilder.append("31");
        String s = stringBuilder.toString();
        java.sql.Date date1 = java.sql.Date.valueOf(s);
        System.out.println(date1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        System.out.println(calendar.get(Calendar.YEAR));


    }
}
