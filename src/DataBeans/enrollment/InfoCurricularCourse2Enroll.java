/*
 * Created on Jun 28, 2004
 *
 */
package DataBeans.enrollment;

import DataBeans.DataTranferObject;
import DataBeans.InfoCurricularCourse;
import Dominio.enrollment.CurricularCourse2Enroll;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author João Mota
 *  
 */
public class InfoCurricularCourse2Enroll extends DataTranferObject {

    private InfoCurricularCourse infoCurricularCourse;

    private CurricularCourseEnrollmentType enrollmentType;

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
    public void setEnrollmentType(
            CurricularCourseEnrollmentType enrollmentRuleType) {
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
    public void setInfoCurricularCourse(
            InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

    public void copyFromDomain(CurricularCourse2Enroll curricularCourse2Enroll) {
        if (curricularCourse2Enroll != null) {
            setEnrollmentType(curricularCourse2Enroll
                    .getEnrollmentType());
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

}