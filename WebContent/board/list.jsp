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
		text-align:left;
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

	tdAlign{
		text-align:left;
		padding-left:200px;
	}

	
</style>
<body>
	<h1>게시판</h1>
	
	<c:if test="${sessionScope.user.u_name == null}">
	<p style="text-align:left"><a href="user-login.do">&nbsp;&nbsp;&nbsp;&nbsp;로그인</a></p>
	</c:if>
	<c:if test= "${sessionScope.user.u_name != null}">
	<p style="text-align:left"><a href="logout.do">&nbsp;&nbsp;&nbsp;&nbsp;로그아웃</a></p>
	</c:if>



	<table>
		<tr>
			<td colspan="6">전체 게시글 수 : ${pagination.count}</td>
		<tr>
			<th>번호</th>
			<th style="width:10%">제목</th>
			<th style="width:40%">내용</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
		<c:forEach items="${list}" var="item" varStatus="status">
			<tr>
				<td>${item.rownum}</td>
				<td class="tdAlign"><c:if test="${item.b_depth > 0 }">
						<c:forEach begin="1" end="${item.b_depth}">
							&nbsp;&nbsp;
						</c:forEach>
						<c:forEach begin="1" end="${item.b_depth}">
							RE :
						</c:forEach>
					</c:if>
					<a href="/lcomputerstudy/board-detail.do?b_idx=${item.b_idx}">${item.b_title}</a>
				</td>
				<td>${item.b_content}</td>
				<td>${item.user.u_name}</td>
				<td>${item.b_date}</td>
				<td>${item.b_view}</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<hr>
	<c:if test="${sessionScope.user.u_name != null}">
	<p><a href="/lcomputerstudy/board-insert.do?b_group=0&b_order=0&b_depth=0">[글쓰기] &nbsp;&nbsp;&nbsp;</a></p>
	</c:if>
	<div id="search">
        <form action="board-list.do" name="search" method="get">
        	<input type="hidden" name="page" value="1">
            <select name="type">
            	<option value="0" selected>- 선택- </option>
                <option value="1">제목</option>
                <option value="2">내용</option>
                <option value="3">제목+내용</option>
                <option value="4">작성자</option>
            </select>
            <input type="text" size="20" name="keyword"/>&nbsp;
            <input type="submit" value="검색"/>
        </form>    
	</div>
	
	<div>
		<ul>
			<c:choose>
				<c:when test="${pagination.prevPage >= 5 }">
					<li>
						<a href="board-list.do?page=${pagination.prevPage}">
							◀
						</a>
					</li>
				</c:when>	
			</c:choose>
			<c:forEach var="i" begin="${pagination.startPage}" end="${pagination.endPage}" step="1">
				<c:choose>
					<c:when test="${pagination.page == i }">
						<li style="background-color:#ededed;">	
							<span>${i}</span>
						</li>
					</c:when>
					<c:when test="${pagination.page != i}">
						<li>
							<a href="board-list.do?page=${i}&type=${pagination.search.type}&keyword=${pagination.search.keyword}">${i}</a>
						</li>
					</c:when>
				</c:choose>
			</c:forEach>
			<c:choose>
				<c:when test="${pagination.nextPage <= pagination.lastPage}">
					<li style="">
						<a href="board-list.do?page=${pagination.nextPage}">▶</a>
					</li>
				</c:when>
			</c:choose>
		</ul>
	</div>
</body>
</html>