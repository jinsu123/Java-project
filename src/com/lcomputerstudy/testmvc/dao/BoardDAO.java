package com.lcomputerstudy.testmvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;

import com.lcomputerstudy.testmvc.database.DBConnection;
import com.lcomputerstudy.testmvc.vo.Board;
import com.lcomputerstudy.testmvc.vo.Pagination;
import com.lcomputerstudy.testmvc.vo.Search;
import com.lcomputerstudy.testmvc.vo.User;

public class BoardDAO {
	private static BoardDAO dao = null;
		
	private BoardDAO() {
		
	}
	
	public static BoardDAO getInstance() {
		if(dao ==null) {
			dao = new BoardDAO();
		}
		return dao;
	}
	
	public ArrayList<Board> getBoards(Pagination pagination){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Board> list = null;
		
		int pageNum = pagination.getPageNum();
		
		int type = Search.NONE;
		String keyword = null;
		List<String> columns = null;
		
		Search search = pagination.getSearch();
		String where = "";
		if (search != null ) {		// 입력된 검색어가 있다면
			type = search.getType();
			keyword = search.getKeyword();
			
			columns = new ArrayList<String>();
			
			switch (type) {
				case Search.TITLE:
					columns.add("b_title");
					break;
				case Search.WRITER:
					columns.add("b_writer");
					break;
				case Search.CONTENT:
					columns.add("b_content");
					break;
				case Search.TITLE_CONTENT:
					columns.add("b_content");
					columns.add("b_title");
					break;
				default:
					break;
			}
			
			where = "WHERE ";
			for (int i=0; i<columns.size(); i++) {				
				where += columns.get(i) + " LIKE ? ";
				
				if (i < columns.size()-1)
					where += " OR ";
			}
		}
		
		try {
			conn = DBConnection.getConnection();
			//String query = "select * from board limit 3";
			String query = new StringBuilder()
					.append("SELECT 		@ROWNUM := @ROWNUM - 1 AS ROWNUM, ")
					.append("				ta.*, tb.u_id, tb.u_name \n")
					.append("FROM 			board ta \n")
					.append("LEFT JOIN 		user tb ON ta.u_idx = tb.u_idx, \n")
					.append("				(SELECT @rownum := (SELECT	COUNT(*)-?+1 FROM board ta))tc \n")
					.append(where)
					.append("order by		b_group desc, b_order aSC \n")
					.append("LIMIT			?, 5 \n")
					.toString();
	       	pstmt = conn.prepareStatement(query); 
	       	
	       	pstmt.setInt(1, pageNum);
	       	int index = 2;
		       	if (search != null) {
			      	for (String column : columns) {
			       		pstmt.setString(index, "%"+keyword+"%");
			       		index++; 
			       	}
		       	} 		       	
	       	pstmt.setInt(index, pageNum);

	        rs = pstmt.executeQuery();
	        list = new ArrayList<Board>();

	        while(rs.next()){     
	        	Board board = new Board();
	        	board.setRownum(rs.getInt("ROWNUM"));
      	       	board.setB_idx(rs.getInt("b_idx"));
       	       	board.setB_title(rs.getString("b_title"));
       	       	board.setB_content(rs.getString("b_content"));
       	       	board.setB_view(rs.getInt("b_view"));
       	       	board.setB_date(rs.getString("b_date"));
	   	       	board.setB_group(rs.getInt("b_group"));
	   	       	board.setB_order(rs.getInt("b_order"));
	   	       	board.setB_depth(rs.getInt("b_depth"));
       	       	board.setU_idx(rs.getInt("u_idx"));
       	       	
       	       	User user = new User();
    	       	user.setU_name(rs.getString("u_name"));
    	       	user.setU_idx(rs.getInt("u_idx"));
    	        board.setUser(user);
       	       	
       	       	list.add(board);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} return list;
	}
	
	public Board insertBoard(Board board) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Board board2 =null;
			try {
				conn = DBConnection.getConnection();
				String sql = "insert into board(b_idx, b_title, b_content, b_date, b_writer, b_view, b_group, b_order, b_depth, u_idx) values(?,?,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, board.getB_idx());
				pstmt.setString(2, board.getB_title());
				pstmt.setString(3, board.getB_content());
				pstmt.setString(4, board.getB_date());
				pstmt.setString(5, board.getB_writer());
				pstmt.setInt(6, board.getB_view());
				pstmt.setInt(7, board.getB_group());
				if(board.getB_group()==0) {
					pstmt.setInt(8, board.getB_order());
					pstmt.setInt(9, board.getB_depth());
				}else { 
					board.setB_order(board.getB_order()+1);
					board.setB_depth(board.getB_depth()+1);
					pstmt.setInt(8, board.getB_order());
					pstmt.setInt(9, board.getB_depth());
				}
				pstmt.setInt(10, board.getU_idx());
				pstmt.executeUpdate();
				pstmt.close();
				if(board.getB_group()==0) {
					sql = "update board set b_group = last_insert_id() where b_idx = last_insert_id()";
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();
				}else {
					sql = "update board set b_order = b_order+1 where b_group = ? and b_order >= ? and b_idx <> last_insert_id()";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, board.getB_group());
					pstmt.setInt(2, board.getB_order());
					pstmt.executeUpdate();
					pstmt.close();
				}
				sql = "select last_insert_id() as b_idx ";
				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, board.getB_idx());
				
				rs = pstmt.executeQuery();
				
		        while(rs.next()){     
		        	
		        	board2 = new Board();
	      	       	board2.setB_idx(rs.getInt("b_idx"));
	       	       	
			        }
			}catch(Exception ex) {
				System.out.println("SQLException :"+ ex.getMessage());
			}finally {
				try {
					if (rs != null) rs.close();
					if (pstmt != null) pstmt.close();
					if (conn != null) conn.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			return board2;
	
	}
	
	public Board detailBoard(int b_idx) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Board board = null;
		
		try {
			conn = DBConnection.getConnection();
			String query = "select * from board ta left join user tb on ta.u_idx= tb.u_idx where b_idx=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, b_idx);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				board = new Board();
				board.setB_idx(rs.getInt("b_idx"));
				board.setB_title(rs.getString("b_title"));
				board.setB_content(rs.getString("b_content"));
				board.setB_writer(rs.getString("b_writer"));
				board.setB_date(rs.getString("b_date"));
				board.setB_view(rs.getInt("b_view"));
				board.setB_group(rs.getInt("b_group"));
				board.setB_order(rs.getInt("b_order"));
				board.setB_depth(rs.getInt("b_depth"));
				board.setU_idx(rs.getInt("u_idx"));
				User user = new User();
				user.setU_name(rs.getString("u_name"));
//				board.setU_idx(rs.getInt("u_idx"));
				board.setUser(user);
				
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
		return board;
	}
	
	public Board editBoard(int b_idx) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Board board = null;
		
		try {
			conn = DBConnection.getConnection();
			String query = "select * from board where b_idx=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, b_idx);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				board = new Board();
				board.setB_idx(Integer.parseInt(rs.getString("b_idx")));
				board.setB_title(rs.getString("b_title"));
				board.setB_content(rs.getString("b_content"));
				board.setB_writer(rs.getString("b_writer"));
				
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
		return board;
	}
	
	public void editProcessBoard(Board board) {

		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "UPDATE board SET b_writer = ?,b_title = ?,b_content = ?,b_date=?  where b_idx=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, board.getB_writer());
			pstmt.setString(2, board.getB_title());
			pstmt.setString(3, board.getB_content());
			pstmt.setString(4, board.getB_date());
			pstmt.setInt(5, board.getB_idx());
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
	
	public Board deleteBoard(int b_idx){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Board board =null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "delete from board where b_idx=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, b_idx);
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
		return board;
	}
	
	public void viewcntBoard(int b_idx){
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "update board set b_view = b_view+1 where b_idx=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b_idx);
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
	
	public int getBoardsCount(Search search) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		int count = 0;
		
		
		int type = Search.NONE;
		String keyword = null;
		List<String> columns = null;
		
		String where = "";
		if (search != null) {		// 입력된 검색어가 있다면
			type = search.getType();
			keyword = search.getKeyword();
			
			columns = new ArrayList<String>();
			
			switch (type) {
				case Search.TITLE:
					columns.add("b_title");
					break;
				case Search.WRITER:
					columns.add("b_writer");
					break;
				case Search.CONTENT:
					columns.add("b_content");
					break;
				case Search.TITLE_CONTENT:
					columns.add("b_content");
					columns.add("b_title");
					break;
				default:
					break;
			}
			
			where = "WHERE ";
			for (int i=0; i<columns.size(); i++) {				
				where += columns.get(i) + " LIKE ? ";
				
				if (i < columns.size()-1)
					where += " OR ";
			}
		}

		try {
			conn = DBConnection.getConnection();
			String query =  new StringBuilder()
				        .append("SELECT COUNT(*) count FROM board  ")
				        .append(where)
				        .toString();
	       	pstmt = conn.prepareStatement(query);
	    
	       	int index = 1;
		       	if (search != null) {
			      	for (String column : columns) {
			       		pstmt.setString(index, "%"+keyword+"%");
			       		index++; 
			       	}
		       	} 		       	
	        	
	        rs = pstmt.executeQuery();
	        
	        while(rs.next()){     
	        	count = rs.getInt("count");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	
	public ArrayList<Board> getBoards(int page){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		ArrayList<Board> list = null;
		int pageNum = (page-1)*5;
		
		try {
			conn = DBConnection.getConnection();
//			String query = "select * from board limit ?,5";
			String query = new StringBuilder()
				.append("SELECT                   @ROWNUM := @ROWNUM -1 AS ROWNUM,\n")
				.append("	                     ta.*\n")
				.append("FROM	                 board ta,\n")
				.append("	               		 (SELECT @rownum := (SELECT COUNT(*)-?+1 FROM board ta))tb\n")
				.append("LIMIT	          		 ?,5\n")
				.toString();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, pageNum);
			pstmt.setInt(2, pageNum);
//			pstmt.setInt(1, page);
			rs = pstmt.executeQuery();
			list = new ArrayList<Board>();
			
			while(rs.next()) {
				Board board = new Board();
				board.setRownum(rs.getInt("ROWNUM"));
				board.setB_idx(rs.getInt("b_idx"));
				board.setB_title(rs.getString("b_title"));
				board.setB_content(rs.getString("b_content"));
				board.setB_writer(rs.getString("b_writer"));
				board.setB_date(rs.getString("b_date"));
				board.setB_view(rs.getInt("b_view"));
				
				list.add(board);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(conn!=null) conn.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	
	}
	


}