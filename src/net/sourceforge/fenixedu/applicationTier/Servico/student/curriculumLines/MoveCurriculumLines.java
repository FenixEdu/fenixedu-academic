package net.sourceforge.fenixedu.applicationTier.Servico.student.curriculumLines;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean.EnrolmentLocationBean;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean.OptionalEnrolmentLocationBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class MoveCurriculumLines extends Service {

    public void run(final MoveCurriculumLinesBean moveCurriculumLinesBean) {
	final StudentCurricularPlan studentCurricularPlan = moveCurriculumLinesBean.getStudentCurricularPlan();
	studentCurricularPlan.moveCurriculumLines(AccessControl.getPerson(), moveCurriculumLinesBean);
    }

    public void run(final OptionalCurricularCoursesLocationBean bean) throws FenixServiceException {
	moveEnrolments(bean);
	moveOptionalEnrolments(bean);
	final ExecutionSemester executionPeriod = ExecutionSemester.readActualExecutionPeriod();
	bean.getStudentCurricularPlan().enrol(AccessControl.getPerson(), executionPeriod,
		bean.getIDegreeModulesToEvaluate(executionPeriod), Collections.EMPTY_LIST,
		CurricularRuleLevel.ENROLMENT_WITH_RULES);
    }

    private void moveOptionalEnrolments(final OptionalCurricularCoursesLocationBean bean) {
	for (final OptionalEnrolmentLocationBean line : bean.getOptionalEnrolmentBeans()) {
	    bean.getStudentCurricularPlan().convertOptionalEnrolmentToEnrolment(line.getEnrolment(), line.getCurriculumGroup());
	}
    }

    private void moveEnrolments(final OptionalCurricularCoursesLocationBean bean) throws FenixServiceException {
	for (final EnrolmentLocationBean line : bean.getEnrolmentBeans()) {
	    final CurriculumGroup curriculumGroup = line.getCurriculumGroup(bean.getStudentCurricularPlan());
	    if (curriculumGroup == null) {
		throw new FenixServiceException("error.MoveCurriculumLines.invalid.curriculumGroup");
	    }
	    bean.getStudentCurricularPlan().convertEnrolmentToOptionalEnrolment(line.getEnrolment(), curriculumGroup,
		    line.getOptionalCurricularCourse());
	}
    }

}
