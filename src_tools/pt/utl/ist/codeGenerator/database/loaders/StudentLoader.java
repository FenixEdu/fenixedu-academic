package pt.utl.ist.codeGenerator.database.loaders;

import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class StudentLoader extends PersonLoader {

	public static void addSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
		final HSSFRow header = createSheet(workbook, Student.class);

		PersonLoader.addSheet(workbook, cellStyle, header);

		final String classname = Student.class.getName();

        addColumn(cellStyle, header, classname, "number");
        addColumn(cellStyle, header, classname, "degreeType");
        addColumn(cellStyle, header, classname, "state");
        addColumn(cellStyle, header, classname, "entryPhase");
        addColumn(cellStyle, header, classname, "entryGrade");
	}

	public static void load(final HSSFWorkbook workbook, final Map<String, Country> countries)
            throws ExcepcaoPersistencia {
		final String sheetname = calculateSheetName(Student.class);

        final Map<String, Person> people = PersonLoader.load(workbook, sheetname, countries);

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentSupport.iniciarTransaccao();

        final IPersistentRole persistentRole = persistentSupport.getIPersistentRole();
        final IRole studentRole = persistentRole.readByRoleType(RoleType.STUDENT);

		final HSSFSheet sheet = workbook.getSheet(sheetname);
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			final HSSFRow row = sheet.getRow(i);

            final String username = row.getCell((short) 0).getStringCellValue();

			final Integer number = Integer.valueOf(new Double(row.getCell((short) 34).getNumericCellValue()).intValue());
			final String degreeType = row.getCell((short) 35).getStringCellValue();
			final String state = row.getCell((short) 36).getStringCellValue();
			final String entryPhase = row.getCell((short) 37).getStringCellValue();
			final double entryGrade = row.getCell((short) 38).getNumericCellValue();

            final Student student = new Student();
            student.setNumber(number);
            student.setDegreeType(DegreeType.valueOf(degreeType));
            //student.setState(StudentState.);
            //student.setEntryPhase(EntryPhase.);
            student.setEntryGrade(Double.valueOf(entryGrade));

            final Person person = people.get(username);
            student.setPerson(person);
            person.getPersonRoles().add(studentRole);
		}

        persistentSupport.confirmarTransaccao();
	}

    public static void dump(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle)
            throws ExcepcaoPersistencia {
        final HSSFSheet sheet = createSheet(workbook, cellStyle);

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentSupport.iniciarTransaccao();

        int counter = 0;

        final IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        final List<IStudent> students = (List<IStudent>) persistentStudent.readAll(Student.class);
        for (final IStudent student : students) {
            final HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

            PersonLoader.dump(row, student.getPerson());

            final HSSFCell numberCell = row.createCell((short) 34);
            final HSSFCell degreeTypeCell = row.createCell((short) 35);
            final HSSFCell stateCell = row.createCell((short) 36);
            final HSSFCell entryPhaseCell = row.createCell((short) 37);
            final HSSFCell entryGradeCell = row.createCell((short) 38);

            numberCell.setCellValue(student.getNumber());
            degreeTypeCell.setCellValue(student.getDegreeType().toString());
            stateCell.setCellValue(student.getState().toString());
            entryPhaseCell.setCellValue(student.getEntryPhase().toString());
            if (student.getEntryGrade() != null) {
                entryGradeCell.setCellValue(student.getEntryGrade());
            }

            if (counter++ == 500) {
                break;
            }
        }

        persistentSupport.confirmarTransaccao();
    }

    protected static HSSFSheet createSheet(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
        final String sheetname = calculateSheetName(Student.class);
        final HSSFSheet sheet = workbook.getSheet(sheetname);
        if (sheet != null) {
            return sheet;
        }

        addSheet(workbook, cellStyle);
        return workbook.getSheet(sheetname);
    }

}
