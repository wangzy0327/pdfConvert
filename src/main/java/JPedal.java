import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JPedal {
    public static void main(String[] args) throws IOException, PdfException,
            InterruptedException {
        /**instance of PdfDecoder to convert PDF into image*/
        PdfDecoder decode_pdf = new PdfDecoder(true);

        /**set mappings for non-embedded fonts to use*/
        FontMappings.setFontReplacements();

        /**open the PDF file - can also be a URL or a byte array*/
        try {
            decode_pdf.openPdfFile("D:\\王紫阳\\任务\\测试pdf\\东华智慧水利.pdf"); //file
            //decode_pdf.openPdfFile("C:/myPDF.pdf", "password"); //encrypted file
            //decode_pdf.openPdfArray(bytes); //bytes is byte[] array with PDF
            //decode_pdf.openPdfFileFromURL("http://www.mysite.com/myPDF.pdf",false);

            /**get page 1 as an image*/
            //page range if you want to extract all pages with a loop
            int start = 1, end = decode_pdf.getPageCount();
            for (int i = start; i < end + 1; i++) {
                BufferedImage img = decode_pdf.getPageAsImage(i);
                ImageIO.write(img, "jpg", new File("D:\\王紫阳\\任务\\测试pdf\\东华智慧水利_" + i + ".jpg"));
            }
            /**close the pdf file*/
            decode_pdf.closePdfFile();

        } catch (PdfException e) {
            e.printStackTrace();
        }
    }
}
