/*
 * Created on 23/Jun/2004
 *
 */
package DataBeans;

import Dominio.IEnrolmentInOptionalCurricularCourse;

/**
 * @author Tânia Pousão
 * 23/Jun/2004
 */
public class InfoEnrolmentInOptionalCurricularCourseWithAll
		extends
			InfoEnrolmentInOptionalCurricularCourse {

	public void copyFromDomain(IEnrolmentInOptionalCurricularCourse enrolment) {
		super.copyFromDomain(enrolment);
		if(enrolment != null) {
			setInfoCurricularCourseForOption(InfoCurricularCourse.newInfoFromDomain(enrolment.getCurricularCourseForOption()));
		}
	}
	
	public static InfoEnrolmentInOptionalCurricularCourse newInfoFromDomain(IEnrolmentInOptionalCurricularCourse enrolment) {
		InfoEnrolmentInOptionalCurricularCourseWithAll infoEnrolment = null;
		if(enrolment != null) {
			infoEnrolment = new InfoEnrolmentInOptionalCurricularCourseWithAll();
			infoEnrolment.copyFromDomain(enrolment);
		}
		
		return infoEnrolment;
	}
}
