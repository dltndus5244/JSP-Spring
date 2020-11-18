package com.myspring.pro30.board.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;
import com.myspring.pro30.member.vo.MemberVO;

@Controller("boardController")
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	@Autowired
	private ArticleVO articleVO;
	
	private static String ARTICLE_IMAGE_REPO = "D:\\file_repo\\board";
	
	@RequestMapping(value="/listArticles.do", method= { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listArticles(@RequestParam(value="section", required=false) String _section,
									@RequestParam(value="pageNum", required=false) String _pageNum,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int section = Integer.parseInt(((_section == null) ? "1" : _section));
		int pageNum = Integer.parseInt(((_pageNum == null) ? "1" : _pageNum));
		
		Map<String, Integer> pagingMap = new HashMap<String, Integer>();
		pagingMap.put("section", section);
		pagingMap.put("pageNum", pageNum);
		
		Map articlesMap = boardService.listArticles(pagingMap);
		articlesMap.put("section", section);
		articlesMap.put("pageNum", pageNum);
		
		int totArticle = (Integer) articlesMap.get("totArticle");
		int lastSection = totArticle/100+1;
		articlesMap.put("lastSection", lastSection);
		
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesMap", articlesMap);
		
		return mav;
	}
	
	@RequestMapping(value="/articleForm.do", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView articleForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		return mav;
	}
	
	@RequestMapping(value="/addArticle.do", method=RequestMethod.POST)
	public ModelAndView addArticle(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> articleMap = new HashMap<String, Object>(); //�� ������ �����ϱ� ���� articleMap�� �����մϴ�.
		Enumeration enu = request.getParameterNames();
		
		//�۾���â���� ���۵� �� ������ Map�� key/value�� �����մϴ�. 
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = request.getParameter(name);
			articleMap.put(name, value); //title, content
		}

		//�α��� �� ���ǿ� ���ε��� MemberVO ��ü �����ͼ� id ������
		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("member");
		String id = member.getId();
		
		articleMap.put("id", id);
		articleMap.put("parentNO", 0);
	
		List<String> fileList = upload(request); //÷���� �̹��� ���� �̸��� fileList�� ��ȯ�մϴ�.
		
		//���۵� �̹��� ������ ImageVO ��ü�� �Ӽ��� ���ʴ�� ������ �� imageFileList�� �ٽ� �����մϴ�.
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
		if (fileList != null && fileList.size() != 0) {
			for (String fileName : fileList) {
				ImageVO imageVO = new ImageVO();
				imageVO.setImageFileName(fileName);
				imageFileList.add(imageVO);
			}
			articleMap.put("imageFileList", imageFileList); //imageFileList�� �ٽ� articleMap�� �����մϴ�.
		}
		
		for (int i=0; i<imageFileList.size(); i++) {
			System.out.println("�̹��� �̸� : " + imageFileList.get(i).getImageFileName());
		}
		
		String imageFileName = null;
		try {
			int articleNO = boardService.addArticle(articleMap);
			
			if (imageFileList != null && imageFileList.size() != 0) {
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
			}
		} catch (Exception e) {
			//���� �߻� �� temp ������ �̹������� ��� �����մϴ�.
			if (imageFileList != null && imageFileList.size() != 0) {
				for(ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
					srcFile.delete();
				}
			}
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/board/listArticles.do");
		return mav;
	}
	
	//���� ���� �߰��� ���ε�
	public List<String> upload(MultipartHttpServletRequest request) throws Exception {
		List<String> fileList = new ArrayList<String>();
		Iterator<String> fileNames = request.getFileNames();
		
		while (fileNames.hasNext()) {
			String originalFileName = null;
			String fileName = fileNames.next(); //������ �Ű������̸�(file1, file2,...)
			MultipartFile mFile = request.getFile(fileName);
			
			originalFileName = mFile.getOriginalFilename();
			fileList.add(originalFileName); //���� �̸��� fileList�� ����
			File file = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + originalFileName);
			
			if (mFile.getSize() != 0) {
				try {
					mFile.transferTo(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return fileList;
		
	}
	
	public List<ImageVO> upload2(MultipartHttpServletRequest request) throws Exception {
		List<ImageVO> fileList = new ArrayList<ImageVO>();
		Iterator<String> fileNames = request.getFileNames();
		
		while (fileNames.hasNext()) {
			String originalFileName = null;
			String fileName = fileNames.next(); //�Ű����� �̸�(imageFileName_��ȣ)
			MultipartFile mFile = request.getFile(fileName);
			
			//���� �ִ� �̹��� ������ �������� ���
			if (fileName.contains("imageFileName")) {
				System.out.println("�������� �Ծ��~~~");
				if (mFile.isEmpty() == false) {
					originalFileName = mFile.getOriginalFilename();
					int underbar = fileName.lastIndexOf("_");
					String str_imageFileNO = fileName.substring(underbar+1);
					int imageFileNO = Integer.parseInt(str_imageFileNO);
					
					ImageVO imageVO = new ImageVO();
					imageVO.setImageFileNO(imageFileNO);
					imageVO.setImageFileName(originalFileName);
					
					fileList.add(imageVO);
				}
			} else { //�̹��� ������ �����µ� ���� �߰����� ���
				System.out.println("�߰��� �Ծ��~~~");
				originalFileName = mFile.getOriginalFilename();
				
				ImageVO imageVO = new ImageVO();
				imageVO.setImageFileName(originalFileName);
				
				fileList.add(imageVO);
			}
	
			File file = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + originalFileName);
			if (mFile.getSize() != 0) {
				try {
					mFile.transferTo(file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}	
		return fileList;
	}
	
	@RequestMapping(value="/viewArticle.do", method=RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
										HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map articleMap = boardService.viewArticle(articleNO);
		
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articleMap", articleMap);
		
		return mav;
	}
	
	@RequestMapping(value="/removeArticle.do", method= { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView removeArticle(@RequestParam("articleNO") int articleNO,
										HttpServletRequest request, HttpServletResponse response) throws Exception {
		boardService.removeArticle(articleNO);
		
		try {
			File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
			FileUtils.deleteDirectory(destDir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/board/listArticles.do");
		return mav;
		
	}
	
	@RequestMapping(value="/modArticle.do", method=RequestMethod.POST)
	public ModelAndView modArticle(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = request.getParameterNames();
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
		List<String> fileList = new ArrayList<String>();
		
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = request.getParameter(name);
			articleMap.put(name, value); //articleNO, title, content, originalFileName
		}
		
		imageFileList = upload2(request);
		
		for (int i=0; i<imageFileList.size(); i++) {
			System.out.println(imageFileList.get(i).getImageFileName());
		}
		
		
		articleMap.put("imageFileList", imageFileList);
		
		boardService.modArticle(articleMap);
		
		String articleNO = (String) articleMap.get("articleNO");
		
		String imageFileName = null;
		
		try {			
			if (imageFileList != null && imageFileList.size() != 0) {
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName(); //�ٲ� �̹��� ���� (temp�� ����)
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					
					//������ ������ ���ؼ� ���� �� �̹��� ������ �������� ����..�� ��� ���� �ϴ��� �� �� �����غ��ߵż� �� ����� ���߿� �ִ°ɷ�
				}
			}
		} catch (Exception e) {
			//���� �߻� �� temp ������ �̹������� ��� �����մϴ�.
			if (imageFileList != null && imageFileList.size() != 0) {
				for(ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
					srcFile.delete();
				}
			}
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/board/viewArticle.do?articleNO=" + articleNO);
		return mav;
	}
	
	@RequestMapping(value="/replyForm.do", method= {RequestMethod.GET, RequestMethod.POST}) 
	public ModelAndView replyForm(@RequestParam("articleNO") int articleNO,
									HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articleNO", articleNO);
		return mav;
	}
	
	@RequestMapping(value="/addReply.do", method=RequestMethod.POST)
	public ModelAndView addReply(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> articleMap = new HashMap<String, Object>(); //�� ������ �����ϱ� ���� articleMap�� �����մϴ�.
		Enumeration enu = request.getParameterNames();
		
		//�۾���â���� ���۵� �� ������ Map�� key/value�� �����մϴ�. 
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = request.getParameter(name);
			articleMap.put(name, value); //title, content, parentNO(�� String��)
		}

		//�α��� �� ���ǿ� ���ε��� MemberVO ��ü �����ͼ� id ������
		HttpSession session = request.getSession();
		MemberVO member = (MemberVO) session.getAttribute("member");
		String id = member.getId();
		articleMap.put("id", id);
	
		List<String> fileList = upload(request); //÷���� �̹��� ���� �̸��� fileList�� ��ȯ�մϴ�.
		
		//���۵� �̹��� ������ ImageVO ��ü�� �Ӽ��� ���ʴ�� ������ �� imageFileList�� �ٽ� �����մϴ�.
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();
		if (fileList != null && fileList.size() != 0) {
			for (String fileName : fileList) {
				ImageVO imageVO = new ImageVO();
				imageVO.setImageFileName(fileName);
				imageFileList.add(imageVO);
			}
			articleMap.put("imageFileList", imageFileList); //imageFileList�� �ٽ� articleMap�� �����մϴ�.
		}
		
		for (int i=0; i<imageFileList.size(); i++) {
			System.out.println("���� �̸� : " + imageFileList.get(i).getImageFileName());
		}
		
		String imageFileName = null;
		try {
			int articleNO = boardService.addArticle(articleMap);
			
			if (imageFileList != null && imageFileList.size() != 0) {
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
			}
		} catch (Exception e) {
			//���� �߻� �� temp ������ �̹������� ��� �����մϴ�.
			if (imageFileList != null && imageFileList.size() != 0) {
				for(ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\temp\\" + imageFileName);
					srcFile.delete();
				}
			}
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/board/listArticles.do");
		return mav;
	}
} 
