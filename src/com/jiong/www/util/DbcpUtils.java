package com.jiong.www.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;

/**
 * @author Mono
 */
public class DbcpUtils {
    private static BasicDataSource dataSource = null;
    static {
        try {
            InputStream in = DbcpUtils.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
            Properties properties = new Properties();
            properties.load(in);
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**获取连接*/
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public static QueryRunner queryRunner = new QueryRunner(dataSource);
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
