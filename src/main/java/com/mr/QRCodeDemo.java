package com.mr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

public class QRCodeDemo {
	public static void main(String []args) {

		try {
			String currentPath = getPath();
			Scanner sc;
			while (true)
			{
				System.out.println("请根据提示完成操作：");
				System.out.println("1:生成二维码");
				System.out.println("2:识别二维码");
				System.out.println("3:结束程序");
				sc =new Scanner(System.in);
				String getOp=sc.nextLine();
				int op=Integer.parseInt(getOp.trim());
				if(op==1)
				{
					System.out.println("请输入二维码内容，如：网站地址等文字");
					String text = sc.nextLine();
					System.out.println("请输入二维码图片存放地址，绝对路径或相对路径，当前路径："+currentPath);
					String path = sc.nextLine();
					System.out.println("请输入二维码图片名字：");
					String name = sc.nextLine();
					System.out.println("请输入二维码图片格式，如：png,jpg,gif,jpeg");
					String type = sc.nextLine();
					testEncoder(text,path,name,type);
					System.out.println("生成完毕");
				}
				else if(op==2)
				{
					System.out.println("请输入二维码图片存放地址，绝对路径或相对路径，当前路径："+currentPath);
					String path = sc.nextLine();
					System.out.println("请输入二维码图片名字包括格式，如：'pic.png'");
					String name = sc.nextLine();
					testDecoder(path,name);
					System.out.println("识别成功");
				}
				else if(op==3) {
					break;
				}
				else {
					System.out.println("输入有误，请重新输入");
				}
			}
			sc.close();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	private static BitMatrix getBitMatrix(String str){
		//二维码内容
		String text = str;
		//二维码图片宽度 、高度
		int width = 600;
		int height = 600;
		Hashtable<EncodeHintType, String> hints = new Hashtable<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
			//编码
			return new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
		} catch (WriterException e1) {
			e1.printStackTrace();
			return null;
		}
	}

	public static BufferedImage getBitMatrixImage(String text){
		return MatrixToImageWriter.toBufferedImage(getBitMatrix(text));
	}

	public static void testEncoder(String str,String path,String name ,String imgType)
	{
		BitMatrix bitMatrix = getBitMatrix(str);
//	            File outputFile = new File(path+"/"+name+"."+format);
		Path pathToWrite =Paths.get(path+"/"+name+"."+imgType);
		try {
			//输出二维码图片
//	                MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);

			MatrixToImageWriter.writeToPath(bitMatrix, imgType, pathToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void testDecoder(String path,String name) throws NotFoundException
	{
		//二维码图片路径
		File imageFile = new File(path+"/"+name);
		System.out.println(imageFile.getAbsolutePath()+" "+imageFile.exists());
		BufferedImage image = null;
		try {
			image = ImageIO.read(imageFile);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map<DecodeHintType, Object> hints = new HashMap<>();
//	                 Map<DecodeHintType, Object> hints = new EnumMap < DecodeHintType, Object > (
//	                	        DecodeHintType.class);

			//设置编码方式
			hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
			//优化精度
			hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
			//复杂模式，开启PURE_BARCODE模式
			hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
			hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.QR_CODE));
			//解码获取二维码中信息
			Result result = new MultiFormatReader().decode(binaryBitmap, hints);
			System.out.println(result.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getPath()
	{
//		 return URLDecoder.decode(this.getClass().getResource("/").getPath(),"utf-8");
		return System.getProperty("user.dir");

	}

}
