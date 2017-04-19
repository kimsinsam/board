<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BOARD LIST</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<script>
	$(document).ready(function(){
		var currentPage = ${currentPage};
		console.log(currentPage);
		$('#currentPage_'+currentPage).attr('style','color : red; font-size:25px;');
	});
</script>
</head>
<body>
	<div>전체행의 수 : ${totalRowCount}</div>
	<table border="1">
		<thead>
			<td>BOARD NO</td>
			<td>BOARD TITLE</td>
			<td>BOARD WRITER</td>
			<td>BOARD DATE</td>
		</thead>
		<tbody>
			<c:forEach var="b" items="${board}">
				<tr>
					<td><a href="${pageContext.request.contextPath}/boardView?boardNo=${b.boardNo}">${b.boardNo}</a></td>
					<td>${b.boardTitle}(${b.boardCommentcount})</td>
					<td>${b.boardWriter}</td>
					<td>${b.boardDate}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div>
		<a href="${pageContext.request.contextPath}/boardAdd.do">게시글 입력</a>
	</div>
	<div>
		<form action="${pageContext.request.contextPath}/boardList" method="get">
		<select name="selcet">
			<option value="board_writer">작성자</option>
			<option value="board_title">제목</option>
		</select>
		<input type="text" name="selecttext"/>
		<input type="submit" value="검색"/>
		</form>
	</div>
	<div>
		<form action="${pageContext.request.contextPath}/boardList?pagePerRow" method="get">
		<select name="pagePerRow">
			<option value="3">3줄 씩 보기</option>
			<option value="5">5줄 씩 보기</option>
			<option value="10">10줄 씩 보기</option>
		</select>
		<input type="submit" vlaue="줄 바꾸기"/>
		</form>
	</div>
	<div>
		<c:if test="${currentPage > 1}">
			<a href="${pageContext.request.contextPath}/boardList?currentPage=${forPage*10}">이전</a>
		</c:if>
		<c:forEach var="p" items="${pageNo}">
			<a id="currentPage_${p+(forPage*10)}" href="${pageContext.request.contextPath}/boardList?currentPage=${p+(forPage*10)}">${p+(forPage*10)}</a>
		</c:forEach>
		<c:if test="${(currentPage-1)/10 < (lastPage-10)/10}">
			<a href="${pageContext.request.contextPath}/boardList?currentPage=${((forPage+1)*10)+1}">다음</a>
		</c:if>	
	</div>
</body>
</html>