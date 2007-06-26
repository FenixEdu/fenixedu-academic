package net.sourceforge.fenixedu.util.report;

import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;

public class StyledExcelSpreadsheet {
    private HSSFWorkbook workbook;

    private HSSFSheet sheet;

    private ExcelStyle excelStyle;

    public StyledExcelSpreadsheet(final String sheetName) {
	workbook = new HSSFWorkbook();
	excelStyle = new ExcelStyle(workbook);
	sheet = workbook.createSheet(sheetName);
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

    public HSSFSheet getSheet(String sheetName) {
	sheet = workbook.getSheet(sheetName);
	if (sheet == null) {
	    sheet = workbook.createSheet(sheetName);
	}
	return sheet;
    }

    public boolean hasSheet(String sheetName) {
	return workbook.getSheet(sheetName) != null;
    }

    public void addHeader(String value) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(value);
	cell.setCellStyle(excelStyle.getHeaderStyle());
    }

    public void addHeader(String value, int columnSize) {
	HSSFRow currentRow = getRow();
	short thisCellNumber = (short) (currentRow.getLastCellNum() + 1);
	HSSFCell cell = currentRow.createCell(thisCellNumber);
	cell.setCellValue(value);
	cell.setCellStyle(excelStyle.getHeaderStyle());
	sheet.setColumnWidth(thisCellNumber, (short) columnSize);
    }

    public void addHeader(String value, HSSFCellStyle newStyle) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(value);
	cell.setCellStyle(newStyle);
    }

    public void addHeader(String value, HSSFCellStyle newStyle, int columnSize) {
	HSSFRow currentRow = getRow();
	short thisCellNumber = (short) (currentRow.getLastCellNum() + 1);
	HSSFCell cell = currentRow.createCell(thisCellNumber);
	cell.setCellValue(value);
	cell.setCellStyle(newStyle);
	sheet.setColumnWidth(thisCellNumber, (short) columnSize);
    }

    public void newHeaderRow() {
	int rowNumber = sheet.getLastRowNum();
	if (rowNumber != 0 || sheet.getRow(rowNumber) != null) {
	    rowNumber += 1;
	}
	sheet.createRow(rowNumber);
    }

    public void newRow() {
	int rowNumber = sheet.getLastRowNum();
	if (rowNumber != 0 || sheet.getRow(rowNumber) != null) {
	    rowNumber += 1;
	}
	HSSFRow currentRow = sheet.createRow(rowNumber);
	currentRow.setHeight((short) 250);
    }

    public HSSFRow getRow() {
	return sheet.getRow(sheet.getLastRowNum());
    }

    public void addCell(String value) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(value);
	cell.setCellStyle(excelStyle.getValueStyle());
    }

    public void addCell(String value, HSSFCellStyle newStyle) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(value);
	cell.setCellStyle(newStyle);
    }

    public void addCell(Double value) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(value);
	cell.setCellStyle(excelStyle.getDoubleStyle());
    }

    public void addCell(Double value, HSSFCellStyle newStyle) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(value);
	cell.setCellStyle(newStyle);
    }

    public void addCell(Double value, HSSFCellStyle newStyle, int columnNumber) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) columnNumber);
	cell.setCellValue(value);
	cell.setCellStyle(newStyle);
    }

    public void addCell(Integer value, int columnNumber) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) columnNumber);
	cell.setCellValue(value);
	cell.setCellStyle(excelStyle.getValueStyle());
    }

    public void sumColumn(int firstRow, int lastRow, int firstColumn, int lastColumn,
	    HSSFCellStyle newStyle) {
	for (int col = firstColumn; col <= lastColumn; col++) {
	    CellReference cellRef1 = new CellReference(firstRow, col);
	    CellReference cellRef2 = new CellReference(lastRow, col);
	    HSSFRow currentRow = getRow();
	    HSSFCell cell = currentRow.createCell((short) col);
	    cell.setCellStyle(newStyle);
	    cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
	}
    }

    public void sumRows(int firstRow, int lastRow, int firstColumn, int lastColumn,
	    HSSFCellStyle newStyle) {
	for (int row = firstRow; row <= lastRow; row++) {
	    CellReference cellRef1 = new CellReference(row, firstColumn);
	    CellReference cellRef2 = new CellReference(row, lastColumn);
	    HSSFRow currentRow = sheet.getRow(row);
	    HSSFCell cell = currentRow.createCell((short) (lastColumn + 1));
	    cell.setCellStyle(newStyle);
	    cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
	}
    }

}
