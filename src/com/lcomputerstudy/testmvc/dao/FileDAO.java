package com.lcomputerstudy.testmvc.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lcomputerstudy.testmvc.database.DBConnection;
import com.lcomputerstudy.testmvc.vo.Board;
import com.lcomputerstudy.testmvc.vo.FileUpload;
import com.lcomputerstudy.testmvc.vo.User;

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
	
	public void insertFile(File uploadFile,	Board board) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "insert into file( b_idx, f_name ) values(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board.getB_idx());
			pstmt.setString(2, uploadFile.getName());
			pstmt.executeUpdate();
//			pstmt.close();
//			if(board.getB_idx()==0) {
//				sql = "update file ta left JOIN board tb on tb.b_idx = last_insert_id() set ta.b_idx = tb.b_idx";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.executeUpdate();
//			}
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
	
	
	public FileUpload detailFileUpload(int b_idx) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		FileUpload file = null;
		
		try {
			conn = DBConnection.getConnection();
			String query = "select * from file where b_idx=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, b_idx);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				file = new FileUpload();
				file.setB_idx(rs.getInt("b_idx"));
				file.setF_name(rs.getString("f_name"));
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
}
