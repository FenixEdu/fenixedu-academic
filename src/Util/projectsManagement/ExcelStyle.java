/*
 * Created on Jan 12, 2005
 *
 */
package Util.projectsManagement;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import Util.FenixUtil;

/**
 * @author Susana Fernandes
 * 
 */
public class ExcelStyle extends FenixUtil {
    public static HSSFCellStyle HEADER_STYLE;

    public static HSSFCellStyle STRING_STYLE;

    public static HSSFCellStyle DOUBLE_STYLE;

    public static HSSFCellStyle DOUBLE_NEGATIVE_STYLE;

    public static HSSFCellStyle INTEGER_STYLE;

    // public static HSSFCellStyle DATE_STYLE;

    public ExcelStyle(HSSFWorkbook wb) {
        setHeaderStyle(wb);
        setStringStyle(wb);
        setDoubleStyle(wb);
        setDoubleNegativeStyle(wb);
        setIntegerStyle(wb);
        // setDateStyle(wb);
    }

    private void setHeaderStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        HEADER_STYLE = style;
    }

    private void setStringStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        STRING_STYLE = style;
    }

    private void setDoubleStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
        DOUBLE_STYLE = style;
    }

    private void setDoubleNegativeStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
        font.setColor(HSSFColor.RED.index);
        DOUBLE_NEGATIVE_STYLE = style;
    }

    private void setIntegerStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(wb.createDataFormat().getFormat("#"));
        INTEGER_STYLE = style;
    }
    // private void setDateStyle(HSSFWorkbook wb) {
    // HSSFCellStyle style = wb.createCellStyle();
    // HSSFFont font = wb.createFont();
    // font.setColor(HSSFColor.BLACK.index);
    // font.setFontHeightInPoints((short) 8);
    // style.setFont(font);
    // style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    // style.setDataFormat(wb.createDataFormat().getFormat("#"));
    // INTEGER_STYLE = style;
    // }

}
