<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세</title>
</head>
<style>
	table{
		border-collapse:collapse;
	}
		table tr th{
		font-weight:700;
	}
	table tr td, table tr th{
		border:1px solid #818181;
		width:200px;
		text-align:center;
	}
	p{
		text-decoration:none;
		color:#000;
		font-weight:700;
		border:none;
		cursor:pointer;
		padding:10px;
		display:inline-block;
	}
</style>

<body>
<%@ include file="db_connection.jsp" %>
<h2>게시글</h2>
<table>
	<%
			String title = request.getParameter("b_idx");
		
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			String query = "select * from board where b_idx=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,title);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				String b_idx = rs.getString("b_idx");
				String b_title = rs.getString("b_title");
				String b_content = rs.getString("b_content");
				String b_writer = rs.getString("b_writer");
				String b_date = rs.getString("b_date");
				String b_view = rs.getString("b_view");
	%>
	<tr>
		<td>번호</td>
		<td><%=b_idx %></td>
	</tr>
	<tr>
		<td>제목</td>
		<td><%=b_title %></td>
	</tr>
	<tr>
		<td>내용</td>
		<td><%=b_content %></td>
	</tr>
	<tr>
		<td>작성자</td>
		<td><%=b_writer %></td>
	</tr>
	<tr>
		<td>날짜</td>
		<td><%=b_date %></td>
	</tr>
	<tr>
		<td>조회수</td>
		<td><%=b_view %></td>
	</tr>
	<tr style="height:100px;">
		<td style="border:none;">
			<p style="width:70%; font-weight:700;background-color:#818181;color:#fff;">수정</p>
		</td>
		<td style="border:none;">
			<p style="width:70%; font-weight:700;background-color:red;color:#fff;">삭제</p>
		</td>
	</tr>
	<%
		}
	%>
	
	
</table>
</body>
</html>