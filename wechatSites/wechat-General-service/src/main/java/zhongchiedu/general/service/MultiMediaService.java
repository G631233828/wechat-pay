package zhongchiedu.general.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.general.pojo.MultiMedia;

public interface MultiMediaService extends GeneralService<MultiMedia> {

	
	 List<MultiMedia> uploadPictures(MultipartFile[] file,String dir,String path,String belong);
	
}
