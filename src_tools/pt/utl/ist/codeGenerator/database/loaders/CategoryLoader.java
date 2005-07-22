package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.teacher.Category;

import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CategoryLoader extends BaseLoader {

	public static void addSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
		final HSSFRow header = createSheet(workbook, Category.class);

		final String classname = Category.class.getName();

        addColumn(cellStyle, header, classname, "longName");
        addColumn(cellStyle, header, classname, "code");
        addColumn(cellStyle, header, classname, "canBeExecutionCourseResponsible");
        addColumn(cellStyle, header, classname, "shortName");
	}

	public static void load(final HSSFWorkbook workbook) {
		PersistenceBrokerFactory.defaultPersistenceBroker().beginTransaction();

		final HSSFSheet sheet = workbook.getSheet(calculateSheetName(Category.class));
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);

			final String longName = row.getCell((short) 1).getStringCellValue();
			final String code = row.getCell((short) 2).getStringCellValue();
			final String canBeExecutionCourseResponsible = row.getCell((short) 3).getStringCellValue();
			final String shortName = row.getCell((short) 4).getStringCellValue();

			final Category category = new Category();
			category.setLongName(longName);
			category.setCode(code);
			category.setCanBeExecutionCourseResponsible(Boolean.valueOf(canBeExecutionCourseResponsible));
			category.setShortName(shortName);
		}

		PersistenceBrokerFactory.defaultPersistenceBroker().commitTransaction();
	}

}
