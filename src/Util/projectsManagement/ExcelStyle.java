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

    public static HSSFCellStyle TITLE_STYLE;

    public static HSSFCellStyle HEADER_STYLE;

    public static HSSFCellStyle STRING_STYLE;

    public static HSSFCellStyle DOUBLE_STYLE;

    public static HSSFCellStyle DOUBLE_NEGATIVE_STYLE;

    public static HSSFCellStyle INTEGER_STYLE;

    public static HSSFCellStyle LABEL_STYLE;

    public static HSSFCellStyle VALUE_STYLE;

    public ExcelStyle(HSSFWorkbook wb) {
        setTitleStyle(wb);
        setHeaderStyle(wb);
        setStringStyle(wb);
        setDoubleStyle(wb);
        setDoubleNegativeStyle(wb);
        setIntegerStyle(wb);
        setLabelStyle(wb);
        setValueStyle(wb);
    }

    private void setTitleStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        TITLE_STYLE = style;
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

    private void setLabelStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        LABEL_STYLE = style;
    }

    private void setValueStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        VALUE_STYLE = style;
    }

}
