package test.com.liang;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.vietocr.ImageHelper;

import org.junit.Test;

public class TestOCR {
	
	@Test
	public static String recognizeCharacter(BufferedImage bufferedImage) throws IOException, TesseractException {
		// 这里对图片黑白处理,增强识别率.这里先通过截图,截取图片中需要识别的部分
		BufferedImage bi = ImageHelper.convertImageToGrayscale(bufferedImage);
		// 图片锐化,自己使用中影响识别率的主要因素是针式打印机字迹不连贯,所以锐化反而降低识别率
		bi = ImageHelper.convertImageToBinary(bi);
		// 图片放大5倍,增强识别率(很多图片本身无法识别,放大15倍时就可以轻易识,但是考滤到客户电脑配置低,针式打印机打印不连贯的问题,这里就放大5倍)  
		bi = ImageHelper.getScaledInstance(bi, bi.getWidth() * 5, bi.getHeight() * 5);
		Tesseract instance = Tesseract.getInstance();
//		File imageFile = new File("F:\\testEnglishBig.png");
//		BufferedImage bi = ImageIO.read(imageFile);
		return instance.doOCR(bi);
	}
}
