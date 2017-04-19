<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/boardAdd" method="post">
		<div>
			제목 : <input type="text" name="boardTitle"/>
		</div>
		<div>
			ID : <input type="text" name="boardWriter"/>
		</div>
		<div>
			패스워드 : <input type="password" name="boardPw"/>
		</div>
		<div>
			내용 : <textarea rows="5" cols="50" name="boardContent"></textarea>
		</div>
		<input type="submit" value="입력"/>
		<input type="reset" value="취소"/>
	</form>
</body>
</html>