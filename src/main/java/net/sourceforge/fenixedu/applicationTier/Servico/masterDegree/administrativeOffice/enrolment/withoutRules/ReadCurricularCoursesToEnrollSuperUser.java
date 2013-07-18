package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeAdministrativeOfficeSuperUserAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.EnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import pt.ist.fenixWebFramework.services.Service;

public class ReadCurricularCoursesToEnrollSuperUser extends ReadCurricularCoursesToEnroll {

    @Override
    protected List<CurricularCourse> findCurricularCourses(final List<CurricularCourse> curricularCourses,
            final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester) {

        final List<CurricularCourse> result = new ArrayList<CurricularCourse>();

        for (final CurricularCourse curricularCourse : curricularCourses) {
            if (!studentCurricularPlan.isCurricularCourseApprovedWithoutEquivalencesInCurrentOrPreviousPeriod(curricularCourse,
                    executionSemester)
                    && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, executionSemester)) {

                result.add(curricularCourse);
            }
        }
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCurricularCoursesToEnrollSuperUser serviceInstance = new ReadCurricularCoursesToEnrollSuperUser();

    @Service
    public static List<CurricularCourse2Enroll> runReadCurricularCoursesToEnrollSuperUser(
            final StudentCurricularPlan studentCurricularPlan, final DegreeType degreeType,
            final ExecutionSemester executionSemester, final Integer executionDegreeID, final List<Integer> curricularYearsList,
            final List<Integer> curricularSemestersList) throws FenixServiceException {

        DegreeAdministrativeOfficeSuperUserAuthorizationFilter.instance.execute();
        EnrollmentWithoutRulesAuthorizationFilter.instance.execute(studentCurricularPlan, degreeType);

        return serviceInstance.run(studentCurricularPlan, degreeType, executionSemester, executionDegreeID, curricularYearsList,
                curricularSemestersList);
    }

}