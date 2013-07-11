package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class TransferEnrollments {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static void run(final String destinationStudentCurricularPlanId, final String[] enrollmentIDsToTransfer,
            final String destinationCurriculumGroupID) {

        if (!StringUtils.isEmpty(destinationCurriculumGroupID)) {

            CurriculumGroup curriculumGroup = (CurriculumGroup) FenixFramework.getDomainObject(destinationCurriculumGroupID);
            StudentCurricularPlan studentCurricularPlan = curriculumGroup.getStudentCurricularPlan();

            for (final String enrollmentIDToTransfer : enrollmentIDsToTransfer) {
                Enrolment enrolment = (Enrolment) FenixFramework.getDomainObject(enrollmentIDToTransfer);

                fixEnrolmentCurricularCourse(studentCurricularPlan, enrolment);

                enrolment.setCurriculumGroup(curriculumGroup);
                enrolment.setStudentCurricularPlan(null);
            }

        } else {

            final StudentCurricularPlan studentCurricularPlan =
                    FenixFramework.getDomainObject(destinationStudentCurricularPlanId);
            for (final String enrollmentIDToTransfer : enrollmentIDsToTransfer) {
                final Enrolment enrollment = (Enrolment) FenixFramework.getDomainObject(enrollmentIDToTransfer);

                fixEnrolmentCurricularCourse(studentCurricularPlan, enrollment);

                if (enrollment.getStudentCurricularPlan() != studentCurricularPlan) {
                    enrollment.setStudentCurricularPlan(studentCurricularPlan);
                    enrollment.setCurriculumGroup(null);
                }

            }
        }
    }

    private static void fixEnrolmentCurricularCourse(final StudentCurricularPlan studentCurricularPlan, final Enrolment enrollment) {
        if (enrollment.getCurricularCourse().getDegreeCurricularPlan() != studentCurricularPlan.getDegreeCurricularPlan()) {
            CurricularCourse curricularCourse =
                    studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(
                            enrollment.getCurricularCourse().getCode());
            if (curricularCourse != null) {
                enrollment.setCurricularCourse(curricularCourse);
            }
        }
    }

}