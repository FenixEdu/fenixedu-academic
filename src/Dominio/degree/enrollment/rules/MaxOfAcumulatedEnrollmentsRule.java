package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;

import commons.CollectionUtils;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MaxOfAcumulatedEnrollmentsRule implements IEnrollmentRule {

    Map acumulatedEnrollments;
    List enrolledEnrollments;
    int maxTotalNAC;

    /**
     * 
     * @param studentCurricularPlan
     */
    public MaxOfAcumulatedEnrollmentsRule(IStudentCurricularPlan studentCurricularPlan) {
        List enrollments = studentCurricularPlan.getAllEnrollmentsInCoursesWhereStudentIsEnrolledAtTheMoment();

        List curricularCourses = (List) CollectionUtils.collect(enrollments, new Transformer() {

            public Object transform(Object obj) {
                ICurricularCourse curricularCourse = ((IEnrolment) obj).getCurricularCourse();
                String key = curricularCourse.getCode() + curricularCourse.getName() + curricularCourse.getDegreeCurricularPlan().getName()
                        + curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();
                return (key);
            }
        });

        acumulatedEnrollments = CollectionUtils.getCardinalityMap(curricularCourses);
        enrolledEnrollments = studentCurricularPlan.getStudentEnrolledEnrollments();
        maxTotalNAC = studentCurricularPlan.getStudent().getStudentKind().getMaxNACToEnrol().intValue();
    }

    /**
     * @param List
     * @return List
     */
    public List apply(List curricularCoursesToBeEnrolledIn) {

        int totalNAC = 0;
        int availableNACToEnroll = 0;
        List filtercurricularCoursesToBeEnrolledIn = new ArrayList();

        Iterator iterator = enrolledEnrollments.iterator();
        while (iterator.hasNext()) {
            
            IEnrolment enrollment = (IEnrolment) iterator.next();
            ICurricularCourse curricularCourse = enrollment.getCurricularCourse();
            totalNAC += getIncrementNAC(curricularCourse).intValue();
        }

        availableNACToEnroll = maxTotalNAC - totalNAC;
        iterator = curricularCoursesToBeEnrolledIn.iterator();
        while (iterator.hasNext()) {
            
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();
            if(availableNACToEnroll < getIncrementNAC(curricularCourse).intValue())
                filtercurricularCoursesToBeEnrolledIn.add(curricularCourse);                
        }
        
        curricularCoursesToBeEnrolledIn.removeAll(filtercurricularCoursesToBeEnrolledIn);
        return curricularCoursesToBeEnrolledIn;
    }

    /**
     * 
     * @param curricularCourse
     * @return Integer
     */
    private Integer getIncrementNAC(ICurricularCourse curricularCourse) {

        if (getCurricularCourseAcumulatedEnrolments(curricularCourse).intValue() > 1)
            return curricularCourse.getMaximumValueForAcumulatedEnrollments();
        else
            return curricularCourse.getMinimumValueForAcumulatedEnrollments();
    }

    /**
     * 
     * @param curricularCourse
     * @return Integer
     */
    private Integer getCurricularCourseAcumulatedEnrolments(ICurricularCourse curricularCourse) {
        String key = curricularCourse.getCode() + curricularCourse.getName() + curricularCourse.getDegreeCurricularPlan().getName()
                + curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();

        Integer curricularCourseAcumulatedEnrolments = (Integer) acumulatedEnrollments.get(key);

        if (curricularCourseAcumulatedEnrolments == null)
            curricularCourseAcumulatedEnrolments = new Integer(0);

        return curricularCourseAcumulatedEnrolments;
    }

}