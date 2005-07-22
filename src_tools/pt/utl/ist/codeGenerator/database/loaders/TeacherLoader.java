package pt.utl.ist.codeGenerator.database.loaders;

import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TeacherLoader extends PersonLoader {

	public static void addSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
		final HSSFRow header = createSheet(workbook, Teacher.class);

		PersonLoader.addSheet(workbook, cellStyle, header);

		final String classname = Teacher.class.getName();

		addColumn(cellStyle, header, classname, "teacherNumber");
		addColumn(cellStyle, header, "CATEGORY");
	}

	public static void load(final HSSFWorkbook workbook, final Map<String, Country> countries, final Map<String, Category> categories)
            throws ExcepcaoPersistencia {
		final String sheetname = calculateSheetName(Teacher.class);

        final Map<String, Person> people = PersonLoader.load(workbook, sheetname, countries);

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentSupport.iniciarTransaccao();

        final IPersistentRole persistentRole = persistentSupport.getIPersistentRole();
        final IRole teacherRole = persistentRole.readByRoleType(RoleType.TEACHER);

		final HSSFSheet sheet = workbook.getSheet(sheetname);
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);

            final String username = row.getCell((short) 0).getStringCellValue();

			final Integer teacherNumber = Integer.valueOf(new Double(row.getCell((short) 34).getNumericCellValue()).intValue());
			final String CATEGORY = row.getCell((short) 35).getStringCellValue();

            final Teacher teacher = new Teacher();
            teacher.setTeacherNumber(teacherNumber);
            teacher.setCategory(categories.get(CATEGORY));

            final Person person = people.get(username);
            teacher.setPerson(person);
            person.getPersonRoles().add(teacherRole);
		}

		persistentSupport.confirmarTransaccao();
	}

    public static void dump(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle)
            throws ExcepcaoPersistencia {
        final HSSFSheet sheet = createSheet(workbook, cellStyle);

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentSupport.iniciarTransaccao();

        int counter = 0;

        final IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        final List<ITeacher> teachers = (List<ITeacher>) persistentTeacher.readAll(Teacher.class);
        for (final ITeacher teacher : teachers) {
            final HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

            PersonLoader.dump(row, teacher.getPerson());

            final HSSFCell teacherNumberCell = row.createCell((short) 33);
            final HSSFCell CATEGORYCell = row.createCell((short) 34);

            teacherNumberCell.setCellValue(teacher.getTeacherNumber());
            CATEGORYCell.setCellValue(teacher.getCategory().getLongName());

            if (counter++ == 500) {
                break;
            }
        }

        persistentSupport.confirmarTransaccao();
    }

    protected static HSSFSheet createSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
        final String sheetname = calculateSheetName(Teacher.class);
        final HSSFSheet sheet = workbook.getSheet(sheetname);
        if (sheet != null) {
            return sheet;
        }

        addSheet(workbook, cellStyle);
        return workbook.getSheet(sheetname);
    }

}
