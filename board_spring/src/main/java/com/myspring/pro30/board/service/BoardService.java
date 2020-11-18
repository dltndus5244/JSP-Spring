package com.myspring.pro30.board.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myspring.pro30.board.dao.BoardDAO;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;

@Service("boardService")
@Transactional(propagation=Propagation.REQUIRED)
public class BoardService {
	@Autowired
	private BoardDAO boardDAO;
	@Autowired
	private ArticleVO articleVO;
	
	public Map listArticles(Map<String, Integer> pagingMap) throws DataAccessException {
		Map articlesMap = new HashMap();
		
		List<ArticleVO> articlesList = boardDAO.selectAllArticleByPage(pagingMap);
		int totArticle = boardDAO.selectTotArticle();
		
		articlesMap.put("articlesList", articlesList);
		articlesMap.put("totArticle", totArticle);
		
		return articlesMap;
	}
	
	public int addArticle(Map<String, Object> articleMap) throws DataAccessException {
		int i_articleNO = boardDAO.insertArticle(articleMap);
		String articleNO = Integer.toString(i_articleNO);
		articleMap.put("articleNO", articleNO);
		boardDAO.insertImage(articleMap);
		return i_articleNO;
	}
	
	public Map viewArticle(int articleNO) throws DataAccessException { 
		Map articleMap = new HashMap();
		
		articleVO = boardDAO.selectArticle(articleNO);
		
		//조회수 높임
		boardDAO.updateViews(articleNO); 
		
		List<ImageVO> imageFileList = boardDAO.selectImage(articleNO);
		
		articleMap.put("article", articleVO);
		articleMap.put("imageFileList", imageFileList);
		
		return articleMap;
	}
	
	public void removeArticle(int articleNO) throws DataAccessException {
		boardDAO.deleteArticle(articleNO);
	}
	
	public void modArticle(Map<String, Object> articleMap) throws DataAccessException {
		boardDAO.updateArticle(articleMap); //글 수정(title, content)
		List<ImageVO> imageFileList = (List) articleMap.get("imageFileList");
		
		if (imageFileList != null && imageFileList.size() != 0) {
			String imageFileNO = Integer.toString(imageFileList.get(0).getImageFileNO());
			if (imageFileNO.equals("0")) {
				boardDAO.insertImage(articleMap);
			} else {
				boardDAO.updateImage(imageFileList);
			}
		}

	}	
}
