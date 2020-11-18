<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${ pageContext.request.contextPath }" />
<c:set var="article" value="${ articleMap.article }" />
<c:set var="imageFileList" value="${ articleMap.imageFileList }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	function backToList(obj) {
		obj.action = "${ contextPath }/board/listArticles.do";
		obj.submit();
	}
	
	function fn_remove_article(obj) {
		obj.action = "${ contextPath }/board/removeArticle.do?articleNO=${ article.articleNO }";
		obj.submit();
	}
	
	function fn_enable(obj) {
		document.getElementById("i_title").disabled = false;
		document.getElementById("i_content").disabled = false;
		document.getElementById("tr_btn_modify").style.display = "block";
		document.getElementById("tr_btn").style.display = "none";
		
		if (${ not empty imageFileList && imageFileList != 'null' }) {
			$(".i_imageFileName").attr('disabled', false);

		} else {
			document.getElementById("file_upload").disabled = false;
		}
	}
	
	function fn_modify_article(obj) {
		obj.action = "${ contextPath }/board/modArticle.do";
		obj.submit();
	}
	
	//여기 해결안댐..ㅎ 미리보기 여러개 일때 첫번쨰꺼만 미리보기 바뀜..
	function readURL(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$("#preview").attr("src", e.target.result);
				
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	
	function fn_replyForm(isLogOn, replyForm, loginForm) {
		if (isLogOn != '' && isLogOn == 'true') {
			location.href=replyForm + '?articleNO=${ article.articleNO }'; //로그인 상태이면 답글쓰기창으로 이동합니다.
		} else {
			//로그아웃 상태이면 action 값으로 다음에 수행할 URL인 /board/replyForm.do를 전달하면서 로그인창으로 이동합니다.
			alert('로그인 후 글쓰기가 가능합니다.');
			location.href=loginForm + '?action=/board/replyForm.do&articleNO=${ article.articleNO }';
		}
	}
	
	//파일 업로드 기능을 동적으로 추가합니다.
	var cnt = 1;
	function fn_addFile() {
		$("#d_file").append("<br>" + "<input type='file' name='newFile" + cnt + "' />");
		cnt++;
	}
</script>
</head>
<body>
	<form name="frmArticle" method="post" encType="multipart/form-data">
		<table align="center">
			<tr>
				<td width="150" bgcolor="lightgreen">글번호</td>
				<td align="left"><input type="text" value="${ article.articleNO }" disabled /></td>
				<input type="hidden" name="articleNO" value="${ article.articleNO }" />
			</tr>
			<tr>
				<td width="150" bgcolor="lightgreen">작성자 아이디</td>
				<td align="left"><input type="text" value="${ article.id }" disabled /></td>
			</tr>
			<tr>
				<td width="150" bgcolor="lightgreen">제목</td>
				<td align="left"><input name="title" id="i_title" type="text" value="${ article.title }" disabled /></td>
			</tr>
			<tr>
				<td width="150" bgcolor="lightgreen">내용</td>
				<td><textarea name="content" id="i_content" rows="20" cols="60" disabled>${ article.content }</textarea></td>
			</tr>
			<c:choose>
				<c:when test="${ not empty imageFileList && imageFileList != 'null' }">
					<c:forEach var="item" items="${ imageFileList }" varStatus="status">
						<tr>
							<td rowspan="2" align="center" width="150" bgcolor="lightgreen">이미지${ status.count }</td>
							<td>
								<img id="preview" src="${ contextPath }/download.do?imageFileName=${ item.imageFileName }&articleNO=${ article.articleNO }"
									width="200" height="200" />
								<input type="hidden" name="originalFileName_${ item.imageFileNO }" value="${ item.imageFileName }" />
							</td>
						</tr>	
						<tr>
							<td>
								<input type="file" name="imageFileName_${ item.imageFileNO }"  class="i_imageFileName" onChange="readURL(this)" disabled/>
							</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td width="150" bgcolor="lightgreen">이미지파일 첨부 :</td>
						<td align="left">
							<input id="file_upload" type="button" value="파일 추가" onClick="fn_addFile()" disabled/>
						</td>
						<td><div id="d_file"></div></td>
					</tr>
				</c:otherwise>
			</c:choose>
			<tr>
				<td width="150" bgcolor="lightgreen">작성일자</td>
				<td align="left"><input type="text" value="<fmt:formatDate pattern='yyyy-MM-dd' value='${ article.writeDate }'/>" disabled/></td>
			</tr>
			<tr id="tr_btn">
				<td colspan="2">
					<c:if test="${ article.id == member.id }">
						<input type="button" value="수정하기" onClick="fn_enable(frmArticle)" />
						<input type="button" value="삭제하기" onClick="fn_remove_article(frmArticle)" />
					</c:if>
					<input type="button" value="리스트로 돌아가기" onClick="backToList(frmArticle)" />
					<input type="button" value="답글쓰기" onClick="fn_replyForm('${ isLogOn }', '${ contextPath }/board/replyForm.do',
																				'${ contextPath }/member/loginForm.do')" />
				</td>
			</tr>
			<tr id="tr_btn_modify" style="display:none;">
				<td>
					<input type="submit" value="수정반영하기" onClick="fn_modify_article(frmArticle)" />
					<input type="button" value="취소" onClick="backToList(frmArticle)" />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>