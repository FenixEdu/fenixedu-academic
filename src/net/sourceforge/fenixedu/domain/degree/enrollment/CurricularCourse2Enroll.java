/*
 * Created on Jun 28, 2004
 *
 */
package net.sourceforge.fenixedu.domain.degree.enrollment;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;

/**
 * @author João Mota
 */

public class CurricularCourse2Enroll implements Serializable {
    private ICurricularCourse curricularCourse;

    private CurricularCourseEnrollmentType enrollmentType;

    private Integer accumulatedWeight;

    private Boolean optionalCurricularCourse;

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
                + getEnrollmentType().toString() + " - " + getAccumulatedWeight().toString();
    }

    public boolean isOptionalCurricularCourse() {
        return optionalCurricularCourse.booleanValue();
    }

    public boolean equals(Object obj) {
        if (obj instanceof CurricularCourse2Enroll) {
            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
            return this.getCurricularCourse().equals(curricularCourse2Enroll.getCurricularCourse());
        }
        return false;
    }

}
