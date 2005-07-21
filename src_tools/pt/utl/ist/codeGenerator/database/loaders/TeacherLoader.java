package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
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

	public static void load(final HSSFWorkbook workbook) {
		final String sheetname = calculateSheetName(Teacher.class);

		PersonLoader.load(workbook, sheetname);

		PersistenceBrokerFactory.defaultPersistenceBroker().beginTransaction();

		final HSSFSheet sheet = workbook.getSheet(sheetname);
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);

			final String teacherNumber = row.getCell((short) 35).getStringCellValue();
			final String CATEGORY = row.getCell((short) 36).getStringCellValue();
			final String SERVICE_PROVIDER_REGIME = row.getCell((short) 37).getStringCellValue();
		}

		PersistenceBrokerFactory.defaultPersistenceBroker().commitTransaction();
	}

}
