package test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import util.DBUtil;

public class Demo {

	@Test
	public void test2() {
		Connection connection = null;
		try {
			connection = DBUtil.getConnection();
			System.out.println(connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("创建连接失败",e);
		}
	}
	
}
