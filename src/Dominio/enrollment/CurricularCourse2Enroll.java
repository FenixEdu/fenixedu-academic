/*
 * Created on Jun 28, 2004
 *
 */
package Dominio.enrollment;

import java.io.Serializable;

import Dominio.ICurricularCourse;
import Util.CurricularCourseEnrollmentType;

/**
 * @author João Mota
 *
 */
public class CurricularCourse2Enroll implements Serializable {
    
    private ICurricularCourse curricularCourse;
    private CurricularCourseEnrollmentType enrollmentRuleType;
    
    

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
        this.enrollmentRuleType = enrollmentRuleType;
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
    public CurricularCourseEnrollmentType getEnrollmentRuleType() {
        return enrollmentRuleType;
    }
    /**
     * @param enrollmentRuleType The enrollmentRuleType to set.
     */
    public void setEnrollmentRuleType(
            CurricularCourseEnrollmentType enrollmentRuleType) {
        this.enrollmentRuleType = enrollmentRuleType;
    }
}
