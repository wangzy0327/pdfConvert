import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDF2Img {

    private static final int IMG_WIDTH = 1280;

    /**
     * 将指定pdf文件的首页转换为指定路径的缩略图
     *@param pdfFile 原文件路径，
     *@param imageFile 图片生成路径，
     *@param zoom  缩略图显示倍数，1表示不缩放，0.3则缩小到30%     2 表示放大两倍
     */
    public static void pdfToImage(String pdfFile,String imageFile,float zoom) {

        PdfDecoder decoder = null;
        List<BufferedImage> imageList = null;
        try {
            decoder = new PdfDecoder(true);
            imageList = new ArrayList<>();
            FontMappings.setFontReplacements();
            decoder.setExtractionMode(0, zoom);
            decoder.openPdfFile(pdfFile);
            int start = 1, end = decoder.getPageCount();
            List<String> imgNames = new ArrayList<>();
            Double convertFraction = 0.0d;
            for (int i = start; i < end + 1; i++) {
                BufferedImage image = decoder.getPageAsImage(i);
                String imgName = imageFile + "__pdf__pic_" + (i) + ".png";
                imgNames.add(imgName);
                writeHighQuality(image,imgName);
//                ImageIO.write(image, "png", new File(imgName));
                convertFraction = ((double)(i))/end;
                System.out.println("已转化:"+new java.text.DecimalFormat("#.00").format(convertFraction*100)+"%");
            }

        } catch (PdfException e) {
            e.printStackTrace();
        } finally {
            decoder.closePdfFile();
        }
    }

    /**
     * 将宽度相同的图片，竖向追加在一起 ##注意：宽度必须相同
     * @param piclist 文件流数组
     * @param outPath 输出路径
     */
    public static void yPic(List<BufferedImage> piclist, String outPath) {// 纵向处理图片
        if (piclist == null || piclist.size() <= 0) {
            System.out.println("图片数组为空!");
            return;
        }
        try {
            int height = 0, // 总高度
                    width = 0, // 总宽度
                    _height = 0, // 临时的高度 , 或保存偏移高度
                    __height = 0, // 临时的高度，主要保存每个高度
                    picNum = piclist.size();// 图片的数量
            File fileImg = null; // 保存读取出的图片
            int[] heightArray = new int[picNum]; // 保存每个文件的高度
            BufferedImage buffer = null; // 保存图片流
            List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
            int[] _imgRGB; // 保存一张图片中的RGB数据
            for (int i = 0; i < picNum; i++) {
                buffer = piclist.get(i);
                heightArray[i] = _height = buffer.getHeight();// 图片高度
                if (i == 0) {
                    width = buffer.getWidth();// 图片宽度
                }
                height += _height; // 获取总高度
                _imgRGB = new int[width * _height];// 从图片中读取RGB
                _imgRGB = buffer
                        .getRGB(0, 0, width, _height, _imgRGB, 0, width);
                imgRGB.add(_imgRGB);
            }
            _height = 0; // 设置偏移高度为0
            // 生成新图片
            BufferedImage imageResult = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_BGR);
            for (int i = 0; i < picNum; i++) {
                __height = heightArray[i];
                if (i != 0)
                    _height += __height; // 计算偏移高度
                imageResult.setRGB(0, _height, width, __height, imgRGB.get(i),
                        0, width); // 写入流中
            }
            File outFile = new File(outPath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(imageResult, "jpg", out);// 写图片
            byte[] b = out.toByteArray();
            FileOutputStream output = new FileOutputStream(outFile);
            output.write(b);
            out.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean writeHighQuality(BufferedImage im, String fileFullPath) {
        try {
	            /*输出到文件流*/
            FileOutputStream newimage = new FileOutputStream(fileFullPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
            Graphics graphics = im.getGraphics();
            BufferedImage bufferedImage = null;
            int type = im.getType() == 0? BufferedImage.TYPE_INT_ARGB : im.getType();
            bufferedImage = resizeImage(im,type);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(bufferedImage);
	            /* 压缩质量 */
            jep.setQuality(0.9f, true);
            encoder.encode(bufferedImage, jep);
	           /*近JPEG编码*/
            newimage.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type){
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int newWidth = IMG_WIDTH;
        int newHeight = (newWidth*height)/width;
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        pdfToImage("D:\\王紫阳\\任务\\测试pdf\\睛像科技_内存溢出.pdf", "D:\\王紫阳\\任务\\测试pdf\\pdfImage\\睛像科技_内存溢出",2.0f);
        long endTime = System.currentTimeMillis();
        System.out.println("当前程序耗时："+(endTime - startTime)+"ms");
    }

}
