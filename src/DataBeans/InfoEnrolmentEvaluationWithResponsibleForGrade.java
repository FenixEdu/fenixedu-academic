package DataBeans;

import Dominio.IEnrolmentEvaluation;

/**
 * @author Fernanda Quitério Created on 13/Jul/2004
 *  
 */
public class InfoEnrolmentEvaluationWithResponsibleForGrade extends InfoEnrolmentEvaluation {

    public void copyFromDomain(IEnrolmentEvaluation enrolmentEvaluation) {
        super.copyFromDomain(enrolmentEvaluation);
        if (enrolmentEvaluation != null) {
            setInfoPersonResponsibleForGrade(InfoPerson.newInfoFromDomain(enrolmentEvaluation
                    .getPersonResponsibleForGrade()));
        }
    }

    public static InfoEnrolmentEvaluation newInfoFromDomain(IEnrolmentEvaluation enrolmentEvaluation) {
        InfoEnrolmentEvaluationWithResponsibleForGrade infoEnrolmentEvaluationWithInfoPerson = null;
        if (enrolmentEvaluation != null) {
            infoEnrolmentEvaluationWithInfoPerson = new InfoEnrolmentEvaluationWithResponsibleForGrade();
            infoEnrolmentEvaluationWithInfoPerson.copyFromDomain(enrolmentEvaluation);
        }
        return infoEnrolmentEvaluationWithInfoPerson;
    }
}