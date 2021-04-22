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

     * 获取一个DataSource

     */

    public static DataSource getDataSource(){

        return dataSource;

    }
    public static QueryRunner queryRunner = new QueryRunner(dataSource);
}
