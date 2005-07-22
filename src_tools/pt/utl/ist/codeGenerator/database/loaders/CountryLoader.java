package pt.utl.ist.codeGenerator.database.loaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCountry;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
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

	public static Map<String, Country> load(final HSSFWorkbook workbook) throws ExcepcaoPersistencia {
        final Map<String, Country> countries = new HashMap<String, Country>();

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentSupport.iniciarTransaccao();

		final HSSFSheet sheet = workbook.getSheet(calculateSheetName(Country.class));
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);

			final String name = row.getCell((short) 0).getStringCellValue();
			final String code = row.getCell((short) 1).getStringCellValue();
			final String nationality = row.getCell((short) 2).getStringCellValue();

			final Country country = new Country();
			country.setName(name);
			country.setCode(code);
			country.setNationality(nationality);

            countries.put(name, country);
		}

        persistentSupport.confirmarTransaccao();

        return countries;
	}

    public static void dump(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle)
            throws ExcepcaoPersistencia {
        final HSSFSheet sheet = createSheet(workbook, cellStyle);

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentSupport.iniciarTransaccao();

        final IPersistentCountry persistentCountry = persistentSupport.getIPersistentCountry();
        final List<ICountry> countries = (List<ICountry>) persistentCountry.readAll(Country.class);
        for (final ICountry country : countries) {
            final HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            final HSSFCell nameCell = row.createCell((short) 0);
            final HSSFCell codeCell = row.createCell((short) 1);
            final HSSFCell nationalityCell = row.createCell((short) 2);

            nameCell.setCellValue(country.getName());
            codeCell.setCellValue(country.getCode());
            nationalityCell.setCellValue(country.getNationality());
        }

        persistentSupport.confirmarTransaccao();        
    }

    protected static HSSFSheet createSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
        final String sheetname = calculateSheetName(Country.class);
        final HSSFSheet sheet = workbook.getSheet(sheetname);
        if (sheet != null) {
            return sheet;
        }

        addSheet(workbook, cellStyle);
        return workbook.getSheet(sheetname);
    }

}
