/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.util.projectsManagement;

import net.sourceforge.fenixedu.util.FenixUtil;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author Susana Fernandes
 * 
 */
public class ExcelStyle extends FenixUtil {

    private HSSFCellStyle titleStyle;

    private HSSFCellStyle headerStyle;

    private HSSFCellStyle stringStyle;

    private HSSFCellStyle doubleStyle;

    private HSSFCellStyle doubleNegativeStyle;

    private HSSFCellStyle integerStyle;

    private HSSFCellStyle labelStyle;

    private HSSFCellStyle valueStyle;

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
        titleStyle = style;
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
        headerStyle = style;
    }

    private void setStringStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        stringStyle = style;
    }

    private void setDoubleStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
        doubleStyle = style;
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
        doubleNegativeStyle = style;
    }

    private void setIntegerStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(wb.createDataFormat().getFormat("#"));
        integerStyle = style;
    }

    private void setLabelStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        labelStyle = style;
    }

    private void setValueStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 8);
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style.setWrapText(true);
        valueStyle = style;
    }

    public HSSFCellStyle getDoubleNegativeStyle() {
        return doubleNegativeStyle;
    }

    public HSSFCellStyle getDoubleStyle() {
        return doubleStyle;
    }

    public HSSFCellStyle getHeaderStyle() {
        return headerStyle;
    }

    public HSSFCellStyle getIntegerStyle() {
        return integerStyle;
    }

    public HSSFCellStyle getLabelStyle() {
        return labelStyle;
    }

    public HSSFCellStyle getStringStyle() {
        return stringStyle;
    }

    public HSSFCellStyle getTitleStyle() {
        return titleStyle;
    }

    public HSSFCellStyle getValueStyle() {
        return valueStyle;
    }

}
