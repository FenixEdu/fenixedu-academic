/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ComputeCurricularCourseStatistics {

    @Atomic
    public static String run(String degreeCurricularPlanID, String executionYearID, RegistrationAgreement agreement) {

        ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearID);
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
        Collection<CurricularCourse> curricularCourses = degreeCurricularPlan.getCurricularCourses();

        Formatter result = new Formatter();

        for (CurricularCourse curricularCourse : curricularCourses) {

            Collection<ExecutionSemester> executionSemesters = executionYear.getExecutionPeriods();
            for (ExecutionSemester executionSemester : executionSemesters) {

                // Get Scopes
                // List<CurricularCourseScope> scopes = curricularCourse
                // .getActiveScopesInExecutionPeriod(executionPeriod);

                List<DegreeModuleScope> scopes = curricularCourse.getActiveDegreeModuleScopesInExecutionPeriod(executionSemester);

                if (scopes.isEmpty()) {
                    continue;
                }

                // Get max Year and its Semester
                int year = 0;
                int semester = 0;
                // for (CurricularCourseScope scope : scopes) {
                // Integer scopeYear =
                // scope.getCurricularSemester().getCurricularYear().getYear();
                // if (scopeYear > year) {
                // year = scopeYear;
                // semester = scope.getCurricularSemester().getSemester();
                // }
                // }

                for (DegreeModuleScope scope : scopes) {
                    Integer scopeYear = scope.getCurricularYear();
                    if (scopeYear > year) {
                        year = scopeYear;
                        semester = scope.getCurricularSemester();
                    }
                }

                // Get Execution Course
                List<ExecutionCourse> executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester);
                // if (executionCourses.isEmpty() || executionCourses.size() >
                // 1) {
                // // Houston, we have a problem...!!
                // continue;
                // }
                // ExecutionCourse executionCourse = executionCourses.iterator().next();

                if (executionCourses.size() == 1) {
                    // Organize enrolments by DegreeCurricularPlans
                    Map<DegreeCurricularPlan, Collection<Enrolment>> enrolmentsMap =
                            organizeEnrolmentsByDCP(curricularCourse, executionSemester, agreement, null);

                    calculateEnrolmentsForDCP(result, curricularCourse, executionSemester, year, semester, executionCourses
                            .iterator().next(), enrolmentsMap);
                } else {
                    for (ExecutionCourse executionCourse : executionCourses) {
                        // Organize enrolments by DegreeCurricularPlans
                        Map<DegreeCurricularPlan, Collection<Enrolment>> enrolmentsMap =
                                organizeEnrolmentsByDCP(curricularCourse, executionSemester, agreement, executionCourse);

                        calculateEnrolmentsForDCP(result, curricularCourse, executionSemester, year, semester, executionCourse,
                                enrolmentsMap);
                    }
                }

            }
        }

        return result.toString();
    }

    private static void calculateEnrolmentsForDCP(Formatter result, CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, int year, int semester, ExecutionCourse executionCourse,
            Map<DegreeCurricularPlan, Collection<Enrolment>> enrolmentsMap) {
        // Calculate enrolments for each DegreeCurricularPlan
        for (DegreeCurricularPlan enrolmentDCP : enrolmentsMap.keySet()) {

            int firstEnrolledCount = 0;
            int secondEnrolledCount = 0;

            Collection<Enrolment> dcpEnrolments = enrolmentsMap.get(enrolmentDCP);
            for (Enrolment enrolment : dcpEnrolments) {
                switch (enrolment.getNumberOfTotalEnrolmentsInThisCourse(executionSemester)) {

                case 1:
                    firstEnrolledCount++;
                    break;
                case 2:
                    secondEnrolledCount++;
                    break;
                default:
                    break;
                }
            }

            // Add to result
            result.format("%s\t%s\t%d\t%s\t%d\t%s\t%d\t%d\t%d\t%d\t%d\n", curricularCourse.getCode(), curricularCourse.getName(),
                    executionCourse.getExternalId(), executionCourse.getSigla(), enrolmentDCP.getExternalId(),
                    enrolmentDCP.getName(), semester, year, firstEnrolledCount, secondEnrolledCount, dcpEnrolments.size());
        }
    }

    private static Map<DegreeCurricularPlan, Collection<Enrolment>> organizeEnrolmentsByDCP(CurricularCourse curricularCourse,
            ExecutionSemester executionSemester, RegistrationAgreement agreement, ExecutionCourse executionCourse) {
        Map<DegreeCurricularPlan, Collection<Enrolment>> enrolmentsMap =
                new HashMap<DegreeCurricularPlan, Collection<Enrolment>>();

        List<Enrolment> enrolments = curricularCourse.getEnrolmentsByExecutionPeriod(executionSemester);
        for (Enrolment enrolment : enrolments) {

            if (executionCourse != null && enrolment.getAttendsByExecutionCourse(executionCourse) == null) {
                continue;
            }

            final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();

            if (agreement != null && studentCurricularPlan.getRegistration().getRegistrationAgreement() != agreement) {
                continue;
            }

            DegreeCurricularPlan studentDCP = studentCurricularPlan.getDegreeCurricularPlan();
            Collection<Enrolment> dcpEnrolments = enrolmentsMap.get(studentDCP);
            if (dcpEnrolments == null) {
                dcpEnrolments = new ArrayList<Enrolment>();
                enrolmentsMap.put(studentDCP, dcpEnrolments);
            }
            dcpEnrolments.add(enrolment);

        }
        return enrolmentsMap;
    }

}