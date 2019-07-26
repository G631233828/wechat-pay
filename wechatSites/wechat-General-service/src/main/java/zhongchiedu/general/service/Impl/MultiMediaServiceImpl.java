package zhongchiedu.general.service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Contents;
import zhongchiedu.common.utils.FileOperateUtil;
import zhongchiedu.common.utils.ImageTool;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.general.pojo.MultiMedia;
import zhongchiedu.general.service.MultiMediaService;
@Service
public class MultiMediaServiceImpl  extends GeneralServiceImpl<MultiMedia> implements MultiMediaService{

	
	@Autowired
	private FileOperateUtil fileOperateUtil;
	
	@Autowired
	private ImageTool imageTool;
	
	/**
	 * 上传文件
	 * @return
	 */
	public List<MultiMedia> uploadPictures(MultipartFile[] file,String dir,String path,String belong){
		
		List<MultiMedia> list = new ArrayList<MultiMedia>();
		if(Common.isNotEmpty(file[0].getOriginalFilename())){
			for(MultipartFile m:file){
				String uploadPath = dir+path;
				MultiMedia multi = new MultiMedia();
				Map<String,Object> map = this.fileOperateUtil.upload(m, uploadPath, m.getOriginalFilename());
				String compName =  map.get(Contents.FILENAME).toString();
				imageTool.compressPic(uploadPath,uploadPath,compName,"comp_"+compName,640,320,true);
				multi.setCompressPicName(compName);
				multi.setOriginalName(m.getOriginalFilename());
				multi.setGenerateName(map.get(Contents.FILENAME).toString());
				multi.setSavePath(path);
				multi.setDir(dir);
				multi.setThumbnail(null);
				multi.setExtension(map.get(Contents.SUFFIXNAME).toString());
				multi.setFileType("IMG");
				long size = m.getSize();
				String sizeStr = size < 1023 ? "B"
						: size < (1024 * 1024) - 1 ? "KB" : size < (1024 * 1024 * 1024) - 1 ? "MB" : "GB";
				multi.setFileSize(size < 1023 ? size
						: size < (1024 * 1024) - 1 ? (size / 1024)
								: size < (1024 * 1024 * 1024) - 1 ? (size / (1024 * 1024)) : (size / (1024 * 1024 * 1024)));
				multi.setFileSizeStr(sizeStr);
				multi.setBelong(belong);
				this.insert(multi);
				list.add(multi);
			}
		}
		return list;
		
	}
	
	/**
	 * 上传文件
	 * @return
	 */
	public MultiMedia uploadVideo(MultipartFile m,String path,String belong){
		    MultiMedia multi=null;
		if(Common.isNotEmpty(m.getOriginalFilename())){
				multi = new MultiMedia();
				Map<String,Object> map = this.fileOperateUtil.upload(m, path, m.getOriginalFilename());
				multi.setOriginalName(m.getOriginalFilename());
				multi.setGenerateName(map.get(Contents.FILENAME).toString());
				multi.setSavePath(path);
				multi.setThumbnail(null);
				multi.setExtension(map.get(Contents.SUFFIXNAME).toString());
				multi.setFileType("Video");
				long size = m.getSize();
				String sizeStr = size < 1023 ? "B"
						: size < (1024 * 1024) - 1 ? "KB" : size < (1024 * 1024 * 1024) - 1 ? "MB" : "GB";
				multi.setFileSize(size < 1023 ? size
						: size < (1024 * 1024) - 1 ? (size / 1024)
								: size < (1024 * 1024 * 1024) - 1 ? (size / (1024 * 1024)) : (size / (1024 * 1024 * 1024)));
				multi.setFileSizeStr(sizeStr);
				multi.setBelong(belong);
				this.insert(multi);
		}
		return multi;
		
	}
}
