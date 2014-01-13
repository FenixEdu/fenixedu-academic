package net.sourceforge.fenixedu.domain.reports;

import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.NonRegularTeachingService;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class TeachersByShiftReportFile extends TeachersByShiftReportFile_Base {

    public TeachersByShiftReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem de docentes associados a turnos";
    }

    @Override
    protected String getPrefix() {
        return "teachersByShift";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) {

        spreadsheet.setHeader("semestre");
        spreadsheet.setHeader("id docente");
        spreadsheet.setHeader("id turno");
        spreadsheet.setHeader("nome turno");
        spreadsheet.setHeader("id execution course");
        spreadsheet.setHeader("% assegurada pelo docente");
        spreadsheet.setHeader("OID execucao disciplina");
        spreadsheet.setHeader("OID professorship");

        //TODO remove when the main external teachers structure is global for everyone
        Set<NonRegularTeachingService> nonRegularTeachingServices = Bennu.getInstance().getNonRegularTeachingServicesSet();

        for (ExecutionSemester executionSemester : getExecutionYear().getExecutionPeriods()) {
            //TODO remove this cycle when the main external teachers structure is global for everyone
            for (NonRegularTeachingService nonRegularTeachingService : nonRegularTeachingServices) {
                if (nonRegularTeachingService.getProfessorship().getExecutionCourse().getExecutionPeriod() == executionSemester) {
                    final Shift shift = nonRegularTeachingService.getShift();

                    if (!shift.hasSchoolClassForDegreeType(getDegreeType())) {
                        continue;
                    }
                    Row row = spreadsheet.addRow();
                    row.setCell(executionSemester.getSemester());
                    row.setCell(nonRegularTeachingService.getProfessorship().getPerson().getIstUsername());
                    row.setCell(shift.getExternalId());
                    row.setCell(shift.getNome());
                    row.setCell(shift.getExecutionCourse().getExternalId());
                    row.setCell(nonRegularTeachingService.getPercentage() != null ? nonRegularTeachingService.getPercentage()
                            .toString().replace('.', ',') : StringUtils.EMPTY);
                    row.setCell(String.valueOf(shift.getExecutionCourse().getOid()));
                    row.setCell(String.valueOf(nonRegularTeachingService.getProfessorship().getOid()));
                }
            }
            for (TeacherService teacherService : executionSemester.getTeacherServices()) {
                for (DegreeTeachingService degreeTeachingService : teacherService.getDegreeTeachingServices()) {

                    final Shift shift = degreeTeachingService.getShift();

                    if (!shift.hasSchoolClassForDegreeType(getDegreeType())) {
                        continue;
                    }

                    Row row = spreadsheet.addRow();
                    row.setCell(executionSemester.getSemester());
                    row.setCell(teacherService.getTeacher().getPerson().getIstUsername());
                    row.setCell(shift.getExternalId());
                    row.setCell(shift.getNome());
                    row.setCell(shift.getExecutionCourse().getExternalId());
                    row.setCell(degreeTeachingService.getPercentage() != null ? degreeTeachingService.getPercentage().toString()
                            .replace('.', ',') : StringUtils.EMPTY);
                    row.setCell(String.valueOf(shift.getExecutionCourse().getOid()));
                    row.setCell(String.valueOf(degreeTeachingService.getProfessorship().getOid()));
                }
            }
        }
    }
}
