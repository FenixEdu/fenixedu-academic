package pt.utl.ist.codeGenerator.database.loaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.ICategory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCategory;

import org.apache.poi.hssf.usermodel.HSSFCell;
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

	public static Map<String, Category> load(final HSSFWorkbook workbook) throws ExcepcaoPersistencia {
        final Map<String, Category> categories = new HashMap<String, Category>();

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentSupport.iniciarTransaccao();

		final HSSFSheet sheet = workbook.getSheet(calculateSheetName(Category.class));
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);

			final String longName = row.getCell((short) 0).getStringCellValue();
			final String code = row.getCell((short) 1).getStringCellValue();
			final boolean canBeExecutionCourseResponsible = row.getCell((short) 2).getBooleanCellValue();
			final String shortName = row.getCell((short) 3).getStringCellValue();

			final Category category = new Category();
			category.setLongName(longName);
			category.setCode(code);
			category.setCanBeExecutionCourseResponsible(Boolean.valueOf(canBeExecutionCourseResponsible));
			category.setShortName(shortName);

            categories.put(longName, category);
		}

		persistentSupport.confirmarTransaccao();

        return categories;
	}

    public static void dump(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle)
            throws ExcepcaoPersistencia {
        final HSSFSheet sheet = createSheet(workbook, cellStyle);

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentSupport.iniciarTransaccao();

        final IPersistentCategory persistentCategory = persistentSupport.getIPersistentCategory();
        final List<ICategory> categories = (List<ICategory>) persistentCategory.readAll(Category.class);
        for (final ICategory category : categories) {
            final HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            final HSSFCell longNameCell = row.createCell((short) 0);
            final HSSFCell codeCell = row.createCell((short) 1);
            final HSSFCell canBeExecutionCourseResponsibleCell = row.createCell((short) 2);
            final HSSFCell shortNameCell = row.createCell((short) 3);

            longNameCell.setCellValue(category.getLongName());
            codeCell.setCellValue(category.getCode());
            canBeExecutionCourseResponsibleCell.setCellValue(category.getCanBeExecutionCourseResponsible());
            shortNameCell.setCellValue(category.getShortName());
        }

        persistentSupport.confirmarTransaccao();
    }

    protected static HSSFSheet createSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
        final String sheetname = calculateSheetName(Category.class);
        final HSSFSheet sheet = workbook.getSheet(sheetname);
        if (sheet != null) {
            return sheet;
        }

        addSheet(workbook, cellStyle);
        return workbook.getSheet(sheetname);
    }

}
