package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MaximumCurricularCourseEnrollmentRule extends SelectionUponMaximumNumberEnrollmentRule implements IEnrollmentRule {

    List enrollments;
    int maxCoursesToBeEnrolled;

    public MaximumCurricularCourseEnrollmentRule(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod) {

        super(executionPeriod, studentCurricularPlan.getBranch());
        maxCoursesToBeEnrolled = studentCurricularPlan.getMaximumNumberOfCoursesToEnroll().intValue();
        enrollments = studentCurricularPlan.getAllEnrollmentsInCoursesWhereStudentIsEnrolledAtTheMoment();

    }

    public List apply(List curricularCoursesToBeEnrolledIn) {

        double maxEnrolledCurricularCourses = 0;
        double numberOfEnrolledCurricularCourses = 0;
        double availableCurricularCoursesToEnroll = 0;
        ArrayList filterCurricularCoursesToBeEnrolledIn = new ArrayList();
        
       
        for(int i = 0; i < enrollments.size(); i++){
            IEnrolment enrollment = (IEnrolment) enrollments.get(i);
            numberOfEnrolledCurricularCourses += enrollment.getCurricularCourse().getEnrollmentWeigth().doubleValue();
        }

		availableCurricularCoursesToEnroll = maxEnrolledCurricularCourses - numberOfEnrolledCurricularCourses;
         
		curricularCoursesToBeEnrolledIn = sortCurricularCourseByCurricularYear(curricularCoursesToBeEnrolledIn);
        curricularCoursesToBeEnrolledIn = filterCurricularCoursesThatExeceedAcumulatedValue(curricularCoursesToBeEnrolledIn);
		
        for(int i = 0; i < enrollments.size(); i++){
            ICurricularCourse curricularCourse = (ICurricularCourse) curricularCoursesToBeEnrolledIn.get(i);
            if(curricularCourse.getWeigth().doubleValue() > availableCurricularCoursesToEnroll)
            	filterCurricularCoursesToBeEnrolledIn.add(curricularCourse);
        }
        
        curricularCoursesToBeEnrolledIn.removeAll(filterCurricularCoursesToBeEnrolledIn);

        return curricularCoursesToBeEnrolledIn;
    }
    
    /**
     * 
     * @param curricularCoursesSortedByYear
     * @return
     */
    private List filterCurricularCoursesThatExeceedAcumulatedValue(List curricularCoursesSortedByYear) {

        int acumulatedEnrollments = 0;
        int iterator = 0;
        for (; iterator < curricularCoursesSortedByYear.size(); iterator++) {
            IEnrolment enrollment = (IEnrolment) curricularCoursesSortedByYear.get(iterator);
            ICurricularCourse curricularCourse = enrollment.getCurricularCourse();
            acumulatedEnrollments += curricularCourse.getEnrollmentWeigth().intValue();

            if (acumulatedEnrollments >= maxCoursesToBeEnrolled) {

                curricularCoursesSortedByYear = getCurricularCoursesFromPreviousAndCurrentYear(curricularCoursesSortedByYear, iterator);
                break;
            }
        }
        return curricularCoursesSortedByYear;
    }
}