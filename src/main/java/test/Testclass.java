package test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import util.DBUtil;

public class Testclass {
	
	@Test
	public void test1() throws SQLException {
		Connection connection =DBUtil.getConnection();
		System.out.println(connection);
	}

}
