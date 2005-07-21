package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Student;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class StudentLoader extends PersonLoader {

    public StudentLoader(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
        super(workbook, cellStyle, Student.class);

        addColumn("number");
        addColumn("degreeType");
        addColumn("state");
        addColumn("entryPhase");
        addColumn("entryGrade");
    }

    private void addColumn(final String property) {
        addColumn(Student.class.getName(), property);
    }

}
