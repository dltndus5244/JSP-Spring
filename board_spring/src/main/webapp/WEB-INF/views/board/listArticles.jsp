<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${ pageContext.request.contextPath }" />
<c:set var="articlesList" value="${ articlesMap.articlesList }" />
<c:set var="totArticle" value="${ articlesMap.totArticle }" />
<c:set var="section" value="${ articlesMap.section }" />
<c:set var="pageNum" value="${ articlesMap.pageNum }" />
<c:set var="lastSection" value="${ articlesMap.lastSection }" />
<%
	request.setCharacterEncoding("utf-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록창</title>
<script type="text/javascript">
	function fn_articleForm(isLogOn, articleForm, loginForm) {
		if (isLogOn != '' && isLogOn == 'true') {
			location.href=articleForm; //로그인 상태이면 글쓰기창으로 이동합니다.
		} else {
			//로그아웃 상태이면 action 값으로 다음에 수행할 URL인 /board/articleForm.do를 전달하면서 로그인창으로 이동합니다.
			alert('로그인 후 글쓰기가 가능합니다.');
			location.href=loginForm + '?action=/board/articleForm.do';
		}
	}
</script>
</head>
<body>
	<table align="center" width="60%" border="1" style=" border-collapse: collapse;">
		<tr bgcolor="lightgreen" align="center">
			<td><b>글번호</b></td>
			<td><b>작성자</b></td>
			<td><b>제목</b></td>
			<td><b>작성일</b></td>
			<td><b>조회수</b></td>
		</tr>
		<c:forEach var="article" items="${ articlesList }">
			<tr align="center">
				<td width="5%">${ article.articleNO }</td>
				<td width="10%">${ article.id }</td>
				<td align="left" width="35%">
					<span style="padding-left:10px"></span>  
					<c:choose>
						<c:when test="${ article.level > 1 }">
							<c:forEach begin="1" end="${ article.level }" step="1">
					             <span style="padding-left:10px"></span> 
					         </c:forEach>
					         <span style="font-size:12px;">[답변]</span>
				             <a href="${ contextPath }/board/viewArticle.do?articleNO=${ article.articleNO }">${article.title}</a>
					     </c:when>
					     <c:otherwise>
					        <a href="${ contextPath }/board/viewArticle.do?articleNO=${ article.articleNO }">${article.title }</a>
					     </c:otherwise>
					</c:choose>
				</td>
				<td width="15%"><fmt:formatDate pattern="yyyy-MM-dd" value="${ article.writeDate }"/></td>
				<td width="5%">${ article.views }</td>
			</tr>
		</c:forEach>
	</table>
	
	<div align="center">
		<c:if test="${ totArticle != null }">
			<c:choose>
			<c:when test="${ totArticle < 100 }">
				<c:forEach var="page" begin="1" end="${ totArticle/10+1 }" step="1">
					<a href="${ contextPath }/board/listArticles.do?section=${ section }&pageNum=${ page }">${ page }</a>
				</c:forEach>
			</c:when>
			
			<c:when test="${ totArticle == 100 }">
				<c:forEach var="page" begin="1" end="10" step="1">
					<a href="${ contextPath }/board/listArticles.do?section=${ section }&pageNum=${ page }">${ page }</a>
				</c:forEach>
			</c:when>
			
			<c:when test="${ totArticle > 100 }">
				<c:if test="${ section == lastSection }"> <!-- 마지막 section일 때 -->
					<c:forEach var="page" begin="1" end="${ (totArticle/10+1) - (section-1)*10 }" step="1">
						<c:if test="${ page == 1 }">
							<a href="${ contextPath }/board/listArticles.do?section=${ section-1 }&pageNum=10">prev</a>
						</c:if>
						<a href="${ contextPath }/board/listArticles.do?section=${ section }&pageNum=${ page }">${ page + (section-1)*10 }</a>
					</c:forEach>
				</c:if>
				<c:if test="${ section < lastSection }"> <!-- 마지막 section이 아닐 때(10페이지 존재) -->
					<c:forEach var="page" begin="1" end="10" step="1">
						<c:if test="${ section > 1 && page == 1 }">
							<a href="${ contextPath }/board/listArticles.do?section=${ section-1 }&pageNum=10">prev</a>
						</c:if>
						<a href="${ contextPath }/board/listArticles.do?section=${ section }&pageNum=${ page }">${ page + (section-1)*10 }</a>
						<c:if test="${ page == 10 }">
							<a href="${ contextPath }/board/listArticles.do?section=${ section+1 }&pageNum=1">next</a>
						</c:if>
					</c:forEach>
				</c:if>
			
			</c:when>
		</c:choose>
		</c:if>
	</div>
	<h1 align="center"><a href="javascript:fn_articleForm('${ isLogOn }', '${ contextPath }/board/articleForm.do',
						 '${ contextPath }/member/loginForm.do' )">새 글쓰기</a></h1>
	<br><br>
</body>
</html>