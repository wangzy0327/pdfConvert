import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

public class Pdf2ImgUtil {

    public static void main(String[] args) {
        String filePath="D:\\王紫阳\\任务\\测试pdf\\关于日志、周报规定.pdf";
        Document document=new Document();
        try{
        document.setFile(filePath);
        float scale=2.0f; //缩放比例
        float rotation=0f; //旋转角度
        Double convertFraction = 0.0;
        for(int i=0;i<document.getNumberOfPages();i++){
            BufferedImage image=(BufferedImage)document.getPageImage(i,
                    GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX,
                    rotation,scale);
            convertFraction = ((double)(i+1))/document.getNumberOfPages();
            System.out.println("已转化:"+new java.text.DecimalFormat("#.00").format(convertFraction*100)+"%");
            RenderedImage rendImage=image;
            File file=new File("D:\\王紫阳\\任务\\测试pdf\\pdfImage\\关于日志、周报规定_"+i+".jpg");
            // 这里png作用是：格式是jpg但有png清晰度
            ImageIO.write(rendImage,"png",file);
            image.flush();
        }
        document.dispose();
        }
        catch(Exception e){
        System.out.println(e.getMessage());
        e.printStackTrace();
        }
        System.out.println("======================完成============================");
    }

}
