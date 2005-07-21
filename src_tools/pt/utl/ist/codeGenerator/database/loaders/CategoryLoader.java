package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.teacher.Category;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CategoryLoader extends XlsLoader {

    public CategoryLoader(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
        super(workbook, cellStyle, Category.class.getSimpleName());

        addColumn("longName");
        addColumn("code");
        addColumn("canBeExecutionCourseResponsible");
        addColumn("shortName");
    }

    private void addColumn(final String property) {
        addColumn(Category.class.getName(), property);
    }

}
