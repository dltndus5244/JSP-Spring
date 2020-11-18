package com.myspring.pro30.board.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;


@Repository("boardDAO")
public class BoardDAO {
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private ArticleVO articleVO;
	
	public List<ArticleVO> selectAllArticleByPage(Map<String, Integer> pagingMap) throws DataAccessException {
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticleByPage", pagingMap);
		return articlesList;
	}
	
	public int selectTotArticle() throws DataAccessException {
		int totArticle = sqlSession.selectOne("mapper.board.selectTotArticle");
		return totArticle;
	}
	
	public int insertArticle(Map<String, Object> articleMap) throws DataAccessException {
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		sqlSession.insert("mapper.board.insertArticle", articleMap);
		return articleNO;
	}
	
	public void insertImage(Map<String, Object> articleMap) throws DataAccessException {
		List<ImageVO> imageFileList = (ArrayList) articleMap.get("imageFileList");
		
		//어케 해결했냐면..테스트 해보니까 mod로 map에 저장된 articleNO는 String으로 저장되는데, 
		//add로 map에 저장된 articleNO는 int..? 로 저장되는거 같아서 둘이 형이 달라서 오류낫던 듯 
		//그래서 add에 articleNO 저장해줄 때 string으로 저장했음
		/*
		 * 근데 왜 갑자기 답글이 안됨..? add랑 똑같은 경로로 오는데? 왜?
		 */
		int articleNO = Integer.parseInt((String) articleMap.get("articleNO"));	
		System.out.println("articleNO : " + articleNO);
		int imageFileNO = selectNewImageNO();
		
		for (ImageVO imageVO : imageFileList) {
			imageVO.setImageFileNO(++imageFileNO);
			imageVO.setArticleNO(articleNO);
		}
		
		sqlSession.insert("mapper.board.insertImage", imageFileList);
	}
	
	public int selectNewImageNO() throws DataAccessException {
		int imageFileNO = sqlSession.selectOne("mapper.board.selectNewImageNO");
		return imageFileNO;
	}
	public int selectNewArticleNO() throws DataAccessException {
		int articleNO = sqlSession.selectOne("mapper.board.selectNewArticleNO");
		return articleNO + 1;
	}
	
	public ArticleVO selectArticle(int articleNO) throws DataAccessException {
		articleVO = sqlSession.selectOne("mapper.board.selectArticle", articleNO);
		return articleVO;
	}
	
	public List<ImageVO> selectImage(int articleNO) throws DataAccessException {
		List<ImageVO> imageFileList = sqlSession.selectList("mapper.board.selectImage", articleNO);
		return imageFileList;
	}
	
	public void deleteArticle(int articleNO) throws DataAccessException {
		sqlSession.delete("mapper.board.deleteArticle", articleNO);
	}
	
	public void updateArticle(Map<String, Object> articleMap) throws DataAccessException {
		sqlSession.update("mapper.board.updateArticle", articleMap);
	}
	
	public void updateImage(List<ImageVO> imageFileList) throws DataAccessException {
		sqlSession.update("mapper.board.updateImage", imageFileList);
	}
	
	public void updateViews(int articleNO) throws DataAccessException {
		sqlSession.update("mapper.board.updateViews", articleNO);
	}
}
