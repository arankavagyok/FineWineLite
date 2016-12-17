package boraszat.model;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.Date;

public class PDFSzamla {
    private static final Font LARGE = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    private static final Font NORMAL = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    public static double osszes =0;
    
    private static void addMetaAdat(Document document) {
                document.addTitle("Számla");
                document.addAuthor("Rojik Ádám és Györffy Bálint");
                document.addCreator("Rojik Ádám és Györffy Bálint");
        }

    public PDFSzamla(Document doc, KészBor kBor/*ObservableList<KészBor> kBor*/, String vevőNév, 
                    String vevőCím, String vevőAdóSz, String vevőBankSz, int sorSzám) throws DocumentException {
        
        addMetaAdat(doc);
        addOldal(doc, kBor, vevőNév, vevőCím, vevőAdóSz, vevőBankSz, sorSzám);
    }
    private static void addOldal(Document document,KészBor kBor /*ObservableList<KészBor> kBor*/, String vevőNév, 
                                String vevőCím, String vevőAdóSz, String vevőBankSz, int sorSzám) throws DocumentException {
        
                Paragraph head = new Paragraph();
                head.setAlignment(Element.ALIGN_CENTER);
                Paragraph body = new Paragraph();
                Paragraph left = new Paragraph();
                left.setAlignment(Element.ALIGN_LEFT);
                left.setIndentationLeft(350);
                Paragraph bottom = new Paragraph();
                addEmptyLine(head, 1);
                head.add(new Paragraph("Számla", LARGE));
                addEmptyLine(body, 1);
                body.add(new Paragraph("Számla sorszáma: " + sorSzám, SMALL));
                ügyfélTable(body,vevőNév, vevőCím, vevőAdóSz, vevőBankSz);
                addEmptyLine(body, 1);
                áruTable(body, kBor);
                left.add(new Paragraph("Összesen: "+ (int) osszes + " Ft",NORMAL));
                addEmptyLine(body, 1);
                bottom.add(new Paragraph("Dátum: " + new Date(), SMALL));               
                
                document.add(head);
                document.add(body);
                document.add(left);
                document.add(bottom);
        }

    private static void ügyfélTable(Paragraph category, String vevőNév, 
                                    String vevőCím, String vevőAdóSz, String vevőBankSz) throws BadElementException {
        
                PdfPTable table = new PdfPTable(2);
               
                PdfPCell cEladó = new PdfPCell(new Phrase("Eladó"));
                table.addCell(cEladó);
                PdfPCell cÜgyfél = new PdfPCell(new Phrase("Ügyfél"));
                table.addCell(cÜgyfél);
                
                table.setHeaderRows(1);

                table.addCell("Név: JoBor Kft");
                table.addCell("Név: " + vevőNév);
                table.addCell("Cím: Budapest");
                table.addCell("Cím:" + vevőCím);
                table.addCell("Adószám: 1234567");
                table.addCell("Adószám:"+ vevőAdóSz);
                table.addCell("Bankszámla: 0123456789");
                table.addCell("Bankszámla:"+ vevőBankSz);

                category.add(table);
        }
    private static void áruTable(Paragraph category, KészBor kBor /*ObservableList<KészBor> kBor*/)
                        throws BadElementException {
        
                PdfPTable table = new PdfPTable(5);

                PdfPCell cMegnev = new PdfPCell(new Phrase("Megnevezés"));
                table.addCell(cMegnev);
                PdfPCell cMenny = new PdfPCell(new Phrase("Mennyiség db"));
                table.addCell(cMenny);
                PdfPCell cNettóÉ = new PdfPCell(new Phrase("Nettó érték / db"));
                table.addCell(cNettóÉ);
                PdfPCell cÁfa = new PdfPCell(new Phrase("ÁFA (%)"));
                table.addCell(cÁfa);
                PdfPCell cBruttóÉ = new PdfPCell(new Phrase("Bruttó érték / db"));
                table.addCell(cBruttóÉ);
                
                table.setHeaderRows(1);
                
                String VAT = "27%";
                double áfa = 27;
                
                int brutto = (int)((1+(áfa/100))*kBor.getÁr());
                    
                table.addCell(kBor.getNév());
                table.addCell(Integer.toString(kBor.getDb()));
                table.addCell(Integer.toString(kBor.getÁr()));
                table.addCell(VAT);
                table.addCell(Integer.toString(brutto));
                    
                osszes += brutto*kBor.getDb();
                
//                for (KészBor készBor : kBor) {
//                    
//                    int brutto = (int)((1+(áfa/100))*készBor.getDb());
//                    
//                    table.addCell(készBor.getNév());
//                    table.addCell(Integer.toString(készBor.getDb()));
//                    table.addCell(Integer.toString(készBor.getÁr()));
//                    table.addCell(VAT);
//                    table.addCell(Integer.toString(brutto));
//                    
//                    osszes += brutto;
//                }
                
                category.add(table);
        }      
    private static void addEmptyLine(Paragraph paragraph, int number) {
                for (int i = 0; i < number; i++) {
                        paragraph.add(new Paragraph(" "));
                }
        }
}
