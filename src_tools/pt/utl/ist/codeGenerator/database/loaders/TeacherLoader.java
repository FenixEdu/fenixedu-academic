package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TeacherLoader extends PersonLoader {

	public static void addSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
		final HSSFRow header = createSheet(workbook, Country.class);

		PersonLoader.addSheet(workbook, cellStyle, header);

		final String classname = Teacher.class.getName();

		addColumn(cellStyle, header, classname, "teacherNumber");
		addColumn(cellStyle, header, "CATEGORY");
		addColumn(cellStyle, header, "SERVICE_PROVIDER_REGIME");
	}

}
