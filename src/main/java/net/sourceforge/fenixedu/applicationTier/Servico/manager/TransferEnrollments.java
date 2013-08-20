package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class TransferEnrollments {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(final String destinationStudentCurricularPlanId, final String[] enrollmentIDsToTransfer,
            final String destinationCurriculumGroupID) {

        if (!StringUtils.isEmpty(destinationCurriculumGroupID)) {

            CurriculumGroup curriculumGroup = (CurriculumGroup) AbstractDomainObject.fromExternalId(destinationCurriculumGroupID);
            StudentCurricularPlan studentCurricularPlan = curriculumGroup.getStudentCurricularPlan();

            for (final String enrollmentIDToTransfer : enrollmentIDsToTransfer) {
                Enrolment enrolment = (Enrolment) AbstractDomainObject.fromExternalId(enrollmentIDToTransfer);

                fixEnrolmentCurricularCourse(studentCurricularPlan, enrolment);

                enrolment.setCurriculumGroup(curriculumGroup);
                enrolment.removeStudentCurricularPlan();
            }

        } else {

            final StudentCurricularPlan studentCurricularPlan =
                    AbstractDomainObject.fromExternalId(destinationStudentCurricularPlanId);
            for (final String enrollmentIDToTransfer : enrollmentIDsToTransfer) {
                final Enrolment enrollment = (Enrolment) AbstractDomainObject.fromExternalId(enrollmentIDToTransfer);

                fixEnrolmentCurricularCourse(studentCurricularPlan, enrollment);

                if (enrollment.getStudentCurricularPlan() != studentCurricularPlan) {
                    enrollment.setStudentCurricularPlan(studentCurricularPlan);
                    enrollment.removeCurriculumGroup();
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