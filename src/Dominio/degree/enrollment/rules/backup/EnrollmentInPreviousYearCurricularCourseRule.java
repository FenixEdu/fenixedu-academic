package Dominio.degree.enrollment.rules.backup;

import java.util.Iterator;
import java.util.List;

import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEnrolment;
import Dominio.IStudentCurricularPlan;
import Dominio.degree.enrollment.rules.IEnrollmentRule;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class EnrollmentInPreviousYearCurricularCourseRule implements IEnrollmentRule {

    List temporaryEnrollments;

    IBranch studentBranch;

    public EnrollmentInPreviousYearCurricularCourseRule(IStudentCurricularPlan studentCurricularPlan) {

        temporaryEnrollments = studentCurricularPlan.getStudentTemporarilyEnrolledEnrollments();
        studentBranch = studentCurricularPlan.getBranch();
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {

        Integer minYearEnrolledIn = null;
        Integer actualYearEnrolledIn = null;
        int maxYearTemporarilyEnrolledEnrollment = 0;
        int actualYearTemporarilyEnrolledEnrollment = 0;

        Iterator iterator = temporaryEnrollments.iterator();
        while(iterator.hasNext()) {
            IEnrolment enrollment = (IEnrolment) iterator.next();
            ICurricularCourse curricularCourse = enrollment.getCurricularCourse();
            
            actualYearTemporarilyEnrolledEnrollment = getCurricularYearOfCurricularCourse(curricularCourse).intValue();
            
            if (maxYearTemporarilyEnrolledEnrollment < actualYearTemporarilyEnrolledEnrollment)
                maxYearTemporarilyEnrolledEnrollment = actualYearTemporarilyEnrolledEnrollment;
        }
        
        iterator = curricularCoursesToBeEnrolledIn.iterator();
        while(iterator.hasNext()){
            
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

            actualYearEnrolledIn = getCurricularYearOfCurricularCourse(curricularCourse);
            
            if(minYearEnrolledIn == null){
                minYearEnrolledIn = actualYearEnrolledIn;   
            }
            else {
                if(minYearEnrolledIn.intValue() > actualYearEnrolledIn.intValue())
                    minYearEnrolledIn = actualYearEnrolledIn;
            }
        }
        
        if(minYearEnrolledIn.intValue() < maxYearTemporarilyEnrolledEnrollment)
            return null;
        else
            return curricularCoursesToBeEnrolledIn;
    }

    /**
     * 
     * @param curricularCourse
     * @return
     */
    private Integer getCurricularYearOfCurricularCourse(ICurricularCourse curricularCourse) {

        int maxCurricularYear = 0;
        int actualCurricularYear = 0;

        if (studentBranch != null) {
//            maxCurricularYear = curricularCourse.getCurricularYearByBranch(studentBranch).getYear().intValue();
        } else {
            List curricularCourseScopes = curricularCourse.getScopes();

            Iterator iterator = curricularCourseScopes.iterator();
            while (iterator.hasNext()) {

                ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) iterator.next();
                actualCurricularYear = curricularCourseScope.getCurricularSemester().getCurricularYear().getYear().intValue();

                if (maxCurricularYear < actualCurricularYear)
                    maxCurricularYear = actualCurricularYear;
            }

        }
        return new Integer(maxCurricularYear);
    }
}