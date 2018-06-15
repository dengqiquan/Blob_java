package com.avie.ltd.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="blob")
public class BlobController {
	
	@RequestMapping(value="getStream")
	public void getMp3or4(HttpServletRequest request,HttpServletResponse response){
		//创建文件对象
		File f = new File("E:\\1482999774172.mp3");
		//获取文件名称
		String fileName = f.getName();
		//导出文件
		String agent = request.getHeader("user-agent");
//		String agent = getRequest().getHeader("User-Agent").toUpperCase();
//		InputStream fis = null;
		BufferedInputStream fis = null;
		OutputStream os = null;
		try {
		    fis = new BufferedInputStream(new FileInputStream(f.getPath()));
		    byte[] buffer;
		    buffer = new byte[fis.available()];
		    fis.read(buffer);
		    response.reset();
		    //由于火狐和其他浏览器显示名称的方式不相同，需要进行不同的编码处理
		    if(agent.indexOf("FIREFOX") != -1){//火狐浏览器
		    	response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("GB2312"),"ISO-8859-1"));
		    }else{//其他浏览器
		    	response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
		    }
		    //设置response编码
		    response.setCharacterEncoding("UTF-8");
		    response.addHeader("Content-Length", "" + f.length());
		    //设置输出文件类型
		    response.setContentType("video/mpeg4");
		    //获取response输出流
		    os = response.getOutputStream();
		    // 输出文件
		    os.write(buffer);
		}catch(Exception e){
		    System.out.println(e.getMessage());
		} finally{//With great power there must come great responsibility.
		    //关闭流
		    try {
		        if(fis != null){
		            fis.close();
		        }
		    } catch (IOException e) {
		        System.out.println(e.getMessage());
		    } finally{
		        try {
		            if(os != null){
		                os.flush();
		            }
		        } catch (IOException e) {
		            System.out.println(e.getMessage());
		        } finally{
		            try {
		                if(os != null){
		                    os.close();
		                }
		            } catch (IOException e) {
		                System.out.println(e.getMessage());
		            }
		        }
		    }
		}
	}
}
