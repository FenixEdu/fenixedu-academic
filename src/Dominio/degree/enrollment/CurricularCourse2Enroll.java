/*
 * Created on Jun 28, 2004
 *
 */
package Dominio.degree.enrollment;

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
    private Integer accumulatedWeight;
    
    
    

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
    /**
     * @return Returns the accumulatedWeight.
     */
    public Integer getAccumulatedWeight() {
        return accumulatedWeight;
    }
    /**
     * @param accumulatedWeight The accumulatedWeight to set.
     */
    public void setAccumulatedWeight(Integer accumulatedWeight) {
        this.accumulatedWeight = accumulatedWeight;
    }

    public String toString() {
        return getCurricularCourse().getName() + " - " +
        	getCurricularCourse().getCode() + " - " +
        	getEnrollmentType().getName() + " - " +
        	getAccumulatedWeight().toString();
    }
}