package pt.utl.ist.codeGenerator.database.loaders;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class XlsLoader {

    final HSSFCellStyle cellStyle;
    final HSSFSheet sheet;
    final HSSFRow header;

    public XlsLoader(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle, final String name) {
        this.cellStyle = cellStyle;
        this.sheet = workbook.createSheet(calculateSheetName(name));
        this.sheet.setDefaultColumnWidth((short) 20);
        header = this.sheet.createRow(0);
    }

    protected HSSFCell addColumn(final String column) {
        final HSSFCell cell = header.createCell((short) (header.getRowNum() + 1));
        cell.setCellStyle(cellStyle);
        cell.setCellValue(column);
        return cell;
    }

    private static String calculateSheetName(final String name) {
        return (name.length() <= 31) ? name : name.substring(0, 31);
    }

}
