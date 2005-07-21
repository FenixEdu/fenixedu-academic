package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.teacher.Category;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CategoryLoader extends XlsLoader {

	public static void addSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
		final HSSFRow header = createSheet(workbook, Category.class);

		final String classname = Category.class.getName();

        addColumn(cellStyle, header, classname, "longName");
        addColumn(cellStyle, header, classname, "code");
        addColumn(cellStyle, header, classname, "canBeExecutionCourseResponsible");
        addColumn(cellStyle, header, classname, "shortName");
	}

}
