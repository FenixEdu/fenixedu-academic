package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Country;

import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CountryLoader extends BaseLoader {

	public static void addSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
		final HSSFRow header = createSheet(workbook, Country.class);

		final String classname = Country.class.getName();

        addColumn(cellStyle, header, classname, "name");
        addColumn(cellStyle, header, classname, "code");
        addColumn(cellStyle, header, classname, "nationality");
	}

	public static void load(final HSSFWorkbook workbook) {
		PersistenceBrokerFactory.defaultPersistenceBroker().beginTransaction();

		final HSSFSheet sheet = workbook.getSheet(calculateSheetName(Country.class));
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);

			final String name = row.getCell((short) 1).getStringCellValue();
			final String code = row.getCell((short) 2).getStringCellValue();
			final String nationality = row.getCell((short) 3).getStringCellValue();

			final Country country = new Country();
			country.setName(name);
			country.setCode(code);
			country.setNationality(nationality);
		}

		PersistenceBrokerFactory.defaultPersistenceBroker().commitTransaction();
	}

}
