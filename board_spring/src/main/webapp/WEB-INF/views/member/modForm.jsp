<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${ pageContext.request.contextPath }" />
<%
	request.setCharacterEncoding("utf-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정</title>
</head>
<body>
	<h1 align="center">회원 정보 수정</h1>
	<form method="post" action="${ contextPath }/member/modMember.do?id=${ member.id }">
		<table align="center">
		<tr>
			<td width="200" align="right">아이디</td>
			<td width="400"><input type="text" value="${ member.id }" disabled></td>
		</tr>
		<tr>
			<td width="200" align="right">비밀번호</td>
			<td width="400"><input type="text" name="pwd" value="${ member.pwd }" ></td>
		</tr>
		<tr>
			<td width="200" align="right">이름</td>
			<td width="400"><input type="text" name="name" value="${ member.name }"></td>
		</tr>
		<tr>
			<td width="200" align="right">이메일</td>
			<td width="400"><input type="text" name="email" value="${ member.email }" ></td>
		</tr>
		<tr align="center">
			<td colspan="2"><input type="submit" value="수정하기" /></td>
		</tr>
	</table>
	</form>
	
</body>
</html>