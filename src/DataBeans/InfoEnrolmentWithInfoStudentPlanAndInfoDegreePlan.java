/*
 * Created on 18/Jun/2004
 *
 */
package DataBeans;

import Dominio.IEnrolment;

/**
 * @author Tânia Pousão
 * 18/Jun/2004
 */
public class InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlan extends InfoEnrolment {

	public void copyFromDomain(IEnrolment enrolment) {
		super.copyFromDomain(enrolment);
		if(enrolment != null) {
			setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoDegreeCurricularPlan.newInfoFromDomain(enrolment.getStudentCurricularPlan()));
		}
	}
	
	public static InfoEnrolment newInfoFromDomain(IEnrolment enrolment) {
		InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlan infoEnrolment = null;
		if(enrolment != null) {
			infoEnrolment = new InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlan();
			infoEnrolment.copyFromDomain(enrolment);
		}				
		return infoEnrolment;
	}
}
