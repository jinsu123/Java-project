<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세</title>
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

<h2>게시글</h2>
<table>
	<tr>
		<td>번호</td>
		<td>${board.b_idx}</td>
	</tr>
	<tr>
		<td>제목</td>
		<td>${board.b_title}</td>
	</tr>
	<tr>
		<td>내용</td>
		<td>${board.b_content}</td>
	</tr>
	<tr>
		<td>작성자</td>
		<td>${board.user.u_name}</td>
	</tr>
	<tr>
		<td>날짜</td>
		<td>${board.b_date}</td>
	</tr>
	<tr>
		<td>조회수</td>
		<td>${board.b_view}</td>
	</tr>
	<c:if test="${sessionScope.user.u_name == board.user.u_name }">
	<tr style="height:50px;">
		<td style="border:none;">
			<p style="width:70%; font-weight:700;background-color:#818181;color:#fff;"><a href="/lcomputerstudy/board-edit.do?b_idx=${board.b_idx}">수정</a></p>
		</td>
		<td style="border:none;">
			<p style="width:70%; font-weight:700;background-color:red;color:#fff;"><a href="/lcomputerstudy/board-delete.do?b_idx=${board.b_idx}">삭제</a></p>
		</td>
	</tr>
	</c:if>
</table>
<c:if test="${sessionScope.user.u_name != null}">
<p style="width:4%; font-weight:700;background-color:#818181;color:#fff;"><a href="/lcomputerstudy/board-insert.do?b_group=${board.b_group}&b_order=${board.b_order}&b_depth=${board.b_depth}">답글쓰기</a></p>
</c:if>
<br>
<a href="board-list.do">리스트로 돌아가기</a>
<br>
	<p>댓글쓰기</p>
	
	
	<p> 작성자 : ${sessionScope.user.u_name}</p>
	<p> 내용 : <input type="text" size="40" maxlength="100" name="c_content" id="content" ></p>
	<button type="button" id="insertForm" u_idx="${sessionScope.user.u_name}"  c_group="0" c_order="0" c_depth="0">댓글쓰기</button>

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
					<th class="tdAlign"><c:if test="${item.c_depth > 0 }">
						<c:forEach begin="1" end="${item.c_depth}">
							&nbsp;&nbsp;
						</c:forEach>
						<c:forEach begin="1" end="${item.c_depth}">
							RE :
						</c:forEach>
					</c:if>
					${item.c_content }</th>
					<th>${item.c_date }  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button type="button" class="replyForm" >댓글쓰기</button>
						<button type="button" id="editForm">수정</button>
						<button type="button" id="deleteForm" c_idx="${item.c_idx }">삭제</button>
					
					</th>
				</tr>
				<tr style="display: none;">
					<td colspan="2">
						<textarea cols="100" rows="1"></textarea>
					</td>
					<td>
						<button type="button" class="reInsertForm" c_idx="${item.c_idx }" c_group="${item.c_group }" c_order="${item.c_order }" c_depth="${item.c_depth }">등록</button>
						<button type="button" class="cancelForm">취소</button>
					</td>
				</tr>
				<tr style="display: none;">
					<td colspan="2">
						<textarea cols="100" rows="1">${item.c_content }</textarea>
					</td>
					<td>
						<button type="button" class="reEditForm" c_idx="${item.c_idx }" >수정</button>
						<button type="button" class="cancelForm">취소</button>
					</td>
				</tr>
			</c:forEach>
		</table>
	
		<div>
			<ul>
				<c:choose>
					<c:when test="${pagination.prevPage >= 5 }">
						<li>
							<a class="page" page="${pagination.prevPage}">
								◀
							</a>
						</li>
					</c:when>	
				</c:choose>
				<c:forEach var="i" begin="${pagination.startPage}" end="${pagination.endPage}" step="1">
					<c:choose>
						<c:when test="${pagination.page == i }">
							<li style="background-color:#ededed;">	
								<a class="page">${i}</a>
							</li>
						</c:when>
						<c:when test="${pagination.page != i}">
							<li>
								<a class="page" page="${i}" >${i}</a>
							</li>
						</c:when>
					</c:choose>
				</c:forEach>
				<c:choose>
					<c:when test="${pagination.nextPage <= pagination.lastPage}">
						<li style="">
							<a class="page" page="${pagination.nextPage}">▶</a>
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


$(document).on('click', '.cancelForm', function () {
	console.log('cancelForm')
	$(this).parent().parent().hide();
});



$(document).on('click', '#insertForm', function () {

	console.log('test')
	
	let b_idx = '${board.b_idx}'
	console.log('b_idx')
	console.log(b_idx)
	
	
	let count = '${pagination.count}'
	console.log('count')
	console.log(count)

	let id = '${sessionScope.user.u_name}'
	console.log('id')
	console.log(id)

	let c_content = $(this).prev().find('input').val();
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
		data: { b_idx: b_idx, count:count,  id: id, c_content: c_content,  c_group: c_group, c_order: c_order, c_depth: c_depth }
	
	})
    .done(function( html ) {
        console.log(html)
    	 $('#aj_list').html(html);
    });
});



$(document).on('click', '.reInsertForm', function () {


	let b_idx = '${board.b_idx}'
	console.log('b_idx')
	console.log(b_idx)
	
	console.log('test')
	let id = '${sessionScope.user.u_name}'
	console.log('id')
	console.log(id)
	let c_idx =  $(this).attr('c_idx');
	console.log('c_idx')
	console.log(c_idx)
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
		data: { b_idx: b_idx, id: id, c_idx: c_idx, c_content: c_content,  c_group: c_group, c_order: c_order, c_depth: c_depth }
	
	})
    .done(function( html ) {
        console.log(html)
    	 $('#aj_list').html(html);
    });
});




$(document).on('click', '#deleteForm', function () {
	console.log('deleteForm')

	let b_idx = '${board.b_idx}'
	console.log('b_idx')
	console.log(b_idx)
	
	let c_idx = $(this).attr('c_idx');
	console.log('c_idx')
	console.log(c_idx)
	
	$.ajax({
		method: "POST",
		url: "comment-delete.do",
		data: {b_idx: b_idx, c_idx: c_idx }
	
	})
    .done(function( html ) {
        console.log(html)
    	 $('#aj_list').html(html);
    });
	
});



$(document).on('click', '#editForm', function () {
	console.log('editForm')
	$(this).parent().parent().next().next().show();
});

$(document).on('click', '.reEditForm', function () {
	console.log('reEditForm')

	let b_idx = '${board.b_idx}'
	console.log('b_idx')
	console.log(b_idx)
	
	
	let c_idx = $(this).attr('c_idx');
	console.log('c_idx')
	console.log(c_idx)
	let id = '${sessionScope.user.u_name}'
	console.log('id')
	console.log(id)

	let c_content = $(this).parent().parent().find('textarea').val();
	console.log('c_content')
	console.log(c_content)
	
	
	
	
	$.ajax({
		method: "POST",
		url: "comment-edit.do",
		data: { b_idx:b_idx, c_idx: c_idx, id: id, c_content: c_content}
	
	})
    .done(function( html ) {
        console.log(html)
    	 $('#aj_list').html(html);
    });
	
});


$(document).on('click', '.page', function () {
	console.log('page')

	
	let b_idx = '${board.b_idx}'
	console.log('b_idx')
	console.log(b_idx)
	let c_idx = '${sessionScope.user.u_name}'
	console.log('c_idx')
	console.log(c_idx)
	let page = $(this).attr('page');
	console.log('page')
	console.log(page)

	
	
	$.ajax({
		method: "GET",
		url: "comment-list.do",
		data: {b_idx: b_idx, c_idx: c_idx, page: page }
	
	})
    .done(function( html ) {
        console.log(html)
    	 $('#aj_list').html(html);
    });
	
});

</script>
</body>
</html>