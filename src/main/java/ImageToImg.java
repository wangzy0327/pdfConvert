import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ImageToImg {

    private static final int IMG_WIDTH = 1280;

    /**
     *
     *@param filePath 原图片所在文件夹路径，
     *@param imgFile 图片生成路径，
     */
    public static void FileImage(String filePath,String imgFile) {
        int fileNum = 0, folderNum = 0;
        File file = new File(filePath);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    System.out.println("文件夹:" + file2.getAbsolutePath());
                    list.add(file2);
                    fileNum++;
                } else {
                    String extension = "";
                    if(file2.getName().indexOf("jpg")>0||file2.getName().indexOf("png")>0){
                        BufferedImage image = null;
                        try {
                            image = ImageIO.read(file2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String imgName = imgFile + file2.getName();
                        writeHighQuality(image,imgName);
                    }
                    System.out.println("文件:" + file2.getAbsolutePath());
                    folderNum++;
                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        list.add(file2);
                        fileNum++;
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                        folderNum++;
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
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
            jep.setQuality(1.0f, true);
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
        int newWidth = (width>IMG_WIDTH)?IMG_WIDTH:width;
        int newHeight = (newWidth*height)/width;
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resizedImage;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        FileImage("C:\\Users\\wangzy\\Desktop\\test", "C:\\Users\\wangzy\\Desktop\\test1\\");
        long endTime = System.currentTimeMillis();
        System.out.println("当前程序耗时："+(endTime - startTime)+"ms");
    }

}
