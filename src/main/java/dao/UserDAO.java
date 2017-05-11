package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.User;
import util.DBUtil;

public class UserDAO implements Serializable{
	
	public User findByUserName(String userName) {
		User user = null;
		Connection conn = null;
		try {
			conn  = DBUtil.getConnection();
			String sql = "SELECT * FROM user_lyy WHERE username=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userName);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUserName(rs.getString("username"));
				user.setPwd(rs.getString("pwd"));
				user.setName(rs.getString("name"));
				user.setGender(rs.getString("gender"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DBUtil.close(conn);
		}
		return user;
	}
	
	public void save(User user) {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "INSERT INTO user_lyy values(null,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getPwd());
			ps.setString(3, user.getName());
			ps.setString(4, user.getGender());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			DBUtil.close(conn);
		}
	}

}
