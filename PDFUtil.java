package PaymentEmail;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.awt.Color;
import java.util.LinkedHashMap;

/**
 *
 * 
 */
public class PDFUtil {

    private boolean colorToggle  = false;
    private static final Color GREY = Color.decode("#f2f2f2");
    private static final Color DARK_GREY = Color.decode("#e6e6e6");
    
    public void addRows(LinkedHashMap<String, String> rows, PdfPTable table) {

        PdfPCell cell;
        for (String key: rows.keySet()) {
            Color bgc = getNextColor();
            cell = new PdfPCell(new Paragraph(key));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            cell.setBorder(0);
            cell.setPadding(10f);
            cell.setBackgroundColor(bgc);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(rows.get(key)));
            cell.setBorder(0);
            cell.setPadding(10f);
            cell.setBackgroundColor(bgc);
            table.addCell(cell);
        }
    }
    
    private Color getNextColor() {
        return (this.colorToggle = !this.colorToggle) ? GREY : DARK_GREY;
    }
}
