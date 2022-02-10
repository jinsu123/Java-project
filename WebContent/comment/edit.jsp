<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>댓글 수정</title>
</head>
<body>
<h2>댓글수정</h2>
	<form action="comment-edit-process.do" name="comment" method="POST" >
		<input type="hidden" name="c_idx" value="${comment.c_idx}">
		<p> 작성자 : ${sessionScope.user.u_name}</p>
		<p> 내용 : <input type="text" size="40" maxlength="100" name="c_content" value="${comment.c_content }" ></p>
		<button type="submit">수정완료</button>
	</form>
</body>
</html>





