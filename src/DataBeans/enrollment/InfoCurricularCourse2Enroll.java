/*
 * Created on Jun 28, 2004
 *
 */
package DataBeans.enrollment;

import java.io.Serializable;

import DataBeans.DataTranferObject;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularYear;
import Dominio.degree.enrollment.CurricularCourse2Enroll;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author João Mota
 *  
 */
public class InfoCurricularCourse2Enroll extends DataTranferObject implements Serializable {

    private InfoCurricularCourse infoCurricularCourse;

    private CurricularCourseEnrollmentType enrollmentType;

    private Integer accumulatedWeight;

    private InfoCurricularYear curricularYear;

    private Boolean optionalCurricularCourse;

    /**
     * @return Returns the curricularYear.
     */
    public InfoCurricularYear getCurricularYear() {
        return curricularYear;
    }

    /**
     * @param curricularYear
     *            The curricularYear to set.
     */
    public void setCurricularYear(InfoCurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    /**
     * @return Returns the enrollmentRuleType.
     */
    public CurricularCourseEnrollmentType getEnrollmentType() {
        return enrollmentType;
    }

    public Boolean getOptionalCurricularCourse() {
        return optionalCurricularCourse;
    }

    public void setOptionalCurricularCourse(Boolean optionalCurricularCourse) {
        this.optionalCurricularCourse = optionalCurricularCourse;
    }

    /**
     * @param enrollmentRuleType
     *            The enrollmentRuleType to set.
     */
    public void setEnrollmentType(CurricularCourseEnrollmentType enrollmentRuleType) {
        this.enrollmentType = enrollmentRuleType;
    }

    /**
     * @return Returns the infoCurricularCourse.
     */
    public InfoCurricularCourse getInfoCurricularCourse() {
        return infoCurricularCourse;
    }

    /**
     * @param infoCurricularCourse
     *            The infoCurricularCourse to set.
     */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

    public void copyFromDomain(CurricularCourse2Enroll curricularCourse2Enroll) {
        if (curricularCourse2Enroll != null) {
            setEnrollmentType(curricularCourse2Enroll.getEnrollmentType());
            setAccumulatedWeight(curricularCourse2Enroll.getAccumulatedWeight());
            setOptionalCurricularCourse(curricularCourse2Enroll.getOptionalCurricularCourse());
        }
    }

    public static InfoCurricularCourse2Enroll newInfoFromDomain(
            CurricularCourse2Enroll curricularCourse2Enroll) {
        InfoCurricularCourse2Enroll infoCurricularCourse2Enroll = null;
        if (curricularCourse2Enroll != null) {
            infoCurricularCourse2Enroll = new InfoCurricularCourse2Enroll();
            infoCurricularCourse2Enroll.copyFromDomain(curricularCourse2Enroll);
        }
        return infoCurricularCourse2Enroll;
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

    public boolean isOptionalCurricularCourse() {
        return optionalCurricularCourse.booleanValue();
    }
}