/*
 * Created on 23/Jun/2004
 *
 */
package DataBeans;

import Dominio.IEnrollment;

/**
 * @author Tânia Pousão 23/Jun/2004
 */
public class InfoEnrolmentWithCourseAndDegree extends InfoEnrolment {
    public void copyFromDomain(IEnrollment enrolment) {
        super.copyFromDomain(enrolment);
        if (enrolment != null) {
            setInfoCurricularCourse(InfoCurricularCourseWithInfoDegree.newInfoFromDomain(enrolment
                    .getCurricularCourse()));//with degree

        }
    }

    public static InfoEnrolment newInfoFromDomain(IEnrollment enrolment) {
        InfoEnrolmentWithCourseAndDegree infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithCourseAndDegree();
            infoEnrolment.copyFromDomain(enrolment);
        }

        return infoEnrolment;
    }
}