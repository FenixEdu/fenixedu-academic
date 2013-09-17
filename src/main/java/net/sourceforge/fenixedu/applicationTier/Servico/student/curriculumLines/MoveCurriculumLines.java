package net.sourceforge.fenixedu.applicationTier.Servico.student.curriculumLines;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean.EnrolmentLocationBean;
import net.sourceforge.fenixedu.dataTransferObject.student.OptionalCurricularCoursesLocationBean.OptionalEnrolmentLocationBean;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.Atomic;

public class MoveCurriculumLines {

    @Atomic
    public static void run(final MoveCurriculumLinesBean moveCurriculumLinesBean) {
        final StudentCurricularPlan studentCurricularPlan = moveCurriculumLinesBean.getStudentCurricularPlan();
        if (moveCurriculumLinesBean.isWithRules()) {
            studentCurricularPlan.moveCurriculumLines(moveCurriculumLinesBean);
        } else {
            studentCurricularPlan.moveCurriculumLinesWithoutRules(AccessControl.getPerson(), moveCurriculumLinesBean);
        }
    }

    @Atomic
    public static void run(final OptionalCurricularCoursesLocationBean bean) throws FenixServiceException {
        moveEnrolments(bean);
        moveOptionalEnrolments(bean);
    }

    private static void moveOptionalEnrolments(final OptionalCurricularCoursesLocationBean bean) {
        for (final OptionalEnrolmentLocationBean line : bean.getOptionalEnrolmentBeans()) {
            bean.getStudentCurricularPlan().convertOptionalEnrolmentToEnrolment(line.getEnrolment(), line.getCurriculumGroup());
        }
    }

    private static void moveEnrolments(final OptionalCurricularCoursesLocationBean bean) throws FenixServiceException {
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
