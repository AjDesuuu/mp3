/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author alvar
 */

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Map;


public class pdfGenerator {
    
    public static void generate(){
        Document document = new Document();

       
        Rectangle rect = new Rectangle(PageSize.LETTER);
        document.setPageSize(rect);
        
        
        try{
            PdfWriter.getInstance(document,new FileOutputStream("blah.pdf"));    
            document.open();
            Paragraph paragraph = new Paragraph();
            paragraph.add("worw");
            document.add(paragraph);
            document.close();
        }catch(Exception e){
            e.printStackTrace();
        } 
        
        
    }
    
    public static void fontWrite() throws DocumentException{
        
        Document d = new Document();
        
        Font[] fonts ={
            new Font(Font.FontFamily.COURIER, 18,Font.BOLD, new BaseColor(0,0,0))
        };
        
        d.open();
        
        for(int x = 0; x < fonts.length; x++) {
            //The Font Family is: Courier. The font size is: 12.0
            d.add(new Paragraph("The Font Family is: " + fonts[x].getFamilyname() + 
                    ". The font size is: " + fonts[x].getSize(),
                    fonts[x]));
        }
        
        d.close();
    }
    
    public static void readStuff(){
        try {
            //create the PdfReader object
            PdfReader reader = new PdfReader(new FileInputStream("Fonts.pdf"));
            
            //get the PDF version of that document
            System.out.println("PDF Version: " + reader.getPdfVersion());
            //how many pages does the document have?
            System.out.println("Number of Pages: " + reader.getNumberOfPages());
            //how long is it?
            System.out.println("File Length: " + reader.getFileLength());
            //is the file encrypted? Does it need a password to be unlocked?
            System.out.println("Is it encrypted? " + reader.isEncrypted());
            
            //get width, height and rotation of page 1
            System.out.println("Width of Page 1: " + reader.getPageSize(1).getWidth());
            System.out.println("Height of Page 1: " + reader.getPageSize(1).getHeight());
            System.out.println("Rotation of Page 1: " + reader.getPageRotation(1));
            
            
           
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void editStuff(){
        try {
            PdfReader reader = new PdfReader("Fonts.pdf");
            
            //You can only create one stamper for each PdfReader!
            //To check, whether or not a reader already has been used, use:
            //reader.isTampered(); (It returns false, if it hasn't been used)
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("Fonts2.pdf"));
            
            //Create some interesting information for our document with the map
            Map<String, String> info = reader.getInfo();
            
            //Set the author of the document to your name
            info.put(/* Key */ "Author", /* Value */ "Felix Fritz");
            //Set the title of the document to whatever you wish
            info.put("Title", "Fontssssss");
            //Add some information about this document
            info.put("Subject", "Working with fonts is fun uyeeeah");
            
            //Use that Map with all those keys and values for our document
            stamper.setMoreInfo(info);
            //Close the stamper again
            stamper.close();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}   


