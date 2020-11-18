<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${ pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상단부</title>
</head>
<body>
	<table border="0" width="100%">
		<tr>
			<td><h1><font size="30">스프링실습 홈페이지!!</font></h1></td>
			<td>
				<c:choose>
					<c:when test="${ isLogOn == true && member != null }">
						<h3>환영합니다. ${ member.name }님!!!</h3>
						<a href="${ contextPath }/member/logout.do">로그아웃</a>
					</c:when>
					<c:otherwise>
						<a href="${ contextPath }/member/loginForm.do">로그인</a>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
</body>
</html>