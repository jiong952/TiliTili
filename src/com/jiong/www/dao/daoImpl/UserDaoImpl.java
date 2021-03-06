package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.DaoException;
import com.jiong.www.dao.dao.IUserDao;
import com.jiong.www.po.User;

import static com.jiong.www.util.MyDsUtils.*;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;


/**
 * @author Mono
 */
public class UserDaoImpl implements IUserDao {
    /**注册，添加用户信息到用户表*/
    @Override
    public int register(Connection conn, User user)  {
        int row;
        String sql ="INSERT INTO `user` (`login_name`,`login_password`,`user_nickname`) VALUES(?,?,?)";
        Object[] params= {user.getLoginName(), user.getLoginPassword(),user.getLoginName()};
        try {
            row=queryRunner.execute(conn,sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("注册异常",e);
        }
        //sql语句返回结果判断
        //释放连接
        return row;
        //向上抛出到view层
    }
    /**把新注册的用户加入到用户角色表，默认新注册只能为吃瓜群众即1*/
    @Override
    public void insertToRole(Connection conn, int userId, int roleId)  {
        Object[] params={userId,roleId};
        String sql ="INSERT INTO `user_role`(`user_id`,`role_id`)VALUES(?,?)";
        try {
            queryRunner.execute(conn,sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("更新用户角色表异常",e);
        }
    }
    /**用用户名查用户id*/
    @Override
    public int queryId(String userName)  {
        int userId=0;
        String sql ="SELECT `user_id` AS userId FROM `user` WHERE `login_name`=?";
        try {
            User user = queryRunner.query(sql, new BeanHandler<>(User.class), userName);
            if(user!=null){
                userId=user.getUserId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("用用户名查用户id异常",e);
        }
        return userId;
    }

    /**用于注册时验证该用户名是否存在*/
    @Override
    public int isExist(String loginName)  {
        User userQuery = new User();
        int row=0;
        //默认为0不存在
        String sql="SELECT `user_id` AS userId FROM `user` WHERE `login_name`=?";
        try {
            userQuery = queryRunner.query(sql, new BeanHandler<>(User.class), loginName);
            if(userQuery!=null){
                row=1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("验证该用户名是否存在异常",e);
        }
        return row;
        //抛出到view层判断
    }
    /**完善用户信息,实现了每次只改动某个信息，其他的保存为上次的值*/
    @Override
    public int updateInformation(User user)  {
        int row;
        Object[] params = {user.getUserEmail(),user.getUserNickname(),user.getUserGender(),user.getUserDescription(),user.getUserBirthday(),user.getUserId()};
        //查询并储存该用户的信息的原先值
        String sql ="UPDATE `user` SET `user_e-mail`=?,`user_nickname`=?,`user_gender`=?,`user_description`=?,`user_birthday`=? WHERE `user_id`=?";
        try {
            row = queryRunner.execute(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("更新用户信息异常",e);
        }
        //释放连接
        return row;
    }
    /**是否要记住密码*/
    @Override
    public void isRememberPassword(User user) {
        Object[] params = {user.getIsRememberPassword(),user.getUserId()};
        String sql ="UPDATE `user` SET `password_remember`=? WHERE `user_id`=?";
        //如果用户没有修改该栏信息，则保留上次的值,修改则覆盖
        try {
            queryRunner.execute(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("更新是否记住密码异常",e);
        }
    }
    /**登录*/
    @Override
    public User login(String loginName)  {
        User userQuery = new User();
        String sql ="SELECT `user_id` AS userId,`login_password` AS loginPassword FROM `user` WHERE `login_name`=?";
        try {
            userQuery = queryRunner.query(sql, new BeanHandler<>(User.class), loginName);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("登录异常",e);
        }
        return userQuery;
    }
    /**验证用户的身份，吃瓜群众1管理员2游客3超管4*/
    @Override
    public int queryRole(int userId) {
        int roleId;
        //用户角色的id
        String sql ="SELECT `role_id` FROM `user_role`WHERE`user_id`=?";
        try {
            roleId = queryRunner.query(sql, new ScalarHandler<>(), userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看用户身份异常",e);
        }
        return roleId;
    }
    /**验证要修改的密码*/
    @Override
    public String queryPwd(int userId) {
        String realPassword;
        String sql ="SELECT `login_password` AS loginPassword FROM `user` WHERE `user_id`=?";
        try {
            User query = queryRunner.query(sql, new BeanHandler<>(User.class), userId);
            //数据库中用户名对应的正确密码
            realPassword=query.getLoginPassword();
            System.out.println(realPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看密码异常",e);
        }
        return realPassword;
    }
    /**修改密码*/
    @Override
    public int changePwd(User user)  {
        int row;
        Object[] params ={user.getLoginPassword(),user.getUserId()};
        String sql ="UPDATE `user` SET `login_password`=? WHERE `user_id`=?";
        try {
            row=queryRunner.execute(sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("修改密码异常",e);
        }
        //释放连接
        return row;
    }
    /**查询用户的个人信息*/
    @Override
    public User queryInformation(int userId){
        String sql ="SELECT `login_name` AS loginName,`user_e-mail` AS userEmail,`user_nickname` AS userNickname,\n" +
                "`user_gender` AS userGender,`user_description` AS userDescription,`user_birthday` AS userBirthday," +
                "`login_password` AS loginPassword,`password_remember` AS isRememberPassword FROM `user` WHERE `user_id`=?";
        User query = new User();
        try {
            query = queryRunner.query(sql, new BeanHandler<>(User.class), userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查询用户个人信息异常",e);
        }
        //把查询的结果集返回到service层
        return query;
    }
    /**查看是否记住密码，是的话，把密码返回*/
    @Override
    public User isRememberPassword(String loginName)  {
        User query=new User();
        String sql ="SELECT `password_remember` AS isRememberPassword,`login_password` AS loginPassword FROM `user` WHERE `login_name`=?";
        try {
            User user = queryRunner.query(sql, new BeanHandler<>(User.class), loginName);
            if(user!=null){
                query=user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查看是否记住密码异常",e);
        }
        return query;
    }
    /**保存用户设置的头像文件到数据库*/
    @Override
    public int saveIcon(Connection conn,InputStream inputStream, int userId){
        int row;
        Object[] params = {inputStream,userId};
        String sql ="UPDATE `user` SET `icon` = ? WHERE `user_id` = ?";
        try {
            row=queryRunner.execute(conn,sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("保存头像到数据库异常",e);
        }
        return row;
    }
    /**保存用户设置的头像文件本地文件夹*/
    @Override
    public InputStream queryIcon(Connection conn,int userId){
        InputStream binaryStream;
        String sql = "SELECT `icon` AS  icon FROM `user` WHERE `user_id`=?";
        try {
            User query = queryRunner.query(conn,sql, new BeanHandler<>(User.class), userId);
            binaryStream=new ByteArrayInputStream(query.getIcon());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("保存头像到本地文件夹异常",e);
        }
        return binaryStream;
    }
    /**删除用户保存的头像文件*/
    @Override
    public int deleteIcon(Connection conn,int userId) {
        int row;
        String sql ="UPDATE `user` SET `icon` = ? WHERE `user_id` = ?";
        Object[] params={null,userId};
        try {
            row=queryRunner.execute(conn,sql,params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("删除数据库头像异常",e);
        }
        return row;
    }
}