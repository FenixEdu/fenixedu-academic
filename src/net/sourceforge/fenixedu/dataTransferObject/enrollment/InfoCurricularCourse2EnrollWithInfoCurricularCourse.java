/*
 * Created on Jun 28, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.enrollment;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

/**
 * @author João Mota
 *  
 */
public class InfoCurricularCourse2EnrollWithInfoCurricularCourse extends InfoCurricularCourse2Enroll
        implements Serializable {

    public void copyFromDomain(CurricularCourse2Enroll curricularCourse2Enroll) {
        super.copyFromDomain(curricularCourse2Enroll);
        if (curricularCourse2Enroll != null) {
            setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(curricularCourse2Enroll
                    .getCurricularCourse()));
        }
    }

    public static InfoCurricularCourse2Enroll newInfoFromDomain(
            CurricularCourse2Enroll curricularCourse2Enroll) {
        InfoCurricularCourse2EnrollWithInfoCurricularCourse infoCurricularCourse2Enroll = null;
        if (curricularCourse2Enroll != null) {
            infoCurricularCourse2Enroll = new InfoCurricularCourse2EnrollWithInfoCurricularCourse();
            infoCurricularCourse2Enroll.copyFromDomain(curricularCourse2Enroll);
        }
        return infoCurricularCourse2Enroll;
    }

}