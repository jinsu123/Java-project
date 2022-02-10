<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    


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