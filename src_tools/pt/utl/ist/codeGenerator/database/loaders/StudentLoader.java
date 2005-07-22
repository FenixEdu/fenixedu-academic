package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Student;

import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
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

	public static void load(final HSSFWorkbook workbook) {
		final String sheetname = calculateSheetName(Student.class);

		PersonLoader.load(workbook, sheetname);

		PersistenceBrokerFactory.defaultPersistenceBroker().beginTransaction();

		final HSSFSheet sheet = workbook.getSheet(sheetname);
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);

			final String number = row.getCell((short) 35).getStringCellValue();
			final String degreeType = row.getCell((short) 36).getStringCellValue();
			final String state = row.getCell((short) 37).getStringCellValue();
			final String entryPhase = row.getCell((short) 38).getStringCellValue();
			final String entryGrade = row.getCell((short) 39).getStringCellValue();
		}

		PersistenceBrokerFactory.defaultPersistenceBroker().commitTransaction();
	}

}
