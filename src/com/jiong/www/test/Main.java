package com.jiong.www.test;

import com.jiong.www.view.TilitiliView;

public class Main {
    public static void main(String[] args) {
        //bug 用list输入多行文字，只要回车就不可以撤回上一行
        TilitiliView tilitiliView = new TilitiliView();
        int userId = tilitiliView.login();
        int eventId = tilitiliView.viewEvent("赵英俊留给世界最后的话：不要把我忘了，永别了");
        tilitiliView.cancelComment(userId,eventId);
    }
}
