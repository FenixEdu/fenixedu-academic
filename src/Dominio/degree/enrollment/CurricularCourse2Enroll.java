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
 */

public class CurricularCourse2Enroll implements Serializable {

    private ICurricularCourse curricularCourse;

    private CurricularCourseEnrollmentType enrollmentType;

    private Integer accumulatedWeight;

    private Boolean optionalCurricularCourse;

    /**
     *  
     */
    public CurricularCourse2Enroll() {
    }

    public CurricularCourse2Enroll(ICurricularCourse curricularCourse,
            CurricularCourseEnrollmentType enrollmentRuleType, Boolean optionalCurricularCourse) {
        this.curricularCourse = curricularCourse;
        this.enrollmentType = enrollmentRuleType;
        this.optionalCurricularCourse = optionalCurricularCourse;
    }

    /**
     * @return Returns the curricularCourse.
     */
    public ICurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    public Boolean getOptionalCurricularCourse() {
        return optionalCurricularCourse;
    }

    public void setOptionalCurricularCourse(Boolean optionalCurricularCourse) {
        this.optionalCurricularCourse = optionalCurricularCourse;
    }

    /**
     * @param curricularCourse
     *            The curricularCourse to set.
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
     * @param enrollmentRuleType
     *            The enrollmentRuleType to set.
     */
    public void setEnrollmentType(CurricularCourseEnrollmentType enrollmentRuleType) {
        this.enrollmentType = enrollmentRuleType;
    }

    /**
     * @return Returns the accumulatedWeight.
     */
    public Integer getAccumulatedWeight() {
        return accumulatedWeight;
    }

    /**
     * @param accumulatedWeight
     *            The accumulatedWeight to set.
     */
    public void setAccumulatedWeight(Integer accumulatedWeight) {
        this.accumulatedWeight = accumulatedWeight;
    }

    public String toString() {
        return getCurricularCourse().getName() + " - " + getCurricularCourse().getCode() + " - "
                + getEnrollmentType().getName() + " - " + getAccumulatedWeight().toString();
    }

    public boolean isOptionalCurricularCourse() {
        return optionalCurricularCourse.booleanValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(CurricularCourse2Enroll obj) {

        return this.getCurricularCourse().getIdInternal().equals(
                obj.getCurricularCourse().getIdInternal());
    }
}