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
<title>회원 정보 목록</title>
</head>
<body>
	<table align="center" width="80%" border="1" style=" border-collapse: collapse;">
		<tr bgcolor="lightgreen" align="center">
			<td><b>아이디</b></td>
			<td><b>비밀번호</b></td>
			<td><b>이름</b></td>
			<td><b>이메일</b></td>
			<td><b>가입일</b></td>
			<td><b>수정하기</b></td>
			<td><b>삭제하기</b></td>
		</tr>
		<c:forEach var="member" items="${ membersList }">
			<tr align="center">
				<td>${ member.id }</td>
				<td>${ member.pwd }</td>
				<td>${ member.name }</td>
				<td>${ member.email }</td>
				<td><fmt:formatDate pattern="yyyy년 MM월 dd일" value="${ member.joinDate }"/></td>
				<td><a href="${ contextPath }/member/modForm.do?id=${ member.id }" />수정</td>
				<td><a href="${ contextPath }/member/delMember.do?id=${ member.id }" />삭제</td>
			</tr>
		</c:forEach>
	</table>
	<h1 align="center"><a href="${ contextPath }/member/memberForm.do">회원가입하기</a></h1>
	<br><br>
</body>
</html>