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

    protected final HSSFCellStyle cellStyle;
    protected final HSSFSheet sheet;
    protected final HSSFRow header;

    protected final Map<String, ClassDescriptor> descriptorTable = DatabaseDescriptorFactory.getDescriptorTable();

    public XlsLoader(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle, final String name) {
        this.cellStyle = cellStyle;
        this.sheet = workbook.createSheet(calculateSheetName(name));
        this.sheet.setDefaultColumnWidth((short) 20);
        header = this.sheet.createRow(0);
    }

    protected HSSFCell addColumn(final String classname, final String property) {
        final HSSFCell cell = header.createCell((short) (header.getLastCellNum() + 1));
        cell.setCellStyle(cellStyle);
        cell.setCellValue(columnName(classname, property));
        return cell;
    }

    protected void addKeyColumn(final String property) {
        addColumn(property);
    }

    private HSSFCell addColumn(final String column) {
        final HSSFCell cell = header.createCell((short) (header.getLastCellNum() + 1));
        cell.setCellStyle(cellStyle);
        cell.setCellValue(column);
        return cell;
    }

    private static String calculateSheetName(final String name) {
        return (name.length() <= 31) ? name : name.substring(0, 31);
    }

    private String columnName(final String classname, final String property) {
        final ClassDescriptor classDescriptor = descriptorTable.get(classname);
        final FieldDescriptor fieldDescriptor = classDescriptor.getFieldDescriptorByName(property);
        return fieldDescriptor.getColumnName();
    }

}
