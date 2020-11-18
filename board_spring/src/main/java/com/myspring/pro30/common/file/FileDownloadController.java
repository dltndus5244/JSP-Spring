package com.myspring.pro30.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("fileDownloadController")
public class FileDownloadController {
	private static String ARTICLE_IMAGE_REPO = "D:\\file_repo\\board";
	
	@RequestMapping(value="/download.do", method=RequestMethod.GET)
	public void download(@RequestParam("imageFileName") String imageFileName,
							@RequestParam("articleNO") int articleNO,
							HttpServletResponse response) throws Exception {
		String downFile = ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + imageFileName;
		File file = new File(downFile);
		
		response.setHeader("Cache-Control", "no-cache");
		response.addHeader("Content-disposition", "attachment;fileName=" + imageFileName);
		
		FileInputStream in = new FileInputStream(downFile);
		OutputStream out = response.getOutputStream();
		byte[] buffer = new byte[1024*8];
		
		while (true) {
			int count = in.read(buffer);
			if (count == -1) break;
			out.write(buffer, 0, count);
		}
		in.close();
		out.close();
	}
}
