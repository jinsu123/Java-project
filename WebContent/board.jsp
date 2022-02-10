<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
</head>
<style>
	table{
		border-collapse:collapse;
		
	}
	table tr th{
		font-weight:700;
	}
	table tr td, table tr th{
		width:200px;
		text-align:center;
	}
	p{
		text-align:right;
	}
	th{
		background-color:#b0e0e6;
	}
	th-2{
		width:1000px;
	}
</style>
<body>
<%@ include file="db_connection.jsp" %>
	<h1>게시판</h1>
	<table>
		<tr>
			<th>번호</th>
			<th style="width:40%">제목</th>
			<th>작성자</th>
			<th>작성일시</th>
			<th>조회수</th>
		</tr>
		<%
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			String query = "select * from board";
			pstmt = conn.prepareStatement(query);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				String b_idx = rs.getString("b_idx");
				String b_title = rs.getString("b_title");
				String b_writer = rs.getString("b_writer");
				String b_date = rs.getString("b_date");
				String b_view = rs.getString("b_view");
		%>
		<tr>
			<td><%=b_idx %></td>
			<td><a href="boardDetail.jsp?b_title=<%=b_title%>"><%=b_title %></a></td>
			<td><%=b_writer %></td>
			<td><%=b_date %></td>
			<td><%=b_view %></td>
		<%
			}
			rs.close();
			pstmt.close();
			conn.close();
		%>
	</table>
	<br>
	<hr>
	<p><a href="newtext.jsp">[글쓰기] &nbsp;&nbsp;&nbsp;</a></p>
</body>
</html>