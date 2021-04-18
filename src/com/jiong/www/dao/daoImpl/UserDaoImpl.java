package com.jiong.www.dao.daoImpl;

import com.jiong.www.dao.dao.IUserDao;
import com.jiong.www.po.User;
import com.jiong.www.util.JdbcUtils;

import java.io.InputStream;
import java.sql.*;

/**
 * @author Mono
 */
public class UserDaoImpl implements IUserDao {
    /**注册，添加用户信息到用户表*/
    @Override
    public int doRegister(User user)  {
        int row=0;
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="INSERT INTO `user` (`login_name`,`login_password`,`user_nickname`) VALUES(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,user.getLoginName());
            ps.setString(2,user.getLoginPassword());
            ps.setString(3,user.getLoginName());
            //昵称默认为用户名
            row= ps.executeUpdate();
            //sql语句返回结果判断
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //释放连接
        return row;
        //向上抛出到view层
    }
    /**把新注册的用户加入到用户角色表，默认新注册只能为吃瓜群众即1*/
    @Override
    public void doInsertRole(int userId)  {
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="INSERT INTO `user_role`(`user_id`,`role_id`)VALUES(?,1)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            ps.executeUpdate();
            //sql语句返回结果判断
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**用用户名查用户id*/
    @Override
    public int doQueryId(String userName)  {
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        int userId=0;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `user_id` FROM `user` WHERE `login_name`=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,userName);
            //昵称默认为用户名
            rs = ps.executeQuery();
            while (rs.next()){
                userId=rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userId;
    }

    /**用于注册时验证该用户名是否存在*/
    @Override
    public int verifyExist(String loginName)  {
        int row=0;
        //默认为0不存在
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql="SELECT `user_id` FROM `user` WHERE `login_name`=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,loginName);
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                row=1;
                //表里有数据则row=1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return row;
        //抛出到view层判断
    }
    /**完善用户信息,实现了每次只改动某个信息，其他的保存为上次的值*/
    @Override
    public int perfectInformation(User user)  {
        int row = 0;
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            //查询并储存该用户的信息的原先值
            String sql ="UPDATE `user` SET `user_e-mail`=?,`user_nickname`=?,`user_gender`=?,`user_description`=?,`user_birthday`=? WHERE `user_id`=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUserEmail());
            ps.setString(2,user.getUserNickname());
            ps.setInt(3,user.getUserGender());
            ps.setString(4,user.getUserDescription());
            ps.setDate(5, user.getUserBirthday());
            ps.setInt(6,user.getUserId());
            row = ps.executeUpdate();
            //row是返回值，用于判断
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //释放连接
        return row;
    }
    /**是否要记住密码*/
    @Override
    public void isRememberPassword(User user) {
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="UPDATE `user` SET `password_remember`=? WHERE `user_id`=?";
            ps = conn.prepareStatement(sql);
            //如果用户没有修改该栏信息，则保留上次的值,修改则覆盖
            ps.setInt(1,user.getIsRememberPassword());
            ps.setInt(2,user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //释放连接
        }
    }
    /**登录*/
    @Override
    public User login(String loginName)  {
        User userQuery = new User();
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `user_id`,`login_password`FROM `user` WHERE `login_name`=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,loginName);
            rs = ps.executeQuery();
            //用户的id
            if(rs.isBeforeFirst()){
                while (rs.next()){
                    userQuery.setLoginPassword(rs.getString("login_password"));
                    userQuery.setUserId(rs.getInt("user_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userQuery;
    }
    /**验证用户的身份，吃瓜群众1管理员2游客3超管4*/
    @Override
    public int verifyRole(int userId) {
        int roleId=0;
        //用户角色的id
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `role_id` FROM `user_role`WHERE`user_id`=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            rs = ps.executeQuery();
            while (rs.next()){
                roleId=rs.getInt("role_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roleId;
    }
    /**验证要修改的密码*/
    @Override
    public String verifyPassword(int userId) {
        String realPassword=null;
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `login_password` FROM `user` WHERE `user_id`=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            rs = ps.executeQuery();
            //数据库中用户名对应的正确密码
            while(rs.next()){
                realPassword=rs.getString("login_password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return realPassword;
    }
    /**修改密码*/
    @Override
    public int changePassword(User user)  {
        int row=0;
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="UPDATE `user` SET `login_password`=? WHERE `user_id`=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,user.getLoginPassword());
            ps.setInt(2,user.getUserId());
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //释放连接
        return row;
    }
    /**查询用户的个人信息*/
    @Override
    public User queryUserInformation(int userId){
        User userQuery = new User();
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `login_name`,`user_e-mail`,`user_nickname`,\n" +
                    "`user_gender`,`user_description`,`user_birthday`,`login_password`,`password_remember`FROM `user` WHERE `user_id`=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,userId);
            rs = ps.executeQuery();
            while(rs.next()){
                userQuery.setLoginName(rs.getString("login_name"));
                userQuery.setUserEmail(rs.getString("user_e-mail"));
                userQuery.setUserNickname(rs.getString("user_nickname"));
                userQuery.setUserGender(rs.getInt("user_gender"));
                userQuery.setUserDescription(rs.getString("user_description"));
                userQuery.setUserBirthday(rs.getDate("user_birthday"));
                userQuery.setLoginPassword(rs.getString("login_password"));
                userQuery.setIsRememberPassword(rs.getInt("password_remember"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //把查询的结果集返回到service层
        return userQuery;
    }
    /**查看是否记住密码，是的话，把密码返回*/
    @Override
    public User isRememberPassword(String loginName)  {
        User user = new User();
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="SELECT `password_remember`,`login_password` FROM `user` WHERE `login_name`=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,loginName);
            rs = ps.executeQuery();
            while (rs.next()){
                user.setIsRememberPassword(rs.getInt("password_remember"));
                user.setLoginPassword(rs.getString("login_password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }
    /**保存用户设置的头像文件到数据库*/
    @Override
    public int saveIcon(InputStream inputStream, int userId){
        Connection conn = null;
        PreparedStatement ps=null;
        int row=0;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="UPDATE `user` SET `icon` = ? WHERE `user_id` = ?";
            ps = conn.prepareStatement(sql);
            ps.setBinaryStream(1,inputStream);
            ps.setInt(2,userId);
            row = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return row;
    }
    /**保存用户设置的头像文件本地文件夹*/
    @Override
    public InputStream queryIcon(int userId){
        Connection conn = null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        InputStream binaryStream=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql1 = "SELECT `icon` FROM `user` WHERE `user_id`=?";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1,userId);
            rs = ps.executeQuery();
            while (rs.next()){
                binaryStream = rs.getBinaryStream("icon");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return binaryStream;
    }
    /**删除用户保存的头像文件*/
    @Override
    public int deleteIcon(int userId) {
        int row = 0;
        Connection conn = null;
        PreparedStatement ps=null;
        try {
            conn = JdbcUtils.getConnection();
            String sql ="UPDATE `user` SET `icon` = ? WHERE `user_id` = ?";
            ps = conn.prepareStatement(sql);
            ps.setBinaryStream(1,null);
            ps.setInt(2,userId);
            row=ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                JdbcUtils.release(conn,ps,null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return row;
    }
}
