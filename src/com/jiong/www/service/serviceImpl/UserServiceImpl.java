package com.jiong.www.service.serviceImpl;

import com.jiong.www.dao.dao.IUserDao;
import com.jiong.www.dao.daoImpl.UserDaoImpl;
import com.jiong.www.po.User;
import com.jiong.www.service.ServiceException;
import com.jiong.www.service.service.IUserService;
import com.jiong.www.util.ImageUtils;
import com.jiong.www.util.Md5Utils;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;


import static com.jiong.www.util.MyDsUtils.*;

/**
 * @author Mono
 */
public class UserServiceImpl implements IUserService {
    IUserDao iUserDao = new UserDaoImpl();
    /**放在类中，才能验证是不是同一个人*/
    @Override
    public int register(String loginName, String loginPassword,int roleId) {
        int row;
        //封装user对象
        User user = new User();
        user.setLoginName(loginName);
        user.setLoginPassword(loginPassword);
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            //注册，添加信息到用户表
            row = iUserDao.doRegister(conn,user);
            //把新注册的用户加入到用户角色表，默认新注册只能为吃瓜群众即1
            iUserDao.doInsertRole(conn,iUserDao.doQueryId(user.getLoginName()),roleId);
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ServiceException("数据回滚异常",e);
            }
            e.printStackTrace();
            throw new ServiceException("注册异常",e);
        }
        return row;
    }
    /**用于注册时验证该用户名是否存在*/
    @Override
    public int verifyUsername(String loginName){
        int row;
        row= iUserDao.verifyExist(loginName);
        return row;
        //0不存在，1存在
    }
    /**完善用户信息*/
    @Override
    public int perfectInformation(String userEmail, String userNickName, int userGender, String userDescription, int userId, Date userBirthday) {
        int row ;
        // 用于接收dao层的返回值
        //封装对象
        User user = new User();
        user.setUserEmail(userEmail);
        user.setUserNickname(userNickName);
        user.setUserGender(userGender);
        user.setUserDescription(userDescription);
        user.setUserBirthday(userBirthday);
        user.setUserId(userId);
        row= iUserDao.perfectInformation(user);
        //处理dao层的异常
        return row;
        //返回结果集
    }
    /**是否要记住密码*/
    @Override
    public void isRememberPassword(int isRememberPassword, int userId){
        User user = new User();
        user.setIsRememberPassword(isRememberPassword);
        user.setUserId(userId);
        iUserDao.isRememberPassword(user);
    }
    /**登录*/
    @Override
    public int login(String loginName, String loginPassword,int isRememberPassword){
        // 用于接收dao层的返回值
        int userId=0;
        //用用户名查密码和userId
        User userQuery = iUserDao.login(loginName);
        if(isRememberPassword==0){
            loginPassword=Md5Utils.toMd5(loginPassword);
        }
        //密码正确
        if(loginPassword.equals(userQuery.getLoginPassword())){
            userId=userQuery.getUserId();
        }
        //密码错误则uerId为默认的0
        //处理dao层的异常
        return userId;
    }
    /**验证用户的身份，吃瓜群众1管理员2游客3超管4*/
    @Override
    public int verifyRole(int userId){
        int roleId;
        roleId= iUserDao.verifyRole(userId);
        return roleId;
    }
    /**验证要修改的密码*/
    @Override
    public int verifyPassword(String oldPassword, int userId){
        int row=0;
        String realPassword=iUserDao.verifyPassword(userId);
        oldPassword=Md5Utils.toMd5(oldPassword);
        if(realPassword.equals(oldPassword)){
            //旧密码输入正确
            row=1;
        }
        //密码输入不正确则为默认0
        return row;
    }
    /**修改密码*/
    @Override
    public int changePassword(String newPassword, int userId){
        int row;
        User user = new User();
        //新密码加密
        newPassword=Md5Utils.toMd5(newPassword);
        user.setLoginPassword(newPassword);
        user.setUserId(userId);
        row= iUserDao.changePassword(user);
        return row;
    }
    /**查询用户的个人信息*/
    @Override
    public User queryUserInformation(int userId){
        User userQuery;
        //用集合来存数据
        userQuery= iUserDao.queryUserInformation(userId);
        return userQuery;
    }
    /**查看是否记住密码，是的话，把密码返回*/
    @Override
    public User isRememberPassword(String loginName){
        User user ;
        user= iUserDao.isRememberPassword(loginName);
        return user;
    }
    /**保存用户设置的头像文件到数据库，把该二进制文件存到特定文件夹*/
    @Override
    public int saveIcon(InputStream inputStream, int userId){
        int judge;
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            //头像存进数据库
            judge= iUserDao.saveIcon(conn,inputStream,userId);
            //读出
            InputStream binaryStream = iUserDao.queryIcon(conn,userId);
            //存进本地
            ImageUtils.readBlob(binaryStream,"C:\\Users\\Mono\\Desktop\\TiliTili照片\\" + userId + ".jpg");
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ServiceException("数据回滚异常",e);
            }
            e.printStackTrace();
            throw new ServiceException("保存头像异常",e);
        }
        return judge;
    }
    /**删除用户保存的头像文件*/
    @Override
    public boolean deleteIcon(int userId){
        int judge;
        boolean flag=false;
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            //删除用户头像在数据库文件
            judge = iUserDao.deleteIcon(conn,userId);
            if(judge==1){
                File file = new File("C:\\Users\\Mono\\Desktop\\TiliTili照片\\" + userId + ".jpg");
                if(file.exists()){
                    flag = file.delete();
                    //本地文件夹照片文件也删除
                }
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new ServiceException("数据回滚异常",e);
            }
            e.printStackTrace();
            throw new ServiceException("删除头像异常",e);
        }
        return flag;
    }
}
