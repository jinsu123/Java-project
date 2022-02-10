package com.lcomputerstudy.testmvc.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.lcomputerstudy.testmvc.database.DBConnection;
import com.lcomputerstudy.testmvc.vo.FileUpload;

public class FileDAO {
	private static FileDAO dao = null;
	
	private FileDAO() {
		
	}
	
	public static FileDAO getInstance() {
		if(dao == null) {
			dao = new FileDAO();
		}
		return dao;
		
	}
	
	public void insertFile(FileUpload fileupload) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "insert into file( b_idx, f_name ) values(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, fileupload.getB_idx());
			pstmt.setString(2, fileupload.getF_name());
			pstmt.executeUpdate();
			
		}catch(Exception ex) {
			System.out.println("SQLException :"+ ex.getMessage());
		}finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
