/*
 * Created on 23/Jun/2004
 *
 */
package DataBeans;

import Dominio.IEnrolment;

/**
 * @author Tânia Pousão
 * 23/Jun/2004
 */
public class InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear
		extends
			InfoEnrolment {
	public void copyFromDomain(IEnrolment enrolment) {
		super.copyFromDomain(enrolment);
		if(enrolment != null) {
			setInfoCurricularCourse(InfoCurricularCourseWithInfoDegree.newInfoFromDomain(enrolment.getCurricularCourse()));//with degree
			setInfoExecutionPeriod(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(enrolment.getExecutionPeriod()));//with year	
		}
	}
	
	public static InfoEnrolment newInfoFromDomain(IEnrolment enrolment) {
		InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear infoEnrolment = null;
		if(enrolment != null) {
			infoEnrolment = new InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear();
			infoEnrolment.copyFromDomain(enrolment);
		}
		
		return infoEnrolment;
	}
}
