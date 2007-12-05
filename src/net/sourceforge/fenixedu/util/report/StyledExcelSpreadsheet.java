package net.sourceforge.fenixedu.util.report;

import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;

import org.apache.commons.lang.exception.NestableException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.contrib.HSSFCellUtil;
import org.apache.poi.hssf.usermodel.contrib.HSSFRegionUtil;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class StyledExcelSpreadsheet {
    private HSSFWorkbook workbook;

    private HSSFSheet sheet;

    private ExcelStyle excelStyle;

    private boolean wrapText;

    private static final DateTimeFormatter dateFormat = DateTimeFormat.forPattern("dd/MM/yyyy");

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormat
	    .forPattern("dd/MM/yyyy HH:mm");

    private static final DateTimeFormatter timeFormat = DateTimeFormat.forPattern("HH:mm");

    public StyledExcelSpreadsheet(final String sheetName) {
	workbook = new HSSFWorkbook();
	excelStyle = new ExcelStyle(workbook);
	sheet = workbook.createSheet(sheetName);
	sheet.setGridsPrinted(false);
	wrapText = true;
    }

    public StyledExcelSpreadsheet(final String sheetName, final boolean wrapText) {
	workbook = new HSSFWorkbook();
	excelStyle = new ExcelStyle(workbook);
	sheet = workbook.createSheet(sheetName);
	sheet.setGridsPrinted(false);
	this.wrapText = wrapText;
    }

    public StyledExcelSpreadsheet(final String sheetName, int defaultColumnWith) {
	workbook = new HSSFWorkbook();
	excelStyle = new ExcelStyle(workbook);
	sheet = workbook.createSheet(sheetName);
	sheet.setDefaultColumnWidth((short) defaultColumnWith);
	sheet.setGridsPrinted(false);
	wrapText = true;
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

    public void addHeader(int columnNumber, String value) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) columnNumber);
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

    public void addHeader(String value, int columnSize, int columnNumber) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) columnNumber);
	cell.setCellValue(value);
	cell.setCellStyle(excelStyle.getHeaderStyle());
	sheet.setColumnWidth((short) columnNumber, (short) columnSize);
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

    public void addCell(Object value) {
	addCell(value, getDefaultExcelStyle(value), (getRow().getLastCellNum() + 1));
    }

    public void addCell(Object value, boolean wrap) {
	addCell(value, getDefaultExcelStyle(value), (getRow().getLastCellNum() + 1), wrap);
    }

    public void addCell(Object value, HSSFCellStyle newStyle) {
	addCell(value, newStyle, (getRow().getLastCellNum() + 1));
    }

    public void addCell(Object value, int columnNumber) {
	addCell(value, getDefaultExcelStyle(value), columnNumber);
    }

    public void addCell(Object value, int columnNumber, boolean wrap) {
	addCell(value, getDefaultExcelStyle(value), columnNumber, wrap);
    }

    public void addCell(Object value, HSSFCellStyle newStyle, int columnNumber) {
	addCell(value, newStyle, columnNumber, wrapText);
    }

    private void addCell(Object value, HSSFCellStyle newStyle, int columnNumber, boolean wrap) {
	if (value instanceof String) {
	    addCell((String) value, newStyle, columnNumber, wrap);
	} else if (value instanceof Integer) {
	    addCell((Integer) value, newStyle, columnNumber, wrap);
	} else if (value instanceof Double) {
	    addCell((Double) value, newStyle, columnNumber, wrap);
	}
    }

    private void addCell(String value, HSSFCellStyle newStyle, int columnNumber, boolean wrap) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) columnNumber);
	cell.setCellValue(value);
	cell.setCellStyle(getExcelStyle(newStyle, wrap));
    }

    private void addCell(Double value, HSSFCellStyle newStyle, int columnNumber, boolean wrap) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) columnNumber);
	cell.setCellValue(value);
	cell.setCellStyle(getExcelStyle(newStyle, wrap));
    }

    private void addCell(Integer value, HSSFCellStyle newStyle, int columnNumber, boolean wrap) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) columnNumber);
	cell.setCellValue(value);
	cell.setCellStyle(getExcelStyle(newStyle, wrap));
    }

    public void addDateTimeCell(DateTime value) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(dateTimeFormat.print(value));
	cell.setCellStyle(getExcelStyle(excelStyle.getValueStyle(), wrapText));
    }

    public void addDateCell(YearMonthDay value) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(dateFormat.print(value));
	cell.setCellStyle(getExcelStyle(excelStyle.getValueStyle(), wrapText));
    }

    public void addTimeCell(TimeOfDay value) {
	HSSFRow currentRow = getRow();
	HSSFCell cell = currentRow.createCell((short) (currentRow.getLastCellNum() + 1));
	cell.setCellValue(timeFormat.print(value));
	cell.setCellStyle(getExcelStyle(excelStyle.getValueStyle(), wrapText));
    }

    public void sumColumn(int firstRow, int lastRow, int firstColumn, int lastColumn,
	    HSSFCellStyle newStyle) {
	for (int col = firstColumn; col <= lastColumn; col++) {
	    CellReference cellRef1 = new CellReference(firstRow, col);
	    CellReference cellRef2 = new CellReference(lastRow, col);
	    HSSFRow currentRow = getRow();
	    HSSFCell cell = currentRow.createCell((short) col);
	    cell.setCellStyle(getExcelStyle(newStyle, wrapText));
	    cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
	}
    }

    public void sumRows(int firstRow, int lastRow, int firstColumn, int lastColumn, int increment,
	    HSSFCellStyle newStyle) {
	for (int row = firstRow; row <= lastRow; row++) {
	    CellReference[] refs = new CellReference[lastColumn - firstColumn / increment];
	    for (int colIndex = 0, col = firstColumn; col <= lastColumn; col = col + increment, colIndex++) {
		refs[colIndex] = new CellReference(row, col);
	    }
	    HSSFRow currentRow = sheet.getRow(row);
	    HSSFCell cell = currentRow.createCell((short) (lastColumn + 1));
	    cell.setCellStyle(getExcelStyle(newStyle, wrapText));
	    StringBuilder formula = new StringBuilder();
	    for (int index = 0; index < refs.length; index++) {
		if (refs[index] != null) {
		    if (formula.length() != 0) {
			formula.append(";");
		    }
		    formula.append(refs[index].toString());
		}
	    }
	    formula.append(")");
	    cell.setCellFormula("sum(" + formula.toString());
	}
    }

    protected void setCellBorder(HSSFCell cell) {
	final short borderProperty = HSSFCellStyle.BORDER_THIN;
	try {
	    HSSFCellUtil.setCellStyleProperty(cell, workbook, "borderLeft", borderProperty);
	    HSSFCellUtil.setCellStyleProperty(cell, workbook, "borderRight", borderProperty);
	    HSSFCellUtil.setCellStyleProperty(cell, workbook, "borderTop", borderProperty);
	    HSSFCellUtil.setCellStyleProperty(cell, workbook, "borderBottom", borderProperty);
	} catch (NestableException e) {
	}
    }

    public void setRegionBorder(int firstRow, int lastRow, int firstColumn, int lastColumn) {
	for (int rowIndex = firstRow; rowIndex < lastRow; rowIndex++) {
	    for (int colIndex = firstColumn; colIndex < lastColumn; colIndex++) {
		HSSFRow row = sheet.getRow(rowIndex);
		HSSFCell cell = row.getCell((short) colIndex);
		if (cell == null) {
		    cell = row.createCell((short) colIndex);
		}
		setCellBorder(cell);
	    }
	}
    }

    public void mergeCells(int firstRow, int lastRow, int firstColumn, int lastColumn) {
	Region region = new Region((short) firstRow, (short) firstColumn, (short) lastRow,
		(short) lastColumn);
	getSheet().addMergedRegion(region);
	try {
	    HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, region, getSheet(), getWorkbook());
	    HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, region, getSheet(), getWorkbook());
	    HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, region, getSheet(), getWorkbook());
	    HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, region, getSheet(), getWorkbook());
	    HSSFRegionUtil
		    .setBottomBorderColor(HSSFColor.BLACK.index, region, getSheet(), getWorkbook());
	    HSSFRegionUtil.setTopBorderColor(HSSFColor.BLACK.index, region, getSheet(), getWorkbook());
	    HSSFRegionUtil.setLeftBorderColor(HSSFColor.BLACK.index, region, getSheet(), getWorkbook());
	    HSSFRegionUtil.setRightBorderColor(HSSFColor.BLACK.index, region, getSheet(), getWorkbook());
	} catch (NestableException e) {
	}
    }

    public int getMaxiumColumnNumber() {
	int result = 0;
	for (int row = 0; row <= sheet.getLastRowNum(); row++) {
	    result = sheet.getRow(row).getLastCellNum() > result ? sheet.getRow(row).getLastCellNum()
		    : result;
	}
	return result;
    }

    public void setSheetOrientation() {
	HSSFPrintSetup ps = getSheet().getPrintSetup();
	ps.setLandscape(true);
	getSheet().setMargin(HSSFSheet.TopMargin, 0.10);
	getSheet().setMargin(HSSFSheet.BottomMargin, 0.10);
	getSheet().setMargin(HSSFSheet.LeftMargin, 0.10);
	getSheet().setMargin(HSSFSheet.RightMargin, 0.10);
    }

    private HSSFCellStyle getExcelStyle(HSSFCellStyle style, boolean wrap) {
	if (!wrap) {
	    style.setWrapText(false);
	}
	return style;
    }

    private HSSFCellStyle getDefaultExcelStyle(Object value) {
	if (value instanceof Integer) {
	    return getExcelStyle(getExcelStyle().getIntegerStyle(), wrapText);
	} else if (value instanceof Double) {
	    return getExcelStyle(getExcelStyle().getDoubleStyle(), wrapText);
	}
	return getExcelStyle(getExcelStyle().getValueStyle(), wrapText);
    }
}