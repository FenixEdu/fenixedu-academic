package pt.utl.ist.codeGenerator.database.loaders;

import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TeacherLoader extends PersonLoader {

    public TeacherLoader(final HSSFWorkbook workbook, final HSSFCellStyle cellStyle) {
        super(workbook, cellStyle, Teacher.class);

        addColumn("teacherNumber");
        addKeyColumn("CATEGORY");
        addKeyColumn("SERVICE_PROVIDER_REGIME");
    }

    private void addColumn(final String property) {
        addColumn(Teacher.class.getName(), property);
    }

}
