package zhongchiedu.common.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Repository
public class FileOperateUtil {
	

	
	
	
	/***
	 * 将上传的文件进行重命名
	 * 
	 * @param name
	 * @return
	 */
	private static String rname(String name) {
		Long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		Long random = (long) (Math.random() * now);
		String fileName = now + "" + random;

		if (name.indexOf(".") != -1) {
			fileName += name.substring(0, name.lastIndexOf(".")) + name.substring(name.lastIndexOf("."));
		}
		return fileName;

	}

	
	
	
	
	/**
    *
    * @param file 文件
    * @param path 文件存放路径
    * @param fileName 源文件名
    * @return
    */
   public static Map<String,Object> upload(MultipartFile file,String path, String fileName){
	   Map<String,Object> map = new HashMap<>();
       // 生成新的文件名
       //String realPath = path + "/" + FileNameUtils.getFileName(fileName);
       //使用文件名
       String rname = rname(fileName);
	   String realPath = path + "/" + rname;
       
       File dest = new File(realPath);
       //判断文件父目录是否存在
       if(!dest.getParentFile().exists()){
           dest.getParentFile().mkdirs();
       }
       try {
           //保存文件
           file.transferTo(dest);
           map.put(Contents.SAVEPATH, realPath);
           map.put(Contents.FILENAME, rname);
           map.put(Contents.ERROR, false);
           map.put(Contents.UPLOADDIR, path);
           map.put(Contents.SUFFIXNAME,Common.getSuffix(file.getOriginalFilename()));
           return map;
       } catch (IllegalStateException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       map.put(Contents.ERROR, true);
       return map;
       
       
   }
	
	

}