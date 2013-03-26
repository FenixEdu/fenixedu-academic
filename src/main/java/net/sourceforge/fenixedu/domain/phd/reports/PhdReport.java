package net.sourceforge.fenixedu.domain.phd.reports;

import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

public abstract class PhdReport {

    private CellStyle headerStyle;

    protected HSSFWorkbook workbook;

    protected PhdReport(final HSSFWorkbook workbook) {
        this.workbook = workbook;
        this.headerStyle = headerBackgroundStyle();
    }

    protected abstract void setHeaders(final HSSFSheet sheet);

    protected CellStyle headerBackgroundStyle() {
        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
        style.setFillPattern(CellStyle.BIG_SPOTS);

        return style;
    }

    protected String onNullEmptyString(Object value) {

        if (value == null) {
            return "";
        } else if (value instanceof String) {
            return (String) value;
        } else if (value instanceof DateTime) {
            return ((DateTime) value).toString("dd/MM/yyyy");
        } else if (value instanceof LocalDate) {
            return ((LocalDate) value).toString("dd/MM/yyyy");
        } else if (value instanceof YearMonthDay) {
            return ((YearMonthDay) value).toString("dd/MM/yyyy");
        } else if (value instanceof Boolean) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice");
            return bundle.getString(((Boolean) value) ? "label.yes" : "label.no");
        }

        return value.toString();
    }

    protected void addCellValue(HSSFRow row, String value, int cellNumber) {
        HSSFCell cell = row.createCell(cellNumber);
        cell.setCellValue(value);
    }

    protected void addHeaderCell(HSSFSheet sheet, String value, int columnNumber) {
        HSSFRow row = sheet.getRow(0);
        if (row == null) {
            row = sheet.createRow(0);
        }

        HSSFCell cell = row.createCell(columnNumber);

        cell.setCellValue(value);
        cell.setCellStyle(headerStyle);

        cell.setCellValue(value);

        sheet.addMergedRegion(new CellRangeAddress(0, 1, columnNumber, columnNumber));
    }

}
