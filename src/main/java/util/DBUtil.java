package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

public class DBUtil {
	
	private static BasicDataSource ds;
	
	static {
		Properties p = new Properties();
		try {
			//1.读取参数
			p.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
			String driver = p.getProperty("driver");
			String url = p.getProperty("url");
			String user = p.getProperty("user");
			String pwd = p.getProperty("pwd");
			String initSize = p.getProperty("initSize");
			String maxSize = p.getProperty("maxSize");
			//2.创建连接池
			ds = new 	BasicDataSource();
			//3.设置参数
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(user);
			ds.setPassword(pwd);
			ds.setInitialSize(new Integer(initSize));
			ds.setMaxActive(new Integer(maxSize));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("加载文件失败");
		}
	}
	public static Connection getConnection() 
			throws SQLException {
			return ds.getConnection();
		}
	
	/**
	 * 该连接由连接池创建,实际上该连接的实现类
	 * 也是由连接池重写了,它所提供的close方法,
	 * 不再是关闭连接,而是改为归还连接.
	 */
	public static void close(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(
					"归还连接失败", e);
			}
		}
	}

}
