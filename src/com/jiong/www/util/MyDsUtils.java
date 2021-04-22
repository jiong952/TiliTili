package com.jiong.www.util;

import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Mono
 */
public class MyDsUtils {
    private static DataSource dataSource;

    private static ThreadLocal<Connection> thread = new ThreadLocal<>();

    static{

        dataSource = new MyDataSource("com.mysql.cj.jdbc.Driver",

                        "jdbc:mysql://localhost:3306/tilitili?useUnicode=true&characterEncoding=utf8&useSSL=true&serverTimezone=UTC",

                        "root","123456",3);

    }

    /**

     * 直接获取一个Connection

     */

    public static Connection getConnection(){

        Connection con ;

        try {

            con= dataSource.getConnection();

        } catch (SQLException e) {

            throw new RuntimeException(e.getMessage(),e);

        }

        return con;

    }

    /**

     * 获取线程局部的Connection

     */

    public static Connection getThreadConn(){

        Connection con = thread.get();
        //先从线程中取数据

        if(con==null){

            con = getConnection();

            thread.set(con);

        }

        return con;

    }

    /**

     * 可选的调用删除局部线程中的对象

     */

    public static void remove(){

        thread.remove();

    }

    /**

     * 获取一个DataSource

     */

    public static DataSource getDataSource(){

        return dataSource;

    }
    public static QueryRunner queryRunner = new QueryRunner(dataSource);
}
