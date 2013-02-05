package net.sourceforge.fenixedu.domain.reports;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet;
import pt.utl.ist.fenix.tools.util.excel.Spreadsheet.Row;

public class EurAceReportFile extends EurAceReportFile_Base {

    public EurAceReportFile() {
        super();
    }

    @Override
    public String getJobName() {
        return "Listagem para EUR-ACE";
    }

    @Override
    protected String getPrefix() {
        return "eurAce";
    }

    @Override
    public void renderReport(Spreadsheet spreadsheet) throws Exception {
        setDegreeHeaders(spreadsheet);
        spreadsheet.setHeader("nome disciplina");
        spreadsheet.setHeader("codigo execucao disciplina");
        spreadsheet.setHeader("id do docente");
        spreadsheet.setHeader("OID execucao disciplina");

        for (final Degree degree : Degree.readNotEmptyDegrees()) {
            if (checkDegreeType(getDegreeType(), degree)) {
                for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
                    if (checkExecutionYear(getExecutionYear(), degreeCurricularPlan)) {
                        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
                            if (checkExecutionYear(getExecutionYear(), curricularCourse)) {
                                for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                                    if (checkExecutionYear(getExecutionYear(), executionCourse)) {
                                        for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
                                            if (professorship.hasTeacher()) {
                                                final Teacher teacher = professorship.getTeacher();
                                                final Row row = spreadsheet.addRow();
                                                setDegreeCells(row, degree);
                                                row.setCell(curricularCourse.getName());
                                                row.setCell(executionCourse.getIdInternal());
                                                row.setCell(teacher.getPerson().getIstUsername());
                                                row.setCell(String.valueOf(executionCourse.getOid()));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
