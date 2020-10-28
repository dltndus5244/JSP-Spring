<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${ pageContext.request.contextPath }" />

<!DOCTYPE html>
<html>
<head>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	function fn_enable(obj) {
		document.getElementById("i_title").disabled = false;
		document.getElementById("i_content").disabled = false;
		document.getElementById("i_imageFileName").disabled = false;
		document.getElementById("tr_btn_modify").style.display = "block";
		document.getElementById("tr_btn").style.display = "none";
	}
	
	function backToList(obj) {
		obj.action = "${ contextPath }/board/listArticles.do";
		obj.submit();
	}
	
	function fn_modify_article(obj) {
		obj.action = "${ contextPath }/board/modArticle.do";
		obj.submit();
	}
	
	function readURL(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$("#preview").attr("src", e.target.result);
				
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	
	function fn_remove_article(obj) {
		obj.action = "${ contextPath }/board/removeArticle.do?articleNO=${ article.articleNO }";
		obj.submit();
	}
	
	function fn_reply_form(obj) {
		obj.action = "${ contextPath }/board/replyForm.do?parentNO=${ article.articleNO }";
		obj.submit();
	}
</script>
<meta charset="UTF-8">
<title>글 상세보기 </title>
</head>
<body>
	<form name="frmArticle" method="post" encType="multipart/form-data">
		<table align="center">
			<tr>
				<td align="center" width="150" bgcolor="lightgreen">글번호</td>
				<td><input type="text" value="${ article.articleNO }" disabled /></td>
				<input type="hidden" name="articleNO" value="${ article.articleNO }" />
			</tr>
			<tr>
				<td align="center" width="150" bgcolor="lightgreen">작성자 아이디</td>
				<td><input type="text" value="${ article.id }" disabled /></td>
			</tr>
			<tr>
				<td align="center" width="150" bgcolor="lightgreen">제목</td>
				<td><input name="title" id="i_title" type="text" value="${ article.title }" disabled /></td>
			</tr>
			<tr>
				<td align="center" width="150" bgcolor="lightgreen">내용</td>
				<td><textarea name="content" id="i_content" rows="20" cols="60" disabled>${ article.content }</textarea></td>
			</tr>
			<c:if test="${ not empty article.imageFileName }">
				<tr>
					<td rowspan="2" align="center" width="150" bgcolor="lightgreen">이미지</td>
					<td>
						<img id="preview" src="${ contextPath }/download.do?imageFileName=${ article.imageFileName }&articleNO=${ article.articleNO }"
							width="200" height="200" />
						<input type="hidden" name="originalFileName" value="${ article.imageFileName }" />
					</td>
				</tr>	
				<tr>
					<td><input type="file" name="imageFileName" id="i_imageFileName" onChange="readURL(this)" disabled/></td>
				</tr>
			</c:if>
			<tr>
				<td align="center" width="150" bgcolor="lightgreen">작성일자</td>
				<td><input type="text" value="${ article.writeDate }" disabled /></td>
			</tr>
			<tr id="tr_btn">
				<td colspan="2">
					<input type="button" value="수정하기" onClick="fn_enable(frmArticle)" />
					<input type="button" value="삭제하기 " onClick="fn_remove_article(frmArticle)" />
					<input type="button" value="답글쓰기" onClick="fn_reply_form(frmArticle)" />
					<input type="button" value="리스트로 돌아기기" onClick="backToList(frmArticle)" />
				</td>
			</tr>
			<tr id="tr_btn_modify" style="display:none;">
				<td>
					<input type="submit" value="수정반영하기" onClick="fn_modify_article(frmArticle)" />
					<input type="button" value="취소" onClick="backToList(frmArticle)" />
				<td>
			</tr>
		</table>
	</form>
</body>
</html>