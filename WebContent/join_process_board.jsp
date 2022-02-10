<%@page import="jdk.internal.misc.FileSystemOption"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	Date nowTime = new Date();
	SimpleDateFormat sf = new SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm:ss");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 등록 값 넘겨받기</title>
</head>
<body>
<%@ include file ="db_connection.jsp" %>
<%
	request.setCharacterEncoding("UTF-8");
	String writer = request.getParameter("writer");
	String title = request.getParameter("title");
	String content = request.getParameter("content");
	String date = request.getParameter("date");
	
	PreparedStatement pstmt = null;
	
	try{
		String sql = "insert into board(b_writer, b_title, b_content, b_date) values(?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,writer);
		pstmt.setString(2,title);
		pstmt.setString(3,content);
		pstmt.setString(4,date);
		pstmt.executeUpdate();
	}catch(SQLException ex){
		System.out.println("SQLException : "+ex.getMessage());
	}finally{
		if(pstmt != null){
			pstmt.close();
		}
		if(conn != null){
			conn.close();
		}
	}
%>


<h3>저장완료</h3>
<a href="board.jsp">리스트로 돌아가기</a>
</body>
</html>