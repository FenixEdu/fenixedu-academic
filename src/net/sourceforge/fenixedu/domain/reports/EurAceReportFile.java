package net.sourceforge.fenixedu.domain.reports;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.TeacherMasterDegreeService;
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

    public void renderReport(Spreadsheet spreadsheet) throws Exception {
	setDegreeHeaders(spreadsheet);
	spreadsheet.setHeader("nome disciplina");
	spreadsheet.setHeader("número do docente");
	spreadsheet.setHeader("créditos");

	for (final Degree degree : Degree.readNotEmptyDegrees()) {
	    if (checkDegreeType(getDegreeType(), degree)) {
		for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
		    if (checkExecutionYear(getExecutionYear(), degreeCurricularPlan)) {
			for (final CurricularCourse curricularCourse : degreeCurricularPlan.getAllCurricularCourses()) {
			    if (checkExecutionYear(getExecutionYear(), curricularCourse)) {
				for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
				    for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
					final Teacher teacher = professorship.getTeacher();
					final Row row = spreadsheet.addRow();
					setDegreeCells(row, degree);
					row.setCell(curricularCourse.getName());
					row.setCell(teacher.getTeacherNumber().toString());
					double credits = 0;
					for (final DegreeTeachingService degreeTeachingService : professorship
						.getDegreeTeachingServicesSet()) {
					    credits += degreeTeachingService.calculateCredits();
					}
					for (final TeacherMasterDegreeService teacherMasterDegreeService : professorship
						.getTeacherMasterDegreeServicesSet()) {
					    final Double d = teacherMasterDegreeService.getCredits();
					    if (d != null) {
						credits += d.doubleValue();
					    }
					}
					row.setCell(Double.toString(Math.round((credits * 100.0)) / 100.0));
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
