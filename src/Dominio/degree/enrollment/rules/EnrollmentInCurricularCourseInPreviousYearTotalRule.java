package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularYear;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class EnrollmentInCurricularCourseInPreviousYearTotalRule implements IEnrollmentRule {

    List studentEnrolledEnrollments;
    IBranch studentBranch;
    IExecutionPeriod executionPeriod;

    public EnrollmentInCurricularCourseInPreviousYearTotalRule(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod) {

        studentEnrolledEnrollments = studentCurricularPlan.getStudentEnrolledEnrollments();
        studentBranch = studentCurricularPlan.getBranch();
        this.executionPeriod = executionPeriod;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {

        ArrayList mandatoryCurricularCoursesToBeEnrolledIn = new ArrayList();

        studentEnrolledEnrollments = sortCurricularCoursesByYear(studentEnrolledEnrollments);

        ICurricularCourse lastCurricularCourse = (ICurricularCourse) studentEnrolledEnrollments.get(studentEnrolledEnrollments.size() - 1);
        ICurricularYear maxCurricularYear = lastCurricularCourse.getCurricularYearByBranch(studentBranch, executionPeriod.getSemester());
        int maxYear = maxCurricularYear.getYear().intValue();

        curricularCoursesToBeEnrolledIn = sortCurricularCoursesByYear(curricularCoursesToBeEnrolledIn);

        for (int iter = 0; iter < curricularCoursesToBeEnrolledIn.size(); iter++) {
            ICurricularCourse curricularCourse = (ICurricularCourse) curricularCoursesToBeEnrolledIn.get(iter);
            ICurricularYear curricularYear = curricularCourse.getCurricularYearByBranch(studentBranch, executionPeriod.getSemester());
            int year = maxCurricularYear.getYear().intValue();

            if (year >= maxYear)
                break;

            mandatoryCurricularCoursesToBeEnrolledIn.add(curricularCourse);
        }

        return mandatoryCurricularCoursesToBeEnrolledIn;
    }

    private List sortCurricularCoursesByYear(List curricularCourses) {
        Collections.sort(curricularCourses, new Comparator() {

            public int compare(Object obj1, Object obj2) {

                ICurricularCourse curricularCourse1 = (ICurricularCourse) obj1;
                ICurricularCourse curricularCourse2 = (ICurricularCourse) obj2;
                ICurricularYear curricularYear1 = curricularCourse1.getCurricularYearByBranch(studentBranch, executionPeriod.getSemester());
                ICurricularYear curricularYear2 = curricularCourse2.getCurricularYearByBranch(studentBranch, executionPeriod.getSemester());

                return curricularYear1.getYear().intValue() - curricularYear2.getYear().intValue();
            }
        });

        return curricularCourses;
    }
}