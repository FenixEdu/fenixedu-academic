/*
 * Created on Jun 28, 2004
 *
 */
package Dominio.enrollment;

import java.io.Serializable;

import Dominio.ICurricularCourse;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author João Mota
 *
 */
public class CurricularCourse2Enroll implements Serializable {
    
    private ICurricularCourse curricularCourse;
    private CurricularCourseEnrollmentType enrollmentType;
    
    

    /**
     * 
     */
    public CurricularCourse2Enroll() {
     
    }
    /**
     * @param curricularCourse
     * @param enrollmentRuleType
     */
    public CurricularCourse2Enroll(ICurricularCourse curricularCourse,
            CurricularCourseEnrollmentType enrollmentRuleType) {
        this.curricularCourse = curricularCourse;
        this.enrollmentType = enrollmentRuleType;
    }
    /**
     * @return Returns the curricularCourse.
     */
    public ICurricularCourse getCurricularCourse() {
        return curricularCourse;
    }
    /**
     * @param curricularCourse The curricularCourse to set.
     */
    public void setCurricularCourse(ICurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }
    /**
     * @return Returns the enrollmentRuleType.
     */
    public CurricularCourseEnrollmentType getEnrollmentType() {
        return enrollmentType;
    }
    /**
     * @param enrollmentRuleType The enrollmentRuleType to set.
     */
    public void setEnrollmentType(
            CurricularCourseEnrollmentType enrollmentRuleType) {
        this.enrollmentType = enrollmentRuleType;
    }
}
