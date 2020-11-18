package com.myspring.pro30.board.vo;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component("imageVO")
public class ImageVO {
	private int imageFileNO;
	private String imageFileName;
	private Date regDate;
	private int articleNO;
	
	public ImageVO() {
		
	}

	public ImageVO(int imageFileNO, String imageFileName, int articleNO) {
		super();
		this.imageFileNO = imageFileNO;
		this.imageFileName = imageFileName;
		this.articleNO = articleNO;
	}

	public int getImageFileNO() {
		return imageFileNO;
	}

	public void setImageFileNO(int imageFileNO) {
		this.imageFileNO = imageFileNO;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public int getArticleNO() {
		return articleNO;
	}

	public void setArticleNO(int articleNO) {
		this.articleNO = articleNO;
	}
	
}
