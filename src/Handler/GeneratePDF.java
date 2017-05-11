/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Handler;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.print.PrintServiceLookup;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.print.PrintService;

/**
 *
 * @author Ahmed Saboor
 */
public class GeneratePDF {

    public String GeneratePDFDoc(String[] CustomerData, JSONArray tableData) throws IOException, DocumentException, JSONException {

        String Path = "D:\\InvoicePDF/" + CustomerData[1] + ".pdf";
        //Create PdfReader instance. 
        PdfReader pdfReader = new PdfReader("D:\\InvoicePDF/SamplePDF.pdf");
        //Create PdfStamper instance. 
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(Path));
        //Create BaseFont instance. 
        //Create BaseFont instance. 
        BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        int pages = pdfReader.getNumberOfPages();
        if (1 == pages) {
            PdfContentByte CustomerName = pdfStamper.getOverContent(pages);
            CustomerName.beginText();
            //Set text font and size. 
            CustomerName.setFontAndSize(baseFont, 13);
            CustomerName.setTextMatrix(100, 470);
            //Write text 
            //System.out.println( CustomerData[0]);
            CustomerName.showText(CustomerData[0]);
            CustomerName.endText();

            PdfContentByte billNo = pdfStamper.getOverContent(pages);
            billNo.beginText();
            //Set text font and size. 
            billNo.setFontAndSize(baseFont, 13);
            billNo.setTextMatrix(65, 490);
            //Write text 
            billNo.showText(CustomerData[1]);
            billNo.endText();

            PdfContentByte submitDate = pdfStamper.getOverContent(pages);
            submitDate.beginText();
            //Set text font and size. 
            submitDate.setFontAndSize(baseFont, 13);
            submitDate.setTextMatrix(330, 490);
            //Write text 
            submitDate.showText(CustomerData[2]);
            submitDate.endText();

            int heightValue = 418;
            int totalPrice = 0;
            for (int i = 0; i < tableData.length(); i++) {

                //System.out.println(tableData.getJSONObject(i));
                // Set Product Name.
                PdfContentByte productName = pdfStamper.getOverContent(pages);
                productName.beginText();
                //Set text font and size. 
                productName.setFontAndSize(baseFont, 13);
                productName.setTextMatrix(104, heightValue);

                //Write text 
                productName.showText(tableData.getJSONObject(i).get("ProductName").toString());
                productName.endText();

                // Set Product Quanlity.
                PdfContentByte ProductQuanlity = pdfStamper.getOverContent(pages);
                ProductQuanlity.beginText();
                //Set text font and size. 
                ProductQuanlity.setFontAndSize(baseFont, 13);
                ProductQuanlity.setTextMatrix(27, heightValue);

                //Write text 
                ProductQuanlity.showText(tableData.getJSONObject(i).get("Quanlity").toString());
                ProductQuanlity.endText();

                if (CustomerData[3] == "Individuals") {
                    // Set Product Discount.
                    PdfContentByte ProductDiscount = pdfStamper.getOverContent(pages);
                    ProductDiscount.beginText();
                    //Set text font and size. 
                    ProductDiscount.setFontAndSize(baseFont, 13);
                    ProductDiscount.setTextMatrix(327, heightValue);

                    //Write text 
                    ProductDiscount.showText(tableData.getJSONObject(i).get("Discount").toString());
                    ProductDiscount.endText();
                }
                // Set Product Trade Price.
                PdfContentByte ProductTradePrice = pdfStamper.getOverContent(pages);
                ProductTradePrice.beginText();
                //Set text font and size. 
                ProductTradePrice.setFontAndSize(baseFont, 13);
                ProductTradePrice.setTextMatrix(275, heightValue);

                //Write text 
                ProductTradePrice.showText(tableData.getJSONObject(i).get("TradePrice").toString());
                ProductTradePrice.endText();

                // Set Product Trade Price.
                PdfContentByte ProductTotalPrice = pdfStamper.getOverContent(pages);
                ProductTotalPrice.beginText();
                //Set text font and size. 
                ProductTotalPrice.setFontAndSize(baseFont, 13);
                ProductTotalPrice.setTextMatrix(365, heightValue);
                heightValue = heightValue - 13;
                //Write text 
                System.out.println(tableData.getJSONObject(i).get("Amount"));
                ProductTotalPrice.showText(tableData.getJSONObject(i).get("Amount").toString());
                ProductTotalPrice.endText();
                totalPrice = totalPrice + Integer.parseInt(tableData.getJSONObject(i).get("Amount").toString());
                //System.out.println(totalPrice);

            }

            if (CustomerData[3] == "Combine") {

                // Set Product Main Trade Price.
                PdfContentByte DiscountTotalMainPrice = pdfStamper.getOverContent(pages);
                DiscountTotalMainPrice.beginText();
                //Set text font and size. 
                DiscountTotalMainPrice.setFontAndSize(baseFont, 13);
                DiscountTotalMainPrice.setTextMatrix(327, 82);
                //Write text 
                DiscountTotalMainPrice.showText(CustomerData[4] + " %");
                DiscountTotalMainPrice.endText();

                // Set Product Main Trade Price.
                PdfContentByte ProductTotalMainPrice = pdfStamper.getOverContent(pages);
                ProductTotalMainPrice.beginText();
                //Set text font and size. 
                ProductTotalMainPrice.setFontAndSize(baseFont, 13);
                ProductTotalMainPrice.setTextMatrix(365, 82);

                float discountPrice = (float) totalPrice * ((float) Integer.parseInt(CustomerData[4]) / 100);
                int finalPrice = totalPrice - (int) discountPrice;
                //Write text 
                ProductTotalMainPrice.showText(Integer.toString(finalPrice));
                ProductTotalMainPrice.endText();

            } else {
                // Set Product Main Trade Price.
                PdfContentByte ProductTotalMainPrice = pdfStamper.getOverContent(pages);
                ProductTotalMainPrice.beginText();
                //Set text font and size. 
                ProductTotalMainPrice.setFontAndSize(baseFont, 13);
                ProductTotalMainPrice.setTextMatrix(365, 82);
                //Write text 
                ProductTotalMainPrice.showText(Integer.toString(totalPrice));
                ProductTotalMainPrice.endText();
            }

        }
        pdfStamper.close();

        return Path;
    }

    public void PrintPDF(String Path) throws IOException, PrinterException {
        PDDocument document = PDDocument.load(new File(Path));
        PrintService myPrintService = findPrintService("hp LaserJet 3015 UPD PCL 5");

        PrinterJob job = PrinterJob.getPrinterJob();

        job.printDialog();
        job.setPageable(new PDFPageable(document));
        job.setPrintService(myPrintService);
        job.print();
    }

    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            System.out.println(printService.getName().trim());
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
}
