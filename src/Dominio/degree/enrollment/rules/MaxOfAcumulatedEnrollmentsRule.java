package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;

import commons.CollectionUtils;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MaxOfAcumulatedEnrollmentsRule extends SelectionUponMaximumNumberEnrollmentRule implements IEnrollmentRule {

    Map acumulatedEnrollments;
    List enrolledEnrollments;
    int maxTotalNAC;
    int availableNACToEnroll;
    int currentSemester;

    /**
     * 
     * @param studentCurricularPlan
     */
    public MaxOfAcumulatedEnrollmentsRule(IStudentCurricularPlan studentCurricularPlan, IExecutionPeriod executionPeriod) {
        
        super(executionPeriod, studentCurricularPlan.getBranch());
        List enrollments = studentCurricularPlan.getAllEnrollmentsInCoursesWhereStudentIsEnrolledAtTheMoment();

        List curricularCourses = (List) CollectionUtils.collect(enrollments, new Transformer() {

            public Object transform(Object obj) {
                ICurricularCourse curricularCourse = ((IEnrolment) obj).getCurricularCourse();
                String key = curricularCourse.getCode() + curricularCourse.getName()
                        + curricularCourse.getDegreeCurricularPlan().getDegree().getNome()
                        + curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso();
                return (key);
            }
        });

        acumulatedEnrollments = CollectionUtils.getCardinalityMap(curricularCourses);
        enrolledEnrollments = studentCurricularPlan.getStudentEnrolledEnrollments();
        maxTotalNAC = studentCurricularPlan.getMaximumNumberOfAcumulatedEnrollments().intValue();
    }

    /**
     * @param List
     * @return List
     */
    public List apply(List curricularCoursesToBeEnrolledIn) {

        int totalNAC = 0;
        List filtercurricularCoursesToBeEnrolledIn = new ArrayList();

        for(int i = 0; i < enrolledEnrollments.size(); i++) {

            IEnrolment enrollment = (IEnrolment) enrolledEnrollments.get(i);
            ICurricularCourse curricularCourse = enrollment.getCurricularCourse();
            totalNAC += getIncrementNAC(curricularCourse).intValue();
        }

        availableNACToEnroll = maxTotalNAC - totalNAC;
        
        curricularCoursesToBeEnrolledIn = sortCurricularCourseByCurricularYear(curricularCoursesToBeEnrolledIn);
        curricularCoursesToBeEnrolledIn = filterCurricularCoursesThatExeceedAcumulatedValue(curricularCoursesToBeEnrolledIn);
        
        for(int i = 0; i < curricularCoursesToBeEnrolledIn.size(); i++){
            ICurricularCourse curricularCourse = (ICurricularCourse) curricularCoursesToBeEnrolledIn.get(i);
            if(availableNACToEnroll < getIncrementNAC(curricularCourse).intValue())
                filtercurricularCoursesToBeEnrolledIn.add(curricularCourse); 
        }
        
        curricularCoursesToBeEnrolledIn.removeAll(filtercurricularCoursesToBeEnrolledIn);

        return curricularCoursesToBeEnrolledIn;
    }

    /**
     * 
     * @param curricularCoursesSortedByYear
     * @return
     */
    private List filterCurricularCoursesThatExeceedAcumulatedValue(List curricularCoursesSortedByYear) {

        int acumulatedNAC = 0;
        int iterator = 0;
        for (; iterator < curricularCoursesSortedByYear.size(); iterator++) {
            IEnrolment enrollment = (IEnrolment) curricularCoursesSortedByYear.get(iterator);
            ICurricularCourse curricularCourse = enrollment.getCurricularCourse();
            acumulatedNAC += getIncrementNAC(curricularCourse).intValue();

            if (acumulatedNAC >= availableNACToEnroll) {

                curricularCoursesSortedByYear = getCurricularCoursesFromPreviousAndCurrentYear(curricularCoursesSortedByYear, iterator);
                break;
            }
        }
        return curricularCoursesSortedByYear;
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

        String key = curricularCourse.getCode() + curricularCourse.getName() + curricularCourse.getDegreeCurricularPlan().getDegree().getNome()
                + curricularCourse.getDegreeCurricularPlan().getDegree().getTipoCurso();

        Integer curricularCourseAcumulatedEnrolments = (Integer) acumulatedEnrollments.get(key);

        if (curricularCourseAcumulatedEnrolments == null)
            curricularCourseAcumulatedEnrolments = new Integer(0);

        return curricularCourseAcumulatedEnrolments;
    }

}