package board_ex01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private DataSource dataFactory;
	
	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<ArticleVO> selectAllArticles(HashMap<String, Integer> pagingMap) {
		ArrayList<ArticleVO> articlesList = new ArrayList<ArticleVO>();
		int section = pagingMap.get("section");
		int pageNum = pagingMap.get("pageNum");
		
		try {
			conn = dataFactory.getConnection();
			String query = "SELECT * FROM (";
			query += " SELECT ROWNUM as recNum, LVL, articleNO, parentNO, title, id, writeDate";
			query += " FROM (";
			query += " SELECT LEVEL as LVL, articleNO, parentNO, title, id, writeDate";
			query += " FROM t_Board";
			query += " START WITH parentNo=0";
			query += " CONNECT BY PRIOR articleNO=parentNO";
			query += " ORDER SIBLINGS BY articleNO DESC )";
			query += " )";
			query += " WHERE recNum BETWEEN (?-1)*100 + (?-1)*10+1 AND (?-1)*100 + ?*10";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, section);
			pstmt.setInt(2, pageNum);
			pstmt.setInt(3, section);
			pstmt.setInt(4, pageNum);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int level = rs.getInt("lvl");
				int articleNO = rs.getInt("articleNO");
				int parentNO = rs.getInt("parentNO");
				String title = rs.getString("title");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writeDate");
				
				ArticleVO articleVO = new ArticleVO();
				articleVO.setLevel(level);
				articleVO.setArticleNO(articleNO);
				articleVO.setParentNO(parentNO);
				articleVO.setTitle(title);
				articleVO.setId(id);
				articleVO.setWriteDate(writeDate);
				
				articlesList.add(articleVO);
			}
			
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articlesList;
	}
	
	public int selectTotArticles() {
		try {
			conn = dataFactory.getConnection();
			String query = "SELECT COUNT(articleNO) FROM t_Board";
			pstmt = conn.prepareStatement(query);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);
			
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getNewArticleNO() {
		try {
			conn = dataFactory.getConnection();
			String query = "SELECT MAX(articleNO) FROM t_Board";
			pstmt = conn.prepareStatement(query);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) 
				return rs.getInt(1) + 1;
		
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int insertNewArticle(ArticleVO article) {
		int articleNO = getNewArticleNO();
		
		try {
			conn = dataFactory.getConnection();
			
			int parentNO = article.getParentNO();
			String title = article.getTitle();
			String content = article.getContent();
			String imageFileName = article.getImageFileName();
			String id = article.getId();
			
			String query = "INSERT INTO t_Board (articleNO, parentNO, title, content, imageFileName, id)";
			query += " VALUES (?, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.setInt(2, parentNO);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			pstmt.setString(5, imageFileName);
			pstmt.setString(6, id);
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articleNO;
	}
	
	public ArticleVO selectArticle(int articleNO) {
		ArticleVO article = new ArticleVO();
		
		try {
			conn = dataFactory.getConnection();
			
			String query = "SELECT articleNO, title, content, imageFileName, writeDate, id";
			query += " FROM t_Board";
			query += " WHERE articleNO=?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			int _articleNO = rs.getInt("articleNO");
			String title = rs.getString("title");
			String content = rs.getString("content");
			String imageFileName = rs.getString("imageFileName");
			Date writeDate = rs.getDate("writeDate");
			String id = rs.getString("id");
			
			article.setArticleNO(_articleNO);
			article.setTitle(title);
			article.setContent(content);
			article.setImageFileName(imageFileName);
			article.setWriteDate(writeDate);
			article.setId(id);
			
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return article;
	}
	
	public void updateArticle(ArticleVO article) {
		try {
			conn = dataFactory.getConnection();
			
			int articleNO = article.getArticleNO();
			String title = article.getTitle();
			String content = article.getContent();
			String imageFileName = article.getImageFileName();
			
			String query = "UPDATE t_Board SET title=?, content=?";
			if (imageFileName != null && imageFileName.length() != 0)
				query += ", imageFileName=?";
			query += " WHERE articleNO=?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			
			if (imageFileName != null && imageFileName.length() != 0) {
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNO);
			} else {
				pstmt.setInt(3, articleNO);
			}
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteArticle(int articleNO) {
		try {
			conn = dataFactory.getConnection();
			
			String query = "DELETE FROM t_Board";
			query += " WHERE articleNO in (";
			query += " SELECT articleNO FROM t_Board";
			query += " START WITH articleNO=?";
			query += " CONNECT BY PRIOR articleNO=parentNO )";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			pstmt.executeUpdate();
			
			pstmt.close();
			conn.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Integer> selectRemovedArticles(int articleNO) {
		ArrayList<Integer> articleNOList = new ArrayList<Integer>();
		try {
			conn = dataFactory.getConnection();
			
			String query = "SELECT articleNO FROM t_Board";
			query += " START WITH articleNO=?";
			query += " CONNECT BY PRIOR articleNO=parentNO";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, articleNO);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				articleNO = rs.getInt("articleNO");
				articleNOList.add(articleNO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articleNOList;
	}
}
