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
	
	private static final String FILENAME = "fileName";      //文件名称
	private static final String CONTENTTYPE = "contentType";
	private static final String HASSUFFIX = "hassuffix";     //是否满足后缀名
	private static final String SAVEPATH = "savepath";       //存储硬盘路径
	private static final String NOFILE = "nofile";           //是否满足文件
	private static final String RENAME = "reName";           //重命名
	private static final String SERVLETPATH = "servletPath"; //服务器路径
	private static final String EXTENSION = "extension"; //文件后缀名

	
	
	
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

//		if (name.indexOf(".") != -1) {
//			fileName += name.substring(0, name.lastIndexOf(".")) + name.substring(name.lastIndexOf("."));
//		}
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
	
   
   
   

	/***
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @param storeName
	 * @param contentType
	 * @param realName
	 * @throws Exception
	 */
	public static void download(HttpServletRequest request, HttpServletResponse response, String storeName,
			String contentType, String UPLOADDIR) throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;

		BufferedOutputStream bos = null;
		
		String ctxPath =request.getServletContext().getRealPath("/WEB-INF/") + UPLOADDIR;
		
		String downLoadPath = ctxPath + storeName;

		long fileLength = new File(downLoadPath).length();

		response.setContentType(contentType);

		response.setHeader("Content-disposition",
				"attachment; filename=" + new String(storeName.getBytes("utf-8"), "ISO8859-1"));

		response.setHeader("Content-Length", String.valueOf(fileLength));

		bis = new BufferedInputStream(new FileInputStream(downLoadPath));

		bos = new BufferedOutputStream(response.getOutputStream());

		byte[] buff = new byte[2048];

		int bytesRead;

		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}

   
   
	/**
	 * 文件上传
	 * 
	 * // 固定参数值对 // .put(FileOperateUtil.STORENAME, zipName(storeName)); //
	 * map.put(FileOperateUtil.SIZE, new File(zipName).length()); //
	 * map.put(FileOperateUtil.SUFFIX, "zip");
	 * 
	 * @param request
	 *            httpservletRequest
	 * @param UPLOADDIR
	 *            上传至
	 * @param filetype
	 *            上传文件类型
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> upload(HttpServletRequest request, String UPLOADDIR, String[] filetype)
			throws Exception {

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;

		Map<String, MultipartFile> fileMap = mRequest.getFileMap();
		// 文件重命名
		String rname = "";

		String path = "";

		File destFile = null;
		// 上传目录
		String uploadDir = request.getSession().getServletContext().getRealPath("/WEB-INF/") + UPLOADDIR;

		// 判断文件目录是否存在，如果不存在则创建目录
		File file = new File(uploadDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 文件名称
		String fileName = null;
		// 文件是否符合要求格式
		boolean has = false;

		for (Iterator<Map.Entry<String, MultipartFile>> it = fileMap.entrySet().iterator(); it.hasNext();) {

			Map<String, Object> map = new HashMap<String, Object>();
			

			Map.Entry<String, MultipartFile> entry = it.next();

			MultipartFile mFile = entry.getValue();

			map.put(FileOperateUtil.NOFILE, false);
			// 获取上传文件的名称
			fileName = mFile.getOriginalFilename();
			if (fileName.equals("") || fileName.equals(null)) {
				// 设置标识没有文件上传
				map.put(FileOperateUtil.NOFILE, true);
				result.add(map);
				return result;
			}
			// 获取文件的后缀名
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			// 如果文件的后缀名为空
			if (extension == "") {
				map.put(FileOperateUtil.NOFILE, true);
			} else {
				for (int a = 0; a < filetype.length; a++) {
					if (filetype[a].contains(extension)) {
						has = true;
					}
				}
				// 标识后缀名
				map.put(FileOperateUtil.HASSUFFIX, has);
				map.put(FileOperateUtil.EXTENSION, extension);//文件后缀名
				
				if (has == false) {
					// 设置表示上传文件符合文件要求
					result.add(map);
					return result;
				}
				// 对上传文件进行重命名
				rname = rname(fileName);
				map.put(FileOperateUtil.RENAME, rname);
				path = file + "/" + rname;// 存放位置\
				destFile = new File(path);
			}

			OutputStream outputStream = new FileOutputStream(destFile);
			FileCopyUtils.copy(mFile.getInputStream(), outputStream);
			map.put(FileOperateUtil.CONTENTTYPE, "application/octet-stream");
			map.put(FileOperateUtil.SAVEPATH, path);
			map.put(FileOperateUtil.FILENAME, fileName);
			map.put(FileOperateUtil.SERVLETPATH, request.getContextPath() + UPLOADDIR + rname);

			result.add(map);
		}
		return result;
	}
   
   
   
   
   
   
   
   
   
   
	

}