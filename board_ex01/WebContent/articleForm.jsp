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
	function readURL(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$("#preview").attr("src", e.target.result);
				
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	
	function backToList(obj) {
		obj.action = "${ contextPath }/board/listArticles.do";
		obj.submit();
	}
</script>
<meta charset="UTF-8">
<title>글쓰기창</title>
</head>
<body>
	<h1 align="center">새 글 쓰기</h1>
	<form method="post" action="${ contextPath }/board/addArticle.do" encType="multipart/form-data">
		<table align="center">
			<tr>
				<td align="right">글제목 :</td>
				<td><input type="text" name="title" size="67" maxlength="500" /></td>
			</tr>
			<tr>
				<td align="right">글내용 :</td>
				<td><textarea name="content" rows="15" cols="62"></textarea>
			</tr>
			<tr>
				<td align="right">이미지파일 첨부 :</td>
				<td>
					<input type="file" name="imageFileName" onChange="readURL(this);" />
					<img id="preview" src="#" width="200" height="200">
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding-left:130px">
					<input type="submit" value="글쓰기" />
					<input type="button" value="목록보기" onClick="backToList(this.form)" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>