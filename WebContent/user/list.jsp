<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원목록2</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>
<style>
	table {
		border-collapse:collapse;
		margin:40px auto;
	}
	table tr th {
		font-weight:700;
	}
	table tr td, table tr th {
		border:1px solid #818181;
		width:200px;
		text-align:center;
	}
	a {
		text-decoration:none;
		color:#000;
		font-weight:700;
	}
	 h1 {
		text-align:center;
	}
	ul {
		width:400px;
		height:50px;
		margin:10px auto;
	}
	li {
		list-style:none;
		width:50px;
		line-height:50px;
		border:1px solid #ededed;
		float:left;
		text-align:center;
		margin:0 5px;
		border-radius:5px;
	}
</style>
<body>
<h1>회원 목록</h1>
<form action="update-auth.do" method="POST" id="form_auth">
	<input type="hidden" name="auth" value="${item.u_auth}" id="auth">
	<input type="hidden" name="u_idx" value="" id="uIdx">
	<table>
		<tr>
			<td colspan="4">전체 회원 수 : ${pagination.count}</td>
		<tr>
			<th>No</th>
			<th>ID</th>
			<th>이름</th>
			<th>권한</th>
		</tr>
		<c:forEach items="${list}" var="item" varStatus="status">
			 <tr>
				<td><a href="userDetail.jsp?u_idx=${item.u_idx}">${item.u_idx}</a></td>
				<td>${item.u_id}</td>
				<td>${item.u_name}</td>
				<td>
					<button class="btn_auth_user" uidx="${item.u_idx}" type="button" ${item.u_auth == 'user' ? 'disabled' : ''}>사용자</button>
					<button class="btn_auth_admin" uidx="${item.u_idx}" type="button" ${item.u_auth == 'admin' ? 'disabled' : ''}>관리자</button>
				</td>
		     <tr>
		</c:forEach>
	</table>
</form>
<!-- 아래부터 pagination -->
	<div>
		<ul>
			 <c:choose>
				<c:when test="${ pagination.prevPage >= 5}">
					<li>
						<a href="user-list.do?page=${pagination.prevPage}">
							◀
						</a>
					</li>
				</c:when>
			</c:choose> 
			<c:forEach var="i" begin="${pagination.startPage}" end="${pagination.endPage}" step="1">
				
					<c:choose>
						<c:when test="${ pagination.page == i }"> 
							
							<li style="background-color:#ededed;">
								<span>${i}</span>
							</li>
						</c:when>
						<c:when test="${ pagination.page != i }">
							<li>
								<a href="user-list.do?page=${i}">${i}</a>
							</li>
						</c:when>
					</c:choose>
			</c:forEach>
			 <c:choose>
				<c:when test="${ pagination.nextPage < pagination.lastPage }">
					<li style="">
						<a href="user-list.do?page=${pagination.nextPage}">▶</a>
					</li>
				</c:when>
			</c:choose> 
		</ul>
	</div>
<script>
$(document).on('click', '.btn_auth_user', function () {
	console.log('clicked user');
	$('#auth').val('user');
	console.log("user log:");
	console.log($('#auth').val('user'));
	let uidx = $(this).attr('uidx');
	$('#uIdx').val(uidx);
	console.log("uidx log:");
	console.log(uidx);
	
	$('#form_auth').submit();
});

$(document).on('click', '.btn_auth_admin', function () {
	console.log('clicked admin');
	$('#auth').val('admin');
	let uidx = $(this).attr('uidx');
	$('#uIdx').val(uidx);
	
	$('#form_auth').submit();
});
</script>
</body>
</html>
