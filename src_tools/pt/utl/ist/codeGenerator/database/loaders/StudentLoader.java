package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Student;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class StudentLoader extends PersonLoader {

	public static void addSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
		final HSSFRow header = createSheet(workbook, Country.class);

		PersonLoader.addSheet(workbook, cellStyle, header);

		final String classname = Student.class.getName();

        addColumn(cellStyle, header, classname, "number");
        addColumn(cellStyle, header, classname, "degreeType");
        addColumn(cellStyle, header, classname, "state");
        addColumn(cellStyle, header, classname, "entryPhase");
        addColumn(cellStyle, header, classname, "entryGrade");
	}

}
