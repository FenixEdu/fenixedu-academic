package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MaximumCurricularCourseEnrollmentRule implements IEnrollmentRule {

    List temporaryEnrollments;
    int maxCoursesToBeEnrolled;

    public MaximumCurricularCourseEnrollmentRule(IStudentCurricularPlan studentCurricularPlan) {

        maxCoursesToBeEnrolled = studentCurricularPlan.getStudent().getStudentKind().getMaxCoursesToEnrol().intValue();
        temporaryEnrollments = studentCurricularPlan.getStudentTemporarilyEnrolledEnrollments();

    }

    public List apply(List curricularCoursesToBeEnrolledIn) {

        double maxWeightOfEnrolledCurricularCourses = 0;
        double numberOfEnrolledCurricularCourses = 0;
        double weightOfCurricularCoursesToEnroll = 0;
        ArrayList filterCurricularCoursesToBeEnrolledIn = new ArrayList();
        
        Iterator iterator = temporaryEnrollments.iterator();

        while (iterator.hasNext()) {
            IEnrolment enrollment = (IEnrolment) iterator.next();
            numberOfEnrolledCurricularCourses += enrollment.getCurricularCourse().getEnrollmentWeigth().doubleValue();
        }

		weightOfCurricularCoursesToEnroll = maxWeightOfEnrolledCurricularCourses - numberOfEnrolledCurricularCourses;
        iterator = curricularCoursesToBeEnrolledIn.iterator();
        while (iterator.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
            if(curricularCourse.getWeigth().doubleValue() > weightOfCurricularCoursesToEnroll)
            	filterCurricularCoursesToBeEnrolledIn.add(curricularCourse);
        }
        
        curricularCoursesToBeEnrolledIn.removeAll(filterCurricularCoursesToBeEnrolledIn);

        return curricularCoursesToBeEnrolledIn;
    }
}