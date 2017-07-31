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
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.print.PrintServiceLookup;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.json.JSONArray;
import org.json.JSONException;
import javax.print.PrintService;

/**
 *
 * @author Ahmed Saboor
 */
public class GeneratePDF {
    
    /**
     * This Method is used to generate PDF.
     *
     * @param CustomerData
     * @param tableData
     * @return 
     * @throws java.io.IOException
     * @throws com.itextpdf.text.DocumentException
     * @throws org.json.JSONException
     */

    public String GeneratePDFDoc(String[] CustomerData, JSONArray tableData) throws IOException, DocumentException, JSONException {

        
        String Path = "D:\\InvoicePDF/" + CustomerData[1] + ".pdf";
        //Create PdfReader instance. 
        PdfReader pdfReader = new PdfReader("D:\\InvoicePDF/SamplePDF.pdf");
        //Create PdfStamper instance. 
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(Path));
        //Create BaseFont instance. 
        //Create BaseFont instance. 
        BaseFont baseFont = BaseFont.createFont(BaseFont.COURIER_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            
        int pages = pdfReader.getNumberOfPages();
        if (1 == pages) {
            PdfContentByte CustomerName = pdfStamper.getOverContent(pages);
            CustomerName.beginText();
            //Set text font and size. 
            CustomerName.setFontAndSize(baseFont, 13);
            CustomerName.setTextMatrix(90, 665);
            //Write text 
            //System.out.println( CustomerData[0]);
            CustomerName.showText(CustomerData[0]);
            CustomerName.endText();

            PdfContentByte billNo = pdfStamper.getOverContent(pages);
            billNo.beginText();
            //Set text font and size. 
            billNo.setFontAndSize(baseFont, 13);
            billNo.setTextMatrix(90, 695);
            //Write text 
            billNo.showText(CustomerData[1]);
            billNo.endText();

            PdfContentByte submitDate = pdfStamper.getOverContent(pages);
            submitDate.beginText();
            //Set text font and size. 
            submitDate.setFontAndSize(baseFont, 13);
            submitDate.setTextMatrix(465, 695);
            //Write text 
            submitDate.showText(CustomerData[2]);
            submitDate.endText();

            int heightValue = 590;
            int totalPrice = 0;
            for (int i = 0; i < tableData.length(); i++) {

                //System.out.println(tableData.getJSONObject(i));
                // Set Product Name.
                PdfContentByte productpacking = pdfStamper.getOverContent(pages);
                productpacking.beginText();
                //Set text font and size. 
                productpacking.setFontAndSize(baseFont, 13);
                productpacking.setTextMatrix(115, heightValue);

                //Write text 
                productpacking.showText(tableData.getJSONObject(i).get("Packing").toString());
                productpacking.endText();
                
                
                
                // Set Product Name.
                PdfContentByte productName = pdfStamper.getOverContent(pages);
                productName.beginText();
                //Set text font and size. 
                productName.setFontAndSize(baseFont, 13);
                productName.setTextMatrix(155, heightValue);

                //Write text 
                productName.showText(tableData.getJSONObject(i).get("ProductName").toString());
                productName.endText();

                // Set Product Quanlity.
                PdfContentByte ProductQuanlity = pdfStamper.getOverContent(pages);
                ProductQuanlity.beginText();
                //Set text font and size. 
                ProductQuanlity.setFontAndSize(baseFont, 13);
                ProductQuanlity.setTextMatrix(50, heightValue);

                //Write text 
                if  ( tableData.getJSONObject(i).get("Bonus").toString().isEmpty()) {
                    ProductQuanlity.showText(tableData.getJSONObject(i).get("Quanlity").toString());
                } else {
                    ProductQuanlity.showText(tableData.getJSONObject(i).get("Quanlity").toString() + '+' + tableData.getJSONObject(i).get("Bonus").toString());
                }
                ProductQuanlity.endText();

                if (CustomerData[3] == "Individuals") {
                    // Set Product Discount.
                    PdfContentByte ProductDiscount = pdfStamper.getOverContent(pages);
                    ProductDiscount.beginText();
                    //Set text font and size. 
                    ProductDiscount.setFontAndSize(baseFont, 13);
                    ProductDiscount.setTextMatrix(458, heightValue);
                
                    //Write text 
                    ProductDiscount.showText(tableData.getJSONObject(i).get("Discount").toString()+ "%");
                    
                    
                    //Set text font and size. 
                    ProductDiscount.setFontAndSize(baseFont, 17);
                    ProductDiscount.setTextMatrix(458, heightValue);
                    
                    //Bonus
                    ProductDiscount.showText("0");
                    
                    ProductDiscount.endText();
                
                }
                
                // Set Product Trade Price.
                PdfContentByte ProductTradePrice = pdfStamper.getOverContent(pages);
                ProductTradePrice.beginText();
                //Set text font and size. 
                ProductTradePrice.setFontAndSize(baseFont, 13);
                ProductTradePrice.setTextMatrix(395, heightValue);

                //Write text 
                ProductTradePrice.showText(tableData.getJSONObject(i).get("TradePrice").toString());
                ProductTradePrice.endText();

                // Set Product Trade Price.
                PdfContentByte ProductTotalPrice = pdfStamper.getOverContent(pages);
                ProductTotalPrice.beginText();
                //Set text font and size. 
                ProductTotalPrice.setFontAndSize(baseFont, 13);
                ProductTotalPrice.setTextMatrix(510, heightValue);
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
                DiscountTotalMainPrice.setTextMatrix(450, 110);
                //Write text 
                DiscountTotalMainPrice.showText(CustomerData[4] + " %");
                DiscountTotalMainPrice.endText();

                // Set Product Main Trade Price.
                PdfContentByte ProductTotalMainPrice = pdfStamper.getOverContent(pages);
                ProductTotalMainPrice.beginText();
                //Set text font and size. 
                ProductTotalMainPrice.setFontAndSize(baseFont, 13);
                ProductTotalMainPrice.setTextMatrix(510, 110);

                float discountPrice = (float) totalPrice * ((float) Integer.parseInt(CustomerData[4]) / 100);
                int finalPrice = totalPrice - (int) discountPrice;
                //Write text 
                ProductTotalMainPrice.showText(Integer.toString(finalPrice));
                
                //Set text font and size. 
                ProductTotalMainPrice.setFontAndSize(baseFont, 15);
                ProductTotalMainPrice.setTextMatrix(510, 110);
                
                //Write Bonus 
                ProductTotalMainPrice.showText("0");
                
                ProductTotalMainPrice.endText();

            } else {
                // Set Product Main Trade Price.
                PdfContentByte ProductTotalMainPrice = pdfStamper.getOverContent(pages);
                ProductTotalMainPrice.beginText();
                //Set text font and size. 
                ProductTotalMainPrice.setFontAndSize(baseFont, 13);
                ProductTotalMainPrice.setTextMatrix(510, 110);
                //Write text 
                ProductTotalMainPrice.showText(Integer.toString(totalPrice));
                ProductTotalMainPrice.endText();
            }

        }
        pdfStamper.close();

        return Path;
    }

    /**
     * This method is used for print PDF
     *
     * @param Path Path of the PDF.
     * @exception IOException On input error.
     * @exception PrinterException On print error.
     * @see IOException
     * @see PrinterException
     */
    public void PrintPDF(String Path) throws IOException, PrinterException {
        // Create PDDocument
        PDDocument document = PDDocument.load(new File(Path));
        // Function Call for PrintService.
        PrintService myPrintService = findPrintService("hp LaserJet 3015 UPD PCL 5");
        // Get PrinterJob.
        PrinterJob job = PrinterJob.getPrinterJob();
        // Show Dialog
        job.printDialog();
        // Set Page for Path
        job.setPageable(new PDFPageable(document));
        // Set Print Service
        job.setPrintService(myPrintService);
        // Job Print Function
        job.print();
    }

    /**
     * This Method is used to find the Print Service
     *
     * @param printerName This is the name of the Printer
     * @return PrintService This will return the PrinterServuce Object.
     */
    private static PrintService findPrintService(String printerName) {
        // Declare PrintService Object.
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        // For Loop 
        for (PrintService printService : printServices) {
            //System.out.println(printService.getName().trim());
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }

    /**
     * This Method is used to find the Print Service
     *
     * @param attributeName Used for attribute Name
     * @param errorIntCode Used for error Code
     */
    public void ShowDialog(String attributeName, int errorIntCode) {
        String errorMessage = "", errorTitle = "";

        switch (errorIntCode) {
            case 1:
                errorTitle = "Validation Error";
                errorMessage = attributeName + " is Empty.";
                break;
            case 2:
                errorTitle = "Validation Error";
                errorMessage = "User information or Bill no is not filled Properly.";
                break;
            case 3:
                errorTitle = "Validation Error";
                errorMessage = "User information or Bill no have invalid entry.";
                break;
            case 4:
                errorTitle = "Warming Error";
                errorMessage = "Discount Package is not select yet.";
                break;
            case 5:
                errorTitle = "Table Error";
                errorMessage = "Table is empty.";
                break;
            case 6:
                errorTitle = "Table Error";
                errorMessage = "Table fields entry are empty or invalid.";
                break;
            case 7:
                errorTitle = "Validation Error";
                errorMessage = "Combine discount field is empty.";
                break;
            case 8:
                errorTitle = "Good News";
                errorMessage = "PDF created Successfully:)";
                break;
            default:
                break;
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(attributeName);
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
}
