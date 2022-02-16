<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 업로드</title>
</head>
<body>
<form action="fileUpload-process.do" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
	<input type="hidden" name="b_group" value="${board.b_group}">
	<input type="hidden" name="b_order" value="${board.b_order}">
	<input type="hidden" name="b_depth" value="${board.b_depth}">
	<p> 이름: ${sessionScope.user.u_name }    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </p>
	<p>파일: <input type="file"  name="upload1"></p>
	<p>파일: <input type="file"  name="upload2"></p>
	<input type="submit" value="전송">
</form>
</body>
</html>