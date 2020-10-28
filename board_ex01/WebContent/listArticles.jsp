<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${ pageContext.request.contextPath }" />
<c:set var="articlesList" value="${ articlesMap.articlesList }" />
<c:set var="totArticles" value="${ articlesMap.totArticles }" />
<c:set var="section" value="${ articlesMap.section }" />
<c:set var="pageNum" value="${ articlesMap.pageNum }" />
<c:set var="lastSection" value="${ articlesMap.lastSection }" />
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>글 목록 보기 </title>
</head>
<body>
	<h1 align="center">자유 게시판</h1>
	<table border="1" align="center" width="60%" style="border-collapse:collapse">
		<tr align="center" bgcolor="lightgreen">
			<td width="10%"><b>글번호</b></td>
			<td width="60%"><b>제목</b></td>
			<td width="15%"><b>작성자</b></td>
			<td width="15%"><b>작성일</b></td>
		</tr>
		<c:if test="${ empty articlesList }">
			<tr align="center">
				<td colspan="4"><b>글이 존재하지 않습니다.</b></td>
			</tr>
		</c:if>
		<c:if test="${ not empty articlesList }">
			<c:forEach var="article" items="${ articlesList }">
				<tr>
					<td align="center">${ article.articleNO }</td>
					<td style="padding-left:20px">
						<c:if test="${ article.level > 1 }"> <!-- level이 0보다 크면 답글임 -->
							<c:forEach begin="1" end="${ article.level }" step="1">
								<span style="padding-left:15px"></span>
							</c:forEach>
							<span style="font-size:12px;">[답변]</span>
							<a href="${ contextPath }/board/viewArticle.do?articleNO=${ article.articleNO }">${ article.title }</a>
						</c:if>
						<c:if test="${ article.level == 1 }">
							<a href="${ contextPath }/board/viewArticle.do?articleNO=${ article.articleNO }">${ article.title }</a>
						</c:if>
					</td>
					<td align="center">${ article.id }</td>
					<td align="center">${ article.writeDate }</td>
				</tr>
			</c:forEach>
		</c:if>
	</table>
	<br>
	<div align="center">
		<c:if test="${ totArticles != null }">
			<c:choose>
			<c:when test="${ totArticles < 100 }">
				<c:forEach var="page" begin="1" end="${ totArticles/10+1 }" step="1">
					<a href="${ contextPath }/board/listArticles.do?section=${ section }&pageNum=${ page }">${ page }</a>
				</c:forEach>
			</c:when>
			
			<c:when test="${ totArticles == 100 }">
				<c:forEach var="page" begin="1" end="10" step="1">
					<a href="${ contextPath }/board/listArticles.do?section=${ section }&pageNum=${ page }">${ page }</a>
				</c:forEach>
			</c:when>
			
			<c:when test="${ totArticles > 100 }">
				<c:if test="${ section == lastSection }"> <!-- 마지막 section일 때 -->
					<c:forEach var="page" begin="1" end="${ (totArticles/10+1) - (section-1)*10 }" step="1">
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
	<br><br>
	<p align="center"><a href="${ contextPath }/board/articleForm.do">글쓰기</a></p>
</body>
</html>