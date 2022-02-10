<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>

	
</head>
<body>
	<h2 style="background-color:#b0e0e6; width:40%; text-align:center">글쓰기</h2>
	<form action="join_process_board.jsp" name="board" method="post">
		<p> 작성자 : <input type="text" name="writer">
		<p> 제   목 : <input type="text" maxlength="100" size="40" name="title">
		<p> <div style="text-align:middle">내   용 :</div><textarea  style="overflow:auto; height:150px; resize:none;"  cols="100	" rows="100" name="content"></textarea>
		<p> 비밀번호 : <input type="text">
		<p> 첨부파일 : <input type="file" name="file"><p>
		<hr width="40%" align="left">
		<p> <input type="submit" value="등록"><input type="button" onclick="location.href='newtext.jsp'" value="다시쓰기"><input type="button" onclick="location.href='board.jsp'" value="리스트">
	</form>	
</body>
</html>