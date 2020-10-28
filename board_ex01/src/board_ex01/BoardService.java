package board_ex01;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardService {
	BoardDAO boardDAO;
	
	public BoardService() {
		boardDAO = new BoardDAO();
	}
	
	//1. �� ��� ����(/listArticles.do)
	public HashMap listArticles(HashMap<String, Integer> pagingMap) {
		HashMap articlesMap = new HashMap();
		ArrayList<ArticleVO> articlesList = boardDAO.selectAllArticles(pagingMap);
		int totArticles = boardDAO.selectTotArticles();
		
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("totArticles", totArticles);
		
		return articlesMap;
	}
	
	//2. �� �߰��ϱ�(/addArticle.do)
	public int addArticle(ArticleVO article) {
		return boardDAO.insertNewArticle(article);
	}
	
	//3. �� ���� �����ֱ�(/viewArticle.do)
	public ArticleVO viewArticle(int articleNO) {
		ArticleVO article = null;
		article = boardDAO.selectArticle(articleNO);
		return article;
	}
	
	//4. �� �����ϱ�(/modArticle.do)
	public void modArticle(ArticleVO article) {
		boardDAO.updateArticle(article);
	}
	
	//5. �� �����ϱ�(/removeArticle.do)
	public ArrayList<Integer> removeArticle(int articleNO) {
		ArrayList<Integer> articleNOList = boardDAO.selectRemovedArticles(articleNO);
		boardDAO.deleteArticle(articleNO);
		return articleNOList;
	}
}
