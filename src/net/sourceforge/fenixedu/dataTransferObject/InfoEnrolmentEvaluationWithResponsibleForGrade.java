package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;

/**
 * @author Fernanda Quitério Created on 13/Jul/2004
 * 
 */
public class InfoEnrolmentEvaluationWithResponsibleForGrade extends InfoEnrolmentEvaluation {

	@Override
	public void copyFromDomain(EnrolmentEvaluation enrolmentEvaluation) {
		super.copyFromDomain(enrolmentEvaluation);
		if (enrolmentEvaluation != null) {
			setInfoPersonResponsibleForGrade(InfoPerson.newInfoFromDomain(enrolmentEvaluation.getPersonResponsibleForGrade()));
			if (enrolmentEvaluation.hasPerson()) {
				setInfoEmployee(InfoPerson.newInfoFromDomain(enrolmentEvaluation.getPerson()));
			}
		}
	}

	public static InfoEnrolmentEvaluation newInfoFromDomain(EnrolmentEvaluation enrolmentEvaluation) {
		InfoEnrolmentEvaluationWithResponsibleForGrade infoEnrolmentEvaluationWithInfoPerson = null;
		if (enrolmentEvaluation != null) {
			infoEnrolmentEvaluationWithInfoPerson = new InfoEnrolmentEvaluationWithResponsibleForGrade();
			infoEnrolmentEvaluationWithInfoPerson.copyFromDomain(enrolmentEvaluation);
		}
		return infoEnrolmentEvaluationWithInfoPerson;
	}
}