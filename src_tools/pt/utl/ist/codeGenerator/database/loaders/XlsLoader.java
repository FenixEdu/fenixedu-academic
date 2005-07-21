package pt.utl.ist.codeGenerator.database.loaders;

import java.util.Map;

import org.apache.ojb.broker.metadata.ClassDescriptor;
import org.apache.ojb.broker.metadata.FieldDescriptor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pt.utl.ist.codeGenerator.database.DatabaseDescriptorFactory;

public class XlsLoader {

    protected static final Map<String, ClassDescriptor> descriptorTable = DatabaseDescriptorFactory.getDescriptorTable();

    protected static HSSFRow createSheet(final HSSFWorkbook workbook, final Class domainClass) {
    	final HSSFSheet sheet = workbook.createSheet(calculateSheetName(domainClass.getSimpleName()));
        sheet.setDefaultColumnWidth((short) 20);
        return sheet.createRow(0);
	}

    private static String calculateSheetName(final String name) {
        return (name.length() <= 31) ? name : name.substring(0, 31);
    }

    private static String columnName(final String classname, final String property) {
        final ClassDescriptor classDescriptor = descriptorTable.get(classname);
        final FieldDescriptor fieldDescriptor = classDescriptor.getFieldDescriptorByName(property);
        return fieldDescriptor.getColumnName();
    }

    protected static HSSFCell addColumn(final HSSFCellStyle cellStyle, final HSSFRow row, final String column) {
        final HSSFCell cell = row.createCell((short) (row.getLastCellNum() + 1));
        cell.setCellStyle(cellStyle);
        cell.setCellValue(column);
        return cell;
    }

    protected static HSSFCell addColumn(final HSSFCellStyle cellStyle, final HSSFRow row, final String classname, final String property) {
        final HSSFCell cell = row.createCell((short) (row.getLastCellNum() + 1));
        cell.setCellStyle(cellStyle);
        cell.setCellValue(columnName(classname, property));
        return cell;
    }

}
