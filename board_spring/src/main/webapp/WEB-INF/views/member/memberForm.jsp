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
<title>회원가입</title>
<c:choose>
	<c:when test="${ param.result == 'failed' }">
		<script type="text/javascript">
			window.onload = function() {
				alert('동일한 아이디가 존재합니다!');
			}
		</script>
	</c:when>
	<c:when test="${ param.result == 'succeed' }">
		<script type="text/javascript">
			window.onload = function() {
				alert('가입 성공');
			}
		</script>
	</c:when>
</c:choose>
</head>
<body>
	<h1 align="center">회원 가입</h1>
	<form method="post" action="${ contextPath }/member/addMember.do">
		<table align="center">
			<tr>
				<td width="200" align="right">아이디</td>
				<td width="400"><input type="text" name="id" /></td>
			</tr>
			<tr>
				<td width="200" align="right">비밀번호</td>
				<td width="400"><input type="text" name="pwd" /></td>
			</tr>
			<tr>
				<td width="200" align="right">이름</td>
				<td width="400"><input type="text" name="name" /></td>
			</tr>
			<tr>
				<td width="200" align="right">이메일</td>
				<td width="400"><input type="text" name="email" /></td>
			</tr>
			<tr align="center">
				<td colspan="2"><input type="submit" value="가입하기" /></td>
			</tr>
		</table>
	</form>
</body>
</html>