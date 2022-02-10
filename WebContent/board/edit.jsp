<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
</head>
<body>
<h2 style="background-color:#b0e0e6; width:40%; text-align:center">글쓰기</h2>
	<form action="board-edit-process.do" name="board" method="post">
		<input type="hidden" name="b_idx" value="${board.b_idx}">
		<p> 작성자 : <input type="text" name="writer" value="${board.b_writer}">
		<p> 제   목 : <input type="text" maxlength="100" size="40" name="title" value="${board.b_title}">
		<p> <div style="text-align:middle">내   용 :</div><textarea  style="overflow:auto; height:150px; resize:none;"  cols="100	" rows="100" name="content"> ${board.b_content}</textarea>
		<p> 비밀번호 : <input type="text">
		<p> 첨부파일 : <input type="file" name="file"><p>
		<hr width="40%" align="left">
		<p> <input type="submit" value="수정완료">
	</form>
</body>
</html>