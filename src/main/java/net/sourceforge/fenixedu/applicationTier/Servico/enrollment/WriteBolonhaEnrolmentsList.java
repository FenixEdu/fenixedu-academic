package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.EnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.MasterDegreeEnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentInOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;

public class WriteBolonhaEnrolmentsList extends WriteEnrollmentsList {

    @Override
    protected void createEnrollment(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, CurricularCourseEnrollmentType enrollmentType, Integer enrollmentClass,
            User userView) {
        final Enrolment enrollment =
                studentCurricularPlan.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionSemester);

        if (enrollment == null) {

            if (enrollmentClass == null || enrollmentClass.intValue() == 0 || enrollmentClass.intValue() == 1) {

                new Enrolment(studentCurricularPlan, studentCurricularPlan.getRoot(), curricularCourse, executionSemester,
                        getEnrollmentCondition(enrollmentType), userView.getUsername());

            } else if (enrollmentClass.intValue() == 2) {

                new EnrolmentInOptionalCurricularCourse(studentCurricularPlan, studentCurricularPlan.getRoot(), curricularCourse,
                        executionSemester, getEnrollmentCondition(enrollmentType), userView.getUsername());

            } else {
                new Enrolment(studentCurricularPlan, studentCurricularPlan.getRoot(), curricularCourse, executionSemester,
                        getEnrollmentCondition(enrollmentType), userView.getUsername()).markAsExtraCurricular();
            }

        } else {
            if (enrollment.getEnrolmentCondition() == EnrollmentCondition.INVISIBLE) {
                enrollment.setEnrolmentCondition(getEnrollmentCondition(enrollmentType));
            }
            if (enrollment.isAnnulled()) {
                enrollment.setEnrollmentState(EnrollmentState.ENROLLED);
            }
        }

    }

    // Service Invokers migrated from Berserk

    private static final WriteBolonhaEnrolmentsList serviceInstance = new WriteBolonhaEnrolmentsList();

    @Atomic
    public static void runWriteBolonhaEnrolmentsList(final StudentCurricularPlan studentCurricularPlan, DegreeType degreeType,
            ExecutionSemester executionSemester, List<String> curricularCourses, Map optionalEnrollments, User userView)
            throws FenixServiceException {
        try {
            EnrollmentWithoutRulesAuthorizationFilter.instance.execute(studentCurricularPlan, degreeType);
            serviceInstance.run(studentCurricularPlan, degreeType, executionSemester, curricularCourses, optionalEnrollments,
                    userView);
        } catch (NotAuthorizedException ex1) {
            try {
                MasterDegreeEnrollmentWithoutRulesAuthorizationFilter.instance.execute(studentCurricularPlan, degreeType);
                serviceInstance.run(studentCurricularPlan, degreeType, executionSemester, curricularCourses, optionalEnrollments,
                        userView);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }
}