import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PDF2IMAGE {
    public static void main(String[] args) {
        pdf2Image("D:\\王紫阳\\任务\\测试pdf\\睛像科技_内存溢出.pdf", "D:\\王紫阳\\任务\\测试pdf\\pdfImage\\", 50);
    }

    /***
     * PDF文件转PNG图片，全部页数
     *
     * @param PdfFilePath pdf完整路径
     * @param dstImgFolder 图片存放的文件夹
     * @param dpi dpi越大转换后越清晰，相对转换速度越慢
     * @return
     */
    public static void pdf2Image(String PdfFilePath, String dstImgFolder, int dpi) {
        //TODO 校验输入文件是否存在 以及是否为PDF
        //TODO 对于PDF加密后的处理
        File inputFile = new File(PdfFilePath);
        PDDocument document = null;
        try {
            document = PDDocument.load(inputFile);
            int size = document.getNumberOfPages();
            List<BufferedImage> picList = new ArrayList<BufferedImage>();
            for (int i = 0; i < size; i++) {
                BufferedImage image = new PDFRenderer(document).renderImageWithDPI(i, dpi, ImageType.RGB);
                picList.add(image);
            }
            document.close();
            List<String> imgNames = new ArrayList<>();
            Double convertFraction = 0.0d;
            for (int j = 0; j < picList.size(); j++) {
                BufferedImage image = picList.get(j);
                String uuid = UUID.randomUUID().toString();
                String imgName = dstImgFolder + uuid + "__pdf__pic_" + (j + 1) + ".png";
                imgNames.add(imgName);
                ImageIO.write(image, "png", new File(imgName));
                convertFraction = ((double)(j+1))/picList.size();
                System.out.println("已转化:"+new java.text.DecimalFormat("#.00").format(convertFraction*100)+"%");
            }
            for(int i = 0;i<imgNames.size();i++){
                System.out.println((i+1)+"页:"+imgNames.get(i));
            }
//            return imgNames;
        } catch (IOException e) {
            e.printStackTrace();
//            return null;
        }
    }

    private static boolean createDirectory(String folder) {
        File dir = new File(folder);
        if (dir.exists()) {
            return true;
        } else {
            return dir.mkdirs();
        }
    }
}
