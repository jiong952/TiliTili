package com.jiong.www.test;

import com.jiong.www.po.User;
import com.jiong.www.service.TilitiliService;
import com.jiong.www.view.TilitiliView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TilitiliView tilitiliView = new TilitiliView();
        int login = tilitiliView.login();
        //tilitiliView.createEventGroup(login);
        //tilitiliView.deleteEventGroup(login);
        //tilitiliView.createEvent(login,1);
    }
}
