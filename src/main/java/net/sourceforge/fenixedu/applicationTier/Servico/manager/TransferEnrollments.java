package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class TransferEnrollments {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(final Integer destinationStudentCurricularPlanId, final Integer[] enrollmentIDsToTransfer,
            final Integer destinationCurriculumGroupID) {

        if (destinationCurriculumGroupID != null) {

            CurriculumGroup curriculumGroup =
                    (CurriculumGroup) RootDomainObject.getInstance().readCurriculumModuleByOID(destinationCurriculumGroupID);
            StudentCurricularPlan studentCurricularPlan = curriculumGroup.getStudentCurricularPlan();

            for (final Integer enrollmentIDToTransfer : enrollmentIDsToTransfer) {
                Enrolment enrolment = (Enrolment) RootDomainObject.getInstance().readCurriculumModuleByOID(enrollmentIDToTransfer);

                fixEnrolmentCurricularCourse(studentCurricularPlan, enrolment);

                enrolment.setCurriculumGroup(curriculumGroup);
                enrolment.removeStudentCurricularPlan();
            }

        } else {

            final StudentCurricularPlan studentCurricularPlan =
                    RootDomainObject.getInstance().readStudentCurricularPlanByOID(destinationStudentCurricularPlanId);
            for (final Integer enrollmentIDToTransfer : enrollmentIDsToTransfer) {
                final Enrolment enrollment = (Enrolment) RootDomainObject.getInstance().readCurriculumModuleByOID(enrollmentIDToTransfer);

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