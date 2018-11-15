package zhongchiedu.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/** 
* @ClassName: ZipCompress 
* @Description: TODO(文件批量打包压缩) 
* @author fliay
* @date 2017年12月15日 上午10:45:00 
*  
*/
public class ZipCompress {

	
	
	/**
	 * 
	* @Title: zipFile 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param srcFile    源文件列表
	* @param @param zipFile    压缩后的文件 
	* @return void    返回类型 
	* @throws
	 */
	public static void zipFile(List<File> srcFile,File zipFile){
		
		byte[] buf = new byte[1024];
		
		try{
			//ZipOutputStream 完成文件的压缩
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
			
			for(int i=0;i<srcFile.size();i++){
				
				FileInputStream in = new FileInputStream(srcFile.get(i));
				
				out.putNextEntry(new ZipEntry(srcFile.get(i).getName()));
				
				int len;
				
				while((len = in.read(buf))>0){
					
						out.write(buf, 0, len);
						
				}
				
				out.closeEntry();
				
				in.close();
			}
			
			out.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	/**
	 * 
	* @Title: unZipFiles 
	* @Description: TODO(zip文件解压缩) 
	* @param @param zipfile
	* @param @param descDir     
	* @return void    返回类型 
	* @throws
	 */
	public static void unZipFiles(File zipfile,String descDir){
	    try {
	      ZipFile zf=new ZipFile(zipfile);
	      for(Enumeration entries=zf.entries();entries.hasMoreElements();){
	        ZipEntry entry=(ZipEntry) entries.nextElement();
	        String zipEntryName=entry.getName();
	        InputStream in=zf.getInputStream(entry);
	        OutputStream out=new FileOutputStream(descDir+zipEntryName);
	        byte[] buf1=new byte[1024];
	        int len;
	        while((len=in.read(buf1))>0){
	          out.write(buf1,0,len);
	        }
	        in.close();
	        out.close();
	      }
	    } catch (Exception e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	  }
	
	
	//删除文件
    public static void deleteFile(File file)
    { 
        if (file.exists())
        { //判断文件是否存在
            if (file.isFile())
            { //判断是否是文件
                file.delete(); //delete()方法 你应该知道 是删除的意思;
            }
            else if (file.isDirectory())
            { //否则如果它是一个目录
                File files[] = file.listFiles(); //声明目录下所有的文件    files[];
                for (int i=0;i<files.length;i++)
                { //遍历目录下所有的文件
                    deleteFile(files[i]); //把每个文件 用这个方法进行迭代
                } 
            } 
            file.delete();
        }
        else
        { 
            System.out.println("所删除的文件不存在！"+'\n'); 
        } 
    }
    
    
    
    
    	//生成临时下载压缩包
    public static String getZipFilename()
    {  
        Date date=new Date();  
        String s=date.getTime()+".zip";  
        return s;  
    }  
    
    //创建临时的下载压缩文件
    public static String templatePath(String path){
    	
    	
    	return path+File.separator;
    }
    
    
    
    
    
    
	
	
	
	
	
	
	
	public static void main(String[] args) {
		List<File> srcfile = new ArrayList();
		File f1 = new File("I:/PS素材/1.jpg");
		File f2 = new File("I:/PS素材/2.jpg");
		
		srcfile.add(f1);
		srcfile.add(f2);
		
		
		String path = templatePath("I:/PS素材/");
		
		
		
		System.out.println(path);
		File zipfile = new File(path+getZipFilename());
		
		zipFile(srcfile,zipfile);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deleteFile(new File(path+getZipFilename()));
	}
	
	
	
	
	
	
	
	
	
	
	
}
