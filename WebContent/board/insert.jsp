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
	<form action="board-insert-process.do" name="board" method="post" enctype="multipart/form-data">
		<input type="hidden" name="b_group" value="${board.b_group}">
		<input type="hidden" name="b_order" value="${board.b_order}">
		<input type="hidden" name="b_depth" value="${board.b_depth}">
		<input type="hidden" name="u_idx" value="${board.u_idx}">
		<p> 작성자 : <input type="text" name="writer" value="${user.getU_name()}" readonly>
		<p> 제   목 : <input type="text" maxlength="100" size="40" name="title">
		<p> <div style="text-align:middle">내   용 :</div><textarea  style="overflow:auto; height:150px; resize:none;"  cols="100	" rows="100" name="content"></textarea>
		<p>파일: <input type="file"  name="upload1"></p>
		<p>파일: <input type="file"  name="upload2"></p>
		<hr width="40%" align="left">
		<p> <input type="submit" value="등록"><input type="button" onclick="location.href='/lcomputerstudy/board-insert.do?b_group=0&b_order=0&b_depth=0'" value="다시쓰기"><input type="button" onclick="location.href='/lcomputerstudy/board-list.do'" value="리스트">
	</form>	
</body>
</html>