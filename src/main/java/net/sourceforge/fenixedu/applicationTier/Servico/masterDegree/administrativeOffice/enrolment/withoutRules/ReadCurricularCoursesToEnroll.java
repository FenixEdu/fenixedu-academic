package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.enrolment.withoutRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.EnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.enrollment.MasterDegreeEnrollmentWithoutRulesAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadCurricularCoursesToEnroll {

    private static final int MAX_CURRICULAR_YEARS = 5;

    private static final int MAX_CURRICULAR_SEMESTERS = 2;

    protected List<CurricularCourse> findCurricularCourses(final List<CurricularCourse> curricularCourses,
            final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester) {

        final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (final CurricularCourse curricularCourse : curricularCourses) {
            if (!studentCurricularPlan.isCurricularCourseNotExtraApprovedInCurrentOrPreviousPeriod(curricularCourse,
                    executionSemester)
                    && !studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, executionSemester)) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    public List<CurricularCourse2Enroll> run(final StudentCurricularPlan studentCurricularPlan, final DegreeType degreeType,
            final ExecutionSemester executionSemester, final String executionDegreeID, final List<Integer> curricularYearsList,
            final List<Integer> curricularSemestersList) throws FenixServiceException {

        if (studentCurricularPlan == null) {
            throw new FenixServiceException("error.student.curriculum.noCurricularPlans");
        }

        final ExecutionDegree executionDegree = AbstractDomainObject.fromExternalId(executionDegreeID);
        if (executionDegree == null) {
            throw new FenixServiceException("error.degree.noData");
        }

        if (executionDegree.getDegreeCurricularPlan() == null) {
            throw new FenixServiceException("error.degree.noData");
        }

        final List<CurricularCourse> possibleStudentCurricularCoursesToEnrol =
                findCurricularCourses(executionDegree.getDegreeCurricularPlan().getCurricularCourses(), studentCurricularPlan,
                        executionSemester);

        final List<CurricularCourse> searchedCurricularCourses;

        if (filterByYearAndSemester(curricularYearsList, curricularSemestersList)) {
            if (studentCurricularPlan.getRegistration().getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
                searchedCurricularCourses =
                        getActiveAndFilteredCurricularCoursesForBolonhaDegrees(possibleStudentCurricularCoursesToEnrol,
                                verifyYears(curricularYearsList), verifySemesters(curricularSemestersList), executionSemester);
            } else {
                searchedCurricularCourses =
                        getActiveAndFilteredCurricularCourses(possibleStudentCurricularCoursesToEnrol,
                                verifyYears(curricularYearsList), verifySemesters(curricularSemestersList), executionSemester);
            }

        } else {
            if (studentCurricularPlan.getRegistration().getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
                searchedCurricularCourses =
                        getActiveCurricularCoursesForBolonha(possibleStudentCurricularCoursesToEnrol, executionSemester);
            } else {
                searchedCurricularCourses =
                        getActiveCurricularCourses(possibleStudentCurricularCoursesToEnrol, executionSemester);
            }
        }

        return createCurricularCourses2EnrolFrom(searchedCurricularCourses);
    }

    private List<CurricularCourse> getActiveCurricularCoursesForBolonha(
            List<CurricularCourse> possibleStudentCurricularCoursesToEnrol, ExecutionSemester executionSemester) {
        final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (final CurricularCourse curricularCourse : possibleStudentCurricularCoursesToEnrol) {
            if (!curricularCourse.getParentContextsByExecutionSemester(executionSemester).isEmpty()) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    private List<CurricularCourse> getActiveAndFilteredCurricularCoursesForBolonhaDegrees(
            List<CurricularCourse> possibleStudentCurricularCoursesToEnrol, List<Integer> curricularYears,
            List<Integer> curricularSemesters, ExecutionSemester executionSemester) {
        final List<CurricularCourse> result = new ArrayList<CurricularCourse>();

        for (final CurricularCourse curricularCourse : possibleStudentCurricularCoursesToEnrol) {

            List<Context> contexts = curricularCourse.getParentContextsByExecutionSemester(executionSemester);
            for (Context context : contexts) {
                Integer year = context.getCurricularPeriod().getOrderByType(AcademicPeriod.YEAR);
                Integer semester = context.getCurricularPeriod().getOrderByType(AcademicPeriod.SEMESTER);
                if ((year == null || curricularYears.contains(year)) && curricularSemesters.contains(semester)) {
                    result.add(curricularCourse);
                    break;
                }
            }
        }
        return result;
    }

    private List<CurricularCourse2Enroll> createCurricularCourses2EnrolFrom(List<CurricularCourse> searchedCurricularCourses) {

        final List<CurricularCourse2Enroll> result = new ArrayList<CurricularCourse2Enroll>();
        for (final CurricularCourse curricularCourse : searchedCurricularCourses) {
            result.add(new CurricularCourse2Enroll(curricularCourse, CurricularCourseEnrollmentType.DEFINITIVE, false));
        }
        return result;
    }

    private List<CurricularCourse> getActiveAndFilteredCurricularCourses(
            List<CurricularCourse> possibleStudentCurricularCoursesToEnrol, List<Integer> curricularYears,
            List<Integer> curricularSemesters, ExecutionSemester executionSemester) {

        final List<CurricularCourse> result = new ArrayList<CurricularCourse>();

        for (final CurricularCourse curricularCourse : possibleStudentCurricularCoursesToEnrol) {
            for (final CurricularCourseScope scope : curricularCourse.getActiveScopesInExecutionPeriod(executionSemester)) {

                if (curricularYears.contains(scope.getCurricularSemester().getCurricularYear().getYear())
                        && curricularSemesters.contains(scope.getCurricularSemester().getSemester())) {

                    result.add(curricularCourse);
                    break;
                }
            }
        }
        return result;
    }

    private List<CurricularCourse> getActiveCurricularCourses(List<CurricularCourse> possibleStudentCurricularCoursesToEnrol,
            ExecutionSemester executionSemester) {

        final List<CurricularCourse> result = new ArrayList<CurricularCourse>();
        for (final CurricularCourse curricularCourse : possibleStudentCurricularCoursesToEnrol) {
            if (curricularCourse.hasActiveScopesInExecutionPeriod(executionSemester)) {
                result.add(curricularCourse);
            }
        }
        return result;
    }

    private boolean filterByYearAndSemester(List<Integer> curricularYearsList, List<Integer> curricularSemestersList) {
        return (!curricularYearsList.isEmpty() || !curricularSemestersList.isEmpty());
    }

    private List<Integer> verifyYears(List<Integer> curricularYearsList) {
        return curricularYearsList.isEmpty() ? getListOfChosenCurricularYears() : curricularYearsList;
    }

    private List<Integer> getListOfChosenCurricularYears() {
        final List<Integer> result = new ArrayList<Integer>();
        for (int i = 1; i <= MAX_CURRICULAR_YEARS; i++) {
            result.add(Integer.valueOf(i));
        }
        return result;
    }

    private List<Integer> verifySemesters(List curricularSemestersList) {
        return curricularSemestersList.isEmpty() ? getListOfChosenCurricularSemesters() : curricularSemestersList;
    }

    private List<Integer> getListOfChosenCurricularSemesters() {
        final List<Integer> result = new ArrayList<Integer>();
        for (int i = 1; i <= MAX_CURRICULAR_SEMESTERS; i++) {
            result.add(Integer.valueOf(i));
        }
        return result;
    }

    // Service Invokers migrated from Berserk

    private static final ReadCurricularCoursesToEnroll serviceInstance = new ReadCurricularCoursesToEnroll();

    @Service
    public static List<CurricularCourse2Enroll> runReadCurricularCoursesToEnroll(StudentCurricularPlan studentCurricularPlan,
            DegreeType degreeType, ExecutionSemester executionSemester, String executionDegreeID,
            List<Integer> curricularYearsList, List<Integer> curricularSemestersList) throws FenixServiceException,
            NotAuthorizedException {
        try {
            EnrollmentWithoutRulesAuthorizationFilter.instance.execute(studentCurricularPlan, degreeType);
            return serviceInstance.run(studentCurricularPlan, degreeType, executionSemester, executionDegreeID,
                    curricularYearsList, curricularSemestersList);
        } catch (NotAuthorizedException ex1) {
            try {
                MasterDegreeEnrollmentWithoutRulesAuthorizationFilter.instance.execute(studentCurricularPlan, degreeType);
                return serviceInstance.run(studentCurricularPlan, degreeType, executionSemester, executionDegreeID,
                        curricularYearsList, curricularSemestersList);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}