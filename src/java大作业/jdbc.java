/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java大作业;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author lenovo
 */
public class jdbc {
        private Connection conn=null;
    	public static Connection getConn() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;DatabaseName=Movie_Homework",
					"sa", "88888888");
			return conn;
                        
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			return null;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}


	public static int modifyBySql(String sql) throws SQLException {
		int rs = 0;
		Connection conn = getConn();
		Statement stmt = conn.createStatement();
		rs = stmt.executeUpdate(sql);
		System.out.println(rs);
		stmt.close();
		conn.close();

		return rs;

	}

	public static ResultSet queryBySql(String sql) throws SQLException {
		ResultSet rs = null;
		Connection conn = getConn();
		Statement stmt = conn.createStatement();
		rs = stmt.executeQuery(sql);

		return rs;

	}
        public void closeCon(){
		if(this.conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
}
 