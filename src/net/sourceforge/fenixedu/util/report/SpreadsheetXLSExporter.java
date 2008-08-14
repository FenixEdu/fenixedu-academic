package net.sourceforge.fenixedu.util.report;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.report.Spreadsheet.Row;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class SpreadsheetXLSExporter {

    public void exportToXLSSheet(final Spreadsheet spreadsheet, final OutputStream outputStream) throws IOException {
	final HSSFWorkbook workbook = new HSSFWorkbook();
	final ExcelStyle excelStyle = new ExcelStyle(workbook);
	exportToXLSSheet(workbook, spreadsheet, excelStyle.getHeaderStyle(), excelStyle.getStringStyle());
	workbook.write(outputStream);
    }

    public void exportToXLSSheet(final Spreadsheet spreadsheet, final File file) throws IOException {
	BufferedOutputStream outputStream = null;
	try {
	    outputStream = new BufferedOutputStream(new FileOutputStream(file));
	    exportToXLSSheet(spreadsheet, outputStream);
	} catch (FileNotFoundException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    throw new RuntimeException(e);
	} finally {
	    if (outputStream != null) {
		try {
		    outputStream.close();
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}
	    }
	}
    }

    public void exportToXLSSheets(final OutputStream outputStream, final Spreadsheet... spreadsheets) throws IOException {
	final HSSFWorkbook workbook = new HSSFWorkbook();
	final ExcelStyle excelStyle = new ExcelStyle(workbook);
	for (final Spreadsheet spreadsheet : spreadsheets) {
	    exportToXLSSheet(workbook, spreadsheet, excelStyle.getHeaderStyle(), excelStyle.getStringStyle());
	}
	workbook.write(outputStream);
    }

    public void exportToXLSSheets(final OutputStream outputStream, final List<Spreadsheet> spreadsheets) throws IOException {
	final HSSFWorkbook workbook = new HSSFWorkbook();
	final ExcelStyle excelStyle = new ExcelStyle(workbook);
	for (final Spreadsheet spreadsheet : spreadsheets) {
	    exportToXLSSheet(workbook, spreadsheet, excelStyle.getHeaderStyle(), excelStyle.getStringStyle());
	}
	workbook.write(outputStream);
    }

    public void exportToXLSSheet(HSSFWorkbook workbook, Spreadsheet spreadsheet, HSSFCellStyle headerCellStyle,
	    HSSFCellStyle cellStyle) {

	final HSSFSheet sheet = workbook.createSheet(spreadsheet.getName());
	sheet.setDefaultColumnWidth((short) 20);

	exportXLSHeaderLine(sheet, headerCellStyle, spreadsheet.getHeader());

	for (final Row row : spreadsheet.getRows()) {
	    exportXLSRowLine(sheet, cellStyle, row.getCells());
	}
    }

    protected void exportXLSHeaderLine(final HSSFSheet sheet, final HSSFCellStyle cellStyle, final List<Object> cells) {
	exportXLSLine(sheet, cellStyle, cells, 0);
    }

    protected void exportXLSRowLine(final HSSFSheet sheet, final HSSFCellStyle cellStyle, final List<Object> cells) {
	exportXLSLine(sheet, cellStyle, cells, 1);
    }

    protected void exportXLSLine(final HSSFSheet sheet, final HSSFCellStyle cellStyle, final List<Object> cells, final int offset) {
	final HSSFRow row = sheet.createRow(sheet.getLastRowNum() + offset);
	for (final Object cellValue : cells) {
	    addColumn(cellStyle, row, cellValue);
	}
    }

    protected HSSFCell addColumn(final HSSFCellStyle cellStyle, final HSSFRow row, final Object cellValue) {
	final HSSFCell cell = row.createCell((short) (row.getLastCellNum() + 1));
	cell.setCellStyle(cellStyle);
	if (cellValue != null) {
	    cell.setCellValue(cellValue.toString());
	} else {
	    cell.setCellValue("");
	}
	return cell;
    }

}
