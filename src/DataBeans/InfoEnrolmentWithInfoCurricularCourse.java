package DataBeans;

import Dominio.IEnrollment;

/**
 * @author Fernanda Quitério
 * Created on 12/Jul/2004
 */
public class InfoEnrolmentWithInfoCurricularCourse extends InfoEnrolment {

	public void copyFromDomain(IEnrollment enrolment) {
		super.copyFromDomain(enrolment);
		if(enrolment != null) {
			setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(enrolment.getCurricularCourse()));
		}
	}
	
	public static InfoEnrolment newInfoFromDomain(IEnrollment enrolment) {
		InfoEnrolmentWithInfoCurricularCourse infoEnrolment = null;
		if(enrolment != null) {
			infoEnrolment = new InfoEnrolmentWithInfoCurricularCourse();
			infoEnrolment.copyFromDomain(enrolment);
		}				
		return infoEnrolment;
	}
}
