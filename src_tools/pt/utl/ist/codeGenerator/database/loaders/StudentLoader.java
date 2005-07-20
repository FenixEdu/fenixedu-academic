package pt.utl.ist.codeGenerator.database.loaders;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class StudentLoader extends XlsLoader {

    public StudentLoader(HSSFWorkbook workbook, HSSFCellStyle cellStyle, String name) {
        super(workbook, cellStyle, name);
    }

}
