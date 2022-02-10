<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>댓글</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
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
		text-align:center;
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
</style>
</head>
<body>

<h2>댓글쓰기</h2>
<form action="comment-insert.do"  method="POST" id="form_comment">
	<p> 작성자 : ${sessionScope.user.u_name}</p>
	<p> 내용 : <input type="text" size="40" maxlength="100" name="c_content" id="content" ></p>
	<button type="submit">댓글쓰기</button>
</form>
	<br>
	<div id="aj_list">
		<table>
			<tr>
				<td colspan="3">전체 댓글 수 : ${pagination.count}</td>
			<tr>
			<tr>
				<th>작성자</th>
				<th style="width:50%">내용</th>
				<th style="width:35%">작성일</th>
			</tr>
			<c:forEach items="${list}" var="item" varStatus="status">
				<tr>
					<th>${item.user.u_name }</th>
					<th>${item.c_content }</th>
					<th>${item.c_date }  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button type="button" class="replyForm" >댓글쓰기</button>
						<button type="button"><a href ="/lcomputerstudy/comment-edit.do?c_idx=${item.c_idx}">수정</a></button>
						<button type="button"><a href ="/lcomputerstudy/comment-delete.do?c_idx=${item.c_idx}">삭제</a></button>
					
					</th>
				</tr>
				<tr style="display: none;">
					<td colspan="2">
						<textarea cols="100" rows="1"></textarea>
					</td>
					<td>
						<button type="button" class="insertForm" c_idx="${item.c_idx }" c_group="${item.c_group }" c_order="${item.c_order }" c_depth="${item.c_depth }">등록</button>
						<button type="button">취소</button>
					</td>
				</tr>
			</c:forEach>
		</table>
	
		<div>
			<ul>
				<c:choose>
					<c:when test="${pagination.prevPage >= 5 }">
						<li>
							<a href="comment-list.do?page=${pagination.prevPage}">
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
								<a href="comment-list.do?page=${i}&type=${pagination.search.type}&keyword=${pagination.search.keyword}">${i}</a>
							</li>
						</c:when>
					</c:choose>
				</c:forEach>
				<c:choose>
					<c:when test="${pagination.nextPage <= pagination.lastPage}">
						<li style="">
							<a href="comment-list.do?page=${pagination.nextPage}">▶</a>
						</li>
					</c:when>
				</c:choose>
			</ul>
		</div>
	</div>
<script>
$(document).on('click', '.replyForm', function () {
	console.log('asdfsadf')
	$(this).parent().parent().next().show();
});

$(document).on('click', '.insertForm', function () {

	console.log('test')
	let id = '${sessionScope.user.u_name}'
	console.log('id')
	console.log(id)
	let c_content = $(this).parent().parent().find('textarea').val();
	console.log('c_content')
	console.log(c_content)
	let c_group = $(this).attr('c_group');
	console.log('c_group')
	console.log(c_group)
	let c_order = $(this).attr('c_order');
	console.log('c_order')
	console.log(c_order)
	let c_depth = $(this).attr('c_depth');
	console.log('c_depth')
	console.log(c_depth)

	$.ajax({
		method: "POST",
		url: "comment-insert.do",
		data: { id: id, c_content: c_content,  c_group: c_group, c_order: c_order, c_depth: c_depth }
	
	})
    .done(function( html ) {
        console.log(html)
    	 $('#aj_list').html(html);
    });
});
</script>
</body>
</html>