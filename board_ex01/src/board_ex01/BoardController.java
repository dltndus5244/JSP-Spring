package board_ex01;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	BoardService boardService;
	public static String ARTICLE_IMAGE_REPO = "D:\\board\\article_image";

	public void init(ServletConfig config) throws ServletException {
		boardService = new BoardService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		
		String nextPage = null;
		String action = request.getPathInfo();
		System.out.println("action : " + action);
		
		//1. 글 목록 보기(/listArticles.do)
		if (action == null || action.equals("/listArticles.do")) {
			HashMap<String, Integer> pagingMap = new HashMap<String, Integer>();
			String section = request.getParameter("section");
			String pageNum = request.getParameter("pageNum");
			
			int _section = Integer.parseInt(((section == null) ? "1" : section));
			int _pageNum = Integer.parseInt(((pageNum == null) ? "1" : pageNum));
			
			pagingMap.put("section", _section);
			pagingMap.put("pageNum", _pageNum);
			
			HashMap articlesMap = boardService.listArticles(pagingMap);
			articlesMap.put("section", _section);
			articlesMap.put("pageNum", _pageNum);
			
			int totArticles = (int) articlesMap.get("totArticles");
			int lastSection = totArticles/100+1;
			articlesMap.put("lastSection", lastSection);
			
			request.setAttribute("articlesMap", articlesMap);
			nextPage = "/listArticles.jsp";
		}
		
		//2. 글쓰기 창 띄우기(/articleForm.do)
		else if (action.equals("/articleForm.do")) {
			nextPage = "/articleForm.jsp";
		}
		
		//3.글 추가하기(/addArticle.do)
		else if (action.equals("/addArticle.do")) {
			HashMap<String, String> articleMap = upload(request, response);
			String title = articleMap.get("title");
			String content = articleMap.get("content");
			String imageFileName = articleMap.get("imageFileName");
			
			ArticleVO article = new ArticleVO();
			article.setParentNO(0);
			article.setTitle(title);
			article.setContent(content);
			article.setImageFileName(imageFileName);
			article.setId("hong");
			
			int articleNO = boardService.addArticle(article);
			
			if (imageFileName != null && imageFileName.length() != 0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				destDir.mkdirs();
				
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			
			nextPage = "/board/listArticles.do";
		}
		
		//4. 글 보여주기(/viewArticle.do)
		else if (action.equals("/viewArticle.do")) {
			String articleNO = request.getParameter("articleNO");
			ArticleVO article = null;
			article = boardService.viewArticle(Integer.parseInt(articleNO));
			
			request.setAttribute("article", article);
			nextPage = "/viewArticle.jsp";
			
		}
		
		//5. 글 수정하기(/modArticle.do)
		else if (action.equals("/modArticle.do")) {
			HashMap articleMap = upload(request, response);
			
			String articleNO = (String) articleMap.get("articleNO");
			String title = (String) articleMap.get("title");
			String content = (String) articleMap.get("content");
			String imageFileName = (String) articleMap.get("imageFileName");
			
			ArticleVO article = new ArticleVO();
			article.setArticleNO(Integer.parseInt(articleNO));
			article.setTitle(title);
			article.setContent(content);
			article.setImageFileName(imageFileName);
			
			boardService.modArticle(article);
			
			if (imageFileName != null && imageFileName.length() != 0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				destDir.mkdirs();
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
				
				String originalFileName = (String) articleMap.get("originalFileName");
				File oldFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + originalFileName);
				oldFile.delete();
			}
			
			nextPage = "/board/viewArticle.do?articleNO=" + articleNO;
		}
		
		//6. 글 삭제하기(/removeArticle)
		else if (action.equals("/removeArticle.do")) {
			int articleNO = Integer.parseInt(request.getParameter("articleNO"));
			ArrayList<Integer> articleNOList = boardService.removeArticle(articleNO);
			
			for (int _articleNO : articleNOList) {
				File imgDir = new File(ARTICLE_IMAGE_REPO + "\\" + _articleNO);
				if (imgDir.exists())
					FileUtils.deleteDirectory(imgDir);
			}
			
			nextPage = "/board/listArticles.do";
			
		}
		
		//7. 답글창 띄우기(/replyForm.do)
		else if (action.equals("/replyForm.do")) {
			int parentNO = Integer.parseInt(request.getParameter("parentNO"));
			HttpSession session = request.getSession();
			session.setAttribute("parentNO", parentNO);
			
			nextPage = "/replyForm.jsp";
		}
		
		//8. 답글 추가하기(/addReply.do) 
		else if (action.equals("/addReply.do")) {
			HttpSession session = request.getSession();
			int parentNO = (int) session.getAttribute("parentNO");

			HashMap<String, String> articleMap = upload(request, response);
			String title = articleMap.get("title");
			String content = articleMap.get("content");
			String imageFileName = articleMap.get("imageFileName");
			
			ArticleVO article = new ArticleVO();
			article.setParentNO(parentNO);
			article.setTitle(title);
			article.setContent(content);
			article.setImageFileName(imageFileName);
			article.setId("lee");
			
			int articleNO = boardService.addArticle(article);
			
			if (imageFileName != null && imageFileName.length() != 0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				destDir.mkdirs();
				
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			nextPage = "/board/viewArticle.do?articleNO=" + articleNO;
		}
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
		dispatcher.forward(request, response);
	}
	
	public HashMap<String, String> upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> articleMap = new HashMap<String, String>();
		String encoding = "utf-8";
		
		File currentDirPath = new File(ARTICLE_IMAGE_REPO);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(currentDirPath);
		factory.setSizeThreshold(1024*1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			List items = upload.parseRequest(request);
			for (int i=0; i<items.size(); i++) {
				FileItem fileItem = (FileItem) items.get(i);
				if (fileItem.isFormField()) {
					articleMap.put(fileItem.getFieldName(), fileItem.getString(encoding));
					System.out.println(fileItem.getFieldName() + " = " + fileItem.getString(encoding));
				} else {
					if (fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\");
						if (idx == -1)
							idx = fileItem.getName().lastIndexOf("/");
						
						String fileName = fileItem.getName().substring(idx+1);
						articleMap.put(fileItem.getFieldName(), fileName);
						System.out.println(fileItem.getFieldName() + " = " + fileItem.getName());
						
						File uploadFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + fileName);
						fileItem.write(uploadFile);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articleMap;
		
	}
}
