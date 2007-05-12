package net.sourceforge.fenixedu.util.report;

import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class StyledExcelSpreadsheet {
    private HSSFWorkbook workbook;

    private HSSFSheet sheet;

    private ExcelStyle excelStyle;

    private HSSFRow currentRow;

    public StyledExcelSpreadsheet(final String worksheetName) {
	workbook = new HSSFWorkbook();
	excelStyle = new ExcelStyle(workbook);
	sheet = workbook.createSheet();
	sheet.setGridsPrinted(false);
    }

    public ExcelStyle getExcelStyle() {
	return excelStyle;
    }

    public HSSFWorkbook getWorkbook() {
	return workbook;
    }

    public HSSFSheet getSheet() {
        return sheet;
    }

    public void addHeader(String value) {
	if (currentRow == null) {
	    newHeaderRow();
	}
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(value);
	cell.setCellStyle(excelStyle.getHeaderStyle());
    }

    public void addHeader(String value, int columnSize) {
	if (currentRow == null) {
	    newHeaderRow();
	}
	short thisCellNumber = (short) (currentRow.getLastCellNum() + 1);
	HSSFCell cell = currentRow.createCell(thisCellNumber);
	cell.setCellValue(value);
	cell.setCellStyle(excelStyle.getHeaderStyle());
	sheet.setColumnWidth(thisCellNumber, (short) columnSize);
    }

    public void addHeader(String value, HSSFCellStyle newStyle) {
	if (currentRow == null) {
	    newHeaderRow();
	}
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(value);
	cell.setCellStyle(newStyle);
    }

    public void addHeader(String value, HSSFCellStyle newStyle, int columnSize) {
	if (currentRow == null) {
	    newHeaderRow();
	}
	short thisCellNumber = (short) (currentRow.getLastCellNum() + 1);
	HSSFCell cell = currentRow.createCell(thisCellNumber);
	cell.setCellValue(value);
	cell.setCellStyle(newStyle);
	sheet.setColumnWidth(thisCellNumber, (short) columnSize);
    }

    public void newHeaderRow() {
	int rowNumber = 0;
	if (currentRow != null) {
	    rowNumber = currentRow.getRowNum() + 1;
	}
	currentRow = sheet.createRow(rowNumber);
    }

    public void newRow() {
	int rowNumber = 0;
	if (currentRow != null) {
	    rowNumber = currentRow.getRowNum() + 1;
	}
	currentRow = sheet.createRow(rowNumber);
	currentRow.setHeight((short) 250);
    }

    public void addCell(String value) {
	if (currentRow == null) {
	    newRow();
	}
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(value);
	cell.setCellStyle(excelStyle.getValueStyle());
    }

    public void addCell(String value, HSSFCellStyle newStyle) {
	if (currentRow == null) {
	    newRow();
	}
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(value);
	cell.setCellStyle(newStyle);
    }

}
