<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/boardRemove" method="post">
	<div>BOARD_NO</div>
	<div><input type="text" name="boardNo" value="${boardNo}" readonly/></div>
	<div>BOARD_WRITER</div>
	<div><input type="text" name="boardwriter" value="${writer}" readonly/></div>
	<div>BOARD_PASSWORD</div>
	<div><input type="password" name="boardPw"/></div>
	<div>
		<input type="submit" value="삭제하기"/>
		<input type="reset" value="취소"/>
	</div>
	</form>
	<div>
		<a href="${pageContext.request.contextPath}/boardList">리스트로 돌아가기</a>
	</div>
</body>
</html>