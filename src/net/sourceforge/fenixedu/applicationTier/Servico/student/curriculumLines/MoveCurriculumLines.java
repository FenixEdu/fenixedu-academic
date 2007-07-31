package net.sourceforge.fenixedu.applicationTier.Servico.student.curriculumLines;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class MoveCurriculumLines extends Service {

    public void run(final MoveCurriculumLinesBean moveCurriculumLinesBean) {
	final StudentCurricularPlan studentCurricularPlan = moveCurriculumLinesBean.getStudentCurricularPlan();
	studentCurricularPlan.moveCurriculumLines(AccessControl.getPerson(), moveCurriculumLinesBean);
    }

}
