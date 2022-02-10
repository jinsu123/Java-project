package com.lcomputerstudy.testmvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lcomputerstudy.testmvc.database.DBConnection;
import com.lcomputerstudy.testmvc.vo.Board;
import com.lcomputerstudy.testmvc.vo.Comment;
import com.lcomputerstudy.testmvc.vo.Comment;
import com.lcomputerstudy.testmvc.vo.Comment;
import com.lcomputerstudy.testmvc.vo.Comment;
import com.lcomputerstudy.testmvc.vo.Pagination;
import com.lcomputerstudy.testmvc.vo.Search;
import com.lcomputerstudy.testmvc.vo.User;

public class CommentDAO {
	private static CommentDAO dao = null;
	
	private CommentDAO(){
		
	}
	
	public static CommentDAO getInstance() {
		if(dao ==null) {
			dao = new CommentDAO();
		}
		return dao;
	}
	
	public ArrayList<Comment> getComments(Pagination pagination, Board board){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Comment> list = null;
		
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
					columns.add("c_title");
					break;
				case Search.CONTENT:
					columns.add("c_content");
					break;
				case Search.TITLE_CONTENT:
					columns.add("c_content");
					columns.add("c_title");
					break;
				default:
					break;
			}
			
			where = "AND ( ";
			for (int i=0; i<columns.size(); i++) {
				if (i==0)
				where += columns.get(i) + " LIKE ? ";
				
				if (i < columns.size()-1)
					where += " OR ";
			}
			where += ")";
		}
		
		try {
			conn = DBConnection.getConnection();
			//String query = "select * from board limit 3";
			String query = new StringBuilder()
					.append("SELECT 		@ROWNUM := @ROWNUM - 1 AS ROWNUM, ")
					.append("				ta.*, tb.u_id, tb.u_name \n")
					.append("FROM 			Comment ta \n")
					.append("LEFT JOIN 		user tb ON ta.u_idx = tb.u_idx, \n")
					.append("				(SELECT @rownum := (SELECT	COUNT(*)-?+1 FROM Comment ta))tc \n")
					.append("WHERE 			b_idx = ? \n")
					.append(where)
					.append("order by		c_group desc, c_order aSC \n")
					.append("LIMIT			?, 5 \n")
					.toString();
	       	pstmt = conn.prepareStatement(query); 
	       	
	       	pstmt.setInt(1, pageNum);
	       	pstmt.setInt(2, board.getB_idx());
	       	int index = 3;
		       	if (search != null) {
			      	for (String column : columns) {
			       		pstmt.setString(index, "%"+keyword+"%");
			       		index++; 
			       	}
		       	} 		       	
	       	pstmt.setInt(index, pageNum);

	        rs = pstmt.executeQuery();
	        list = new ArrayList<Comment>();

	        while(rs.next()){     
	        	Comment comment = new Comment();
	        	comment.setRownum(rs.getInt("ROWNUM"));
      	       	comment.setC_idx(rs.getInt("c_idx"));
       	       	comment.setC_content(rs.getString("c_content"));
       	       	comment.setC_date(rs.getString("c_date"));
	   	       	comment.setC_group(rs.getInt("c_group"));
	   	       	comment.setC_order(rs.getInt("c_order"));
	   	       	comment.setC_depth(rs.getInt("c_depth"));
       	       	comment.setU_idx(rs.getInt("u_idx"));
       	       	comment.setB_idx(rs.getInt("b_idx"));
       	       	
       	       	User user = new User();
    	       	user.setU_name(rs.getString("u_name"));
    	       	user.setU_idx(rs.getInt("u_idx"));
    	        comment.setUser(user);
       	       	

    	        
       	       	list.add(comment);
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
	
	public void insertComment(Comment comment) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		
			try {
				conn = DBConnection.getConnection();
				String sql = "insert into comment(c_idx, c_content, c_date, c_group, c_order, c_depth, u_idx, b_idx) values(?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, comment.getC_idx());
				pstmt.setString(2, comment.getC_content());
				pstmt.setString(3, comment.getC_date());
				pstmt.setInt(4, comment.getC_group());
				if(comment.getC_group()==0) {
					pstmt.setInt(5, comment.getC_order());
					pstmt.setInt(6, comment.getC_depth());
				}else { 
					comment.setC_order(comment.getC_order()+1);
					comment.setC_depth(comment.getC_depth()+1);
					pstmt.setInt(5, comment.getC_order());
					pstmt.setInt(6, comment.getC_depth());
				}
				pstmt.setInt(7, comment.getU_idx());
				pstmt.setInt(8, comment.getB_idx());
				pstmt.executeUpdate();
				pstmt.close();
				if(comment.getC_group()==0) {
					sql = "update comment set c_group = last_insert_id() where c_idx = last_insert_id()";
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();
				}else {
					sql = "update comment set c_order = c_order+1 where c_group = ? and c_order >= ? and c_idx <> last_insert_id()";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, comment.getC_group());
					pstmt.setInt(2, comment.getC_order());
					pstmt.executeUpdate();
				}
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
	
	public Comment detailComment(int b_idx) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Comment comment = null;
		
		try {
			conn = DBConnection.getConnection();
			String query = "select * from Comment ta left join board tb on ta.b_idx= tb.b_idx where tb.b_idx=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, b_idx);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				comment = new Comment();
				comment.setC_idx(rs.getInt("c_idx"));
				comment.setC_content(rs.getString("c_content"));
				comment.setC_date(rs.getString("c_date"));
//				comment.setC_group(rs.getInt("c_group"));
//				comment.setC_order(rs.getInt("c_order"));
//				comment.setC_depth(rs.getInt("c_depth"));
				comment.setU_idx(rs.getInt("u_idx"));
	    	        
				
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
		return comment;
	
	}
	
	public Comment editComment(int c_idx) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Comment comment = null;
		
		try {
			conn = DBConnection.getConnection();
			String query = "select * from comment where c_idx=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, c_idx);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				comment = new Comment();
				comment.setC_idx(Integer.parseInt(rs.getString("c_idx")));
				comment.setC_content(rs.getString("c_content"));
				
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
		return comment;
	}
	
	public void editProcessComment(Comment comment) {

		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "UPDATE comment SET  c_content = ?,c_date=?  where c_idx=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, comment.getC_content());
			pstmt.setString(2, comment.getC_date());
			pstmt.setInt(3, comment.getC_idx());
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

	public Comment deleteComment(int c_idx){
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		Comment comment =null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "delete from Comment where c_idx=?";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, c_idx);
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
		return comment;
	}
	
	
	public void viewcntComment(int c_idx){
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "update Comment set c_view = c_view+1 where c_idx=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, c_idx);
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
	
	
	public int getCommentsCount(Search search, Board board) {
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
				
				case Search.CONTENT:
					columns.add("b_content");
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
				        .append("SELECT COUNT(*) count FROM comment where b_idx=? ")
				        .append(where)
				        .toString();
	       	pstmt = conn.prepareStatement(query);
	       	pstmt.setInt(1, board.getB_idx());
	       	int index = 2;
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
	
	public ArrayList<Comment> getComments(int page){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		ArrayList<Comment> list = null;
		int pageNum = (page-1)*5;
		
		try {
			conn = DBConnection.getConnection();
//			String query = "select * from board limit ?,5";
			String query = new StringBuilder()
				.append("SELECT                   @ROWNUM := @ROWNUM -1 AS ROWNUM,\n")
				.append("	                     ta.*\n")
				.append("FROM	                 Comment ta,\n")
				.append("	               		 (SELECT @rownum := (SELECT COUNT(*)-?+1 FROM Comment ta))tb\n")
				.append("LIMIT	          		 ?,5\n")
				.toString();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, pageNum);
			pstmt.setInt(2, pageNum);
//			pstmt.setInt(1, page);
			rs = pstmt.executeQuery();
			list = new ArrayList<Comment>();
			
			while(rs.next()) {
				Comment board = new Comment();
				board.setRownum(rs.getInt("ROWNUM"));
				board.setC_idx(rs.getInt("c_idx"));
				board.setC_content(rs.getString("c_content"));
//				board.setC_writer(rs.getString("c_writer"));
				board.setC_date(rs.getString("c_date"));
				
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
	


//	public void insertComment(Comment comment) {
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//
//		
//			try {
//				conn = DBConnection.getConnection();
//				String sql = "insert into comment(c_idx, c_content, c_date, c_writer, c_group, c_order, c_depth, u_idx) values(?,?,?,?,?,?,?,?)";
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setInt(1, comment.getC_idx());
//				pstmt.setString(2, comment.getC_content());
//				pstmt.setString(3, comment.getC_date());
//				pstmt.setString(4, comment.getC_writer());
//				pstmt.setInt(5, comment.getC_group());
//				if(comment.getC_group()==0) {
//					pstmt.setInt(6, comment.getC_order());
//					pstmt.setInt(7, comment.getC_depth());
//				}else { 
//					comment.setC_order(comment.getC_order()+1);
//					comment.setC_depth(comment.getC_depth()+1);
//					pstmt.setInt(6, comment.getC_order());
//					pstmt.setInt(7, comment.getC_depth());
//				}
//				pstmt.setInt(8, comment.getU_idx());
//				pstmt.executeUpdate();
//				pstmt.close();
//				if(comment.getC_group()==0) {
//					sql = "update comment set c_group = last_insert_id() where c_idx = last_insert_id()";
//					pstmt = conn.prepareStatement(sql);
//					pstmt.executeUpdate();
//				}else {
//					sql = "update comment set c_order = c_order+1 where c_group = ? and c_order >= ? and c_idx <> last_insert_id()";
//					pstmt = conn.prepareStatement(sql);
//					pstmt.setInt(1, comment.getC_group());
//					pstmt.setInt(2, comment.getC_order());
//					pstmt.executeUpdate();
//				}
//			}catch(Exception ex) {
//				System.out.println("SQLException :"+ ex.getMessage());
//			}finally {
//				try {
//					if(pstmt != null) {
//						pstmt.close();
//					}
//					if(conn != null) {
//						conn.close();
//					}
//					
//				}catch(SQLException e) {
//					e.printStackTrace();
//				}
//			}
//	
//	}

}