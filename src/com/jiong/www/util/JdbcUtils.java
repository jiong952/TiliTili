package com.jiong.www.util;

import java.sql.*;

/**
 * @author Mono
 */
public class JdbcUtils {

    //加载驱动,放在static里，静态加载
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**用用户信息和url进行数据库连接*/
    static String url ="jdbc:mysql://localhost:3306/tilitili?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=UTC";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,"root","123456");
    }

    /**释放连接*/
    public static void release(Connection conn, PreparedStatement ps, ResultSet rs) throws SQLException {
        if(rs!=null){
            rs.close();
        }
        if (ps!=null){
            ps.close();
        }
        if(conn!=null){
            conn.close();
        }
    }

}
