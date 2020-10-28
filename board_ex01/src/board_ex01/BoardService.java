package board_ex01;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardService {
	BoardDAO boardDAO;
	
	public BoardService() {
		boardDAO = new BoardDAO();
	}
	
	//1. 글 목록 보기(/listArticles.do)
	public HashMap listArticles(HashMap<String, Integer> pagingMap) {
		HashMap articlesMap = new HashMap();
		ArrayList<ArticleVO> articlesList = boardDAO.selectAllArticles(pagingMap);
		int totArticles = boardDAO.selectTotArticles();
		
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("totArticles", totArticles);
		
		return articlesMap;
	}
	
	//2. 글 추가하기(/addArticle.do)
	public int addArticle(ArticleVO article) {
		return boardDAO.insertNewArticle(article);
	}
	
	//3. 글 내용 보여주기(/viewArticle.do)
	public ArticleVO viewArticle(int articleNO) {
		ArticleVO article = null;
		article = boardDAO.selectArticle(articleNO);
		return article;
	}
	
	//4. 글 수정하기(/modArticle.do)
	public void modArticle(ArticleVO article) {
		boardDAO.updateArticle(article);
	}
	
	//5. 글 삭제하기(/removeArticle.do)
	public ArrayList<Integer> removeArticle(int articleNO) {
		ArrayList<Integer> articleNOList = boardDAO.selectRemovedArticles(articleNO);
		boardDAO.deleteArticle(articleNO);
		return articleNOList;
	}
}
