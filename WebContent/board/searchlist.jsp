<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
</head>
<style>
	table{
		border-collapse:collapse;
		margin:40px auto;
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
	ul{
		width:600px;
		height:50px;
		margin:10px auto;
	}
	li{
		list-style:none;
		width:50px;
		line-height:50px;
		border:1px solid #ededed;
		float:left;
		text-align:center;
		margin:0 5px;
		border-radius:5px;
	}
	h1{
		text-align:center;
	}
	a{
		text-decoration:none;
		color:#000;
		font-weight:700;
	}
</style>
<body>
	<h1>게시판</h1>
	<table>
		<tr>
			<td colspan="6">전체 게시글 수 : ${Bpagination.boardCount}</td>
		<tr>
			<th>번호</th>
			<th style="width:10%">제목</th>
			<th style="width:40%">내용</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
		<c:forEach items="${searchlist}" var="item" varStatus="status">
			<tr>
				<td>${item.b_idx}</td>
				<td><a href="/lcomputerstudy/board-detail.do?b_idx=${item.b_idx}">${item.b_title}</a></td>
				<td>${item.b_content}</td>
				<td>${item.b_writer}</td>
				<td>${item.b_date}</td>
				<td>${item.b_view}</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<hr>
	<p><a href="/lcomputerstudy/board-insert.do">[글쓰기] &nbsp;&nbsp;&nbsp;</a></p>
	<div class="container">
		<div class="row">
			<form method="post" name="serach" action="board-searchlist.do">
				<table class="pull-right">
					<tr>
						<td><select class="form-contral" name="searchField">
							<option value="0">선택</option>
							<option value="b_title">제목</option>
							<option value="b_content">내용</option>
							<option value="b_content">제목+내용</option>
							<option value="b_writer">작성자</option>
							<option value="b_date">작성일</option>
						</select></td>
						<td><input type="text" class="form-control"
							placeholder="검색어 입력" name="searchText" maxlength="100"></td>
						<td><button type="submit" class="btn btn-success">검색</button></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
	<div>
		<ul>
			<c:choose>
				<c:when test="${Bpagination.prevPage >= 5 }">
					<li>
						<a href="board-list.do?page=${Bpagination.prevPage}">
							◀
						</a>
					</li>
				</c:when>	
			</c:choose>
			<c:forEach var="i" begin="${Bpagination.startPage}" end="${Bpagination.endPage}" step="1">
				<c:choose>
					<c:when test="${Bpagination.page == i }">
						<li style="background-color:#ededed;">	
							<span>${i}</span>
						</li>
					</c:when>
					<c:when test="${Bpagination.page != i}">
						<li>
							<a href="board-list.do?page=${i}">${i}</a>
						</li>
					</c:when>
				</c:choose>
			</c:forEach>
			<c:choose>
				<c:when test="${Bpagination.nextPage <= Bpagination.lastPage}">
					<li style="">
						<a href="board-list.do?page=${Bpagination.nextPage}">▶</a>
					</li>
				</c:when>
			</c:choose>
		</ul>
	</div>
</body>
</html>