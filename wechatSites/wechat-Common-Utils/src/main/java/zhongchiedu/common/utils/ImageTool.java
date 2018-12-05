package zhongchiedu.common.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ImageTool {

	private static final Logger LOG = LoggerFactory
			.getLogger(ImageTool.class);
	private File file = null; // 文件对象

	private String inputDir; // 输入图路径

	private String outputDir; // 输出图路径

	private String inputFileName; // 输入图文件名

	private String outputFileName; // 输出图文件名

	private int outputWidth = 100; // 默认输出图片宽

	private int outputHeight = 100; // 默认输出图片高

	private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)

	public ImageTool() { // 初始化变量

		inputDir = "";

		outputDir = "";

		inputFileName = "";

		outputFileName = "";

		outputWidth = 100;

		outputHeight = 100;

	}

	public void setInputDir(String inputDir) {

		this.inputDir = inputDir;

	}

	public void setOutputDir(String outputDir) {

		this.outputDir = outputDir;

	}

	public void setInputFileName(String inputFileName) {

		this.inputFileName = inputFileName;

	}

	public void setOutputFileName(String outputFileName) {

		this.outputFileName = outputFileName;

	}

	public void setOutputWidth(int outputWidth) {

		this.outputWidth = outputWidth;

	}

	public void setOutputHeight(int outputHeight) {

		this.outputHeight = outputHeight;

	}

	public void setWidthAndHeight(int width, int height) {

		this.outputWidth = width;

		this.outputHeight = height;

	}

	/*
	 * 
	 * 获得图片大小
	 * 
	 * 传入参数 String path ：图片路径
	 */

	public long getPicSize(String path) {

		file = new File(path);

		return file.length();

	}

	// 图片压缩处理
	public boolean compressPic() {

		try {
			// 获得源文件
			file = new File(inputDir+inputFileName);

			if (!file.exists()) {
				return false;
			}

			Image img = ImageIO.read(file);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				LOG.error("当前文件不是图片格式！");
				return false;

			} 
				int newWidth;
				int newHeight;

				// 判断是否是等比缩放

				if (this.proportion == true) {

					// 为等比缩放计算输出的图片宽度及高度

					double rate1 = ((double) img.getWidth(null))
							/ (double) outputWidth + 0.1;

					double rate2 = ((double) img.getHeight(null))
							/ (double) outputHeight + 0.1;

					// 根据缩放比率大的进行缩放控制

					double rate = rate1 > rate2 ? rate1 : rate2;

					newWidth = (int) (((double) img.getWidth(null)) / rate);

					newHeight = (int) (((double) img.getHeight(null)) / rate);

				} else {

					newWidth = outputWidth; // 输出的图片宽度

					newHeight = outputHeight; // 输出的图片高度

				}

				BufferedImage tag = new BufferedImage((int) newWidth,
						(int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * 
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的
				 * 
				 * 优先级比速度高 生成的图片质量比较好 但速度慢
				 */

				tag.getGraphics().drawImage(
						img.getScaledInstance(newWidth, newHeight,
								Image.SCALE_SMOOTH), 0, 0, null);

				FileOutputStream out = new FileOutputStream(outputDir
						+ outputFileName);
				
				ImageInputStream iis = ImageIO.createImageInputStream(file);
				Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);
				String formatName=null;
				while (imageReaders.hasNext()) {
				    ImageReader reader = (ImageReader) imageReaders.next();
				    formatName=reader.getFormatName();
				}
				ImageIO.write(tag, formatName, out);
				out.close();


		} catch (IOException e) {
			LOG.error("图片创建缩略图错误");
			LOG.error(e.getMessage(),e);
			return false;
		}
		return true;

	}
	
	/**
	/**
	 * 等比例压缩图片大小
	 * @param inputDir	压缩图片的路径
	 * @param outputDir	输出图片路径
	 * @param inputFileName	压缩图片的名字
	 * @param outputFileName	输出图片的名字
	 * @return
	 */
	public boolean compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName) {

		// 输入图路径

		this.inputDir = inputDir;

		// 输出图路径

		this.outputDir = outputDir;

		// 输入图文件名

		this.inputFileName = inputFileName;

		// 输出图文件名

		this.outputFileName = outputFileName;

		return compressPic();

	}
	
	/**
	 * 自定义压缩图片大小
	 * @param inputDir	压缩图片的路径
	 * @param outputDir	输出图片路径
	 * @param inputFileName	压缩图片的名字
	 * @param outputFileName	输出图片的名字
	 * @param width	图片的宽度
	 * @param height	图片的高度
	 * @param gp 等比例压缩图片(true/为等比列压,Default:true)	
	 * @return	OK OR NO
	 */
	public boolean compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName, int width, int height,boolean gp) {

		// 输入图路径
		this.inputDir = inputDir;

		// 输出图路径
		this.outputDir = outputDir;

		// 输入图文件名
		this.inputFileName = inputFileName;

		// 输出图文件名
		this.outputFileName = outputFileName;
		
		this.proportion=gp;

		// 设置图片长宽
		setWidthAndHeight(width, height);
		return compressPic();

	}
	
	/**
	 * 等比例压缩图片大小
	 * @param inputDir	压缩图片的路径
	 * @param outputDir	输出图片路径
	 * @param inputFileName	压缩图片的名字
	 * @param outputFileName	输出图片的名字
	 * @param gp	等比例压缩图片(true/为等比列压,Default:true)	
	 * @return
	 */
	public boolean compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName,
			boolean gp) {

		// 输入图路径
		this.inputDir = inputDir;

		// 输出图路径
		this.outputDir = outputDir;

		// 输入图文件名
		this.inputFileName = inputFileName;

		// 输出图文件名
		this.outputFileName = outputFileName;
		
		// 是否是等比缩放 标记
		this.proportion = gp;
		return compressPic();

	}

	public static void main(String[] arg) {
		try{
			ImageTool mypic = new ImageTool();
			String fileName="123.jpg";	//图片的路径
			// compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
			mypic.compressPic("I:\\",
					"I:\\", fileName,
					"1_"+fileName,350,300,true);
	}catch(Exception e){
		e.printStackTrace();
	}
	}

}
