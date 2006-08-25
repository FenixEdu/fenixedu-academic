package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentGrade {

    public InfoEnrolmentEvaluation run(Enrolment enrolment) {

	if (enrolment == null) {
	    return null;
	}
	final DegreeCurricularPlan degreeCurricularPlan = enrolment.getStudentCurricularPlan()
		.getDegreeCurricularPlan();
	return (degreeCurricularPlan.getDegree().getDegreeType() == DegreeType.DEGREE) ? run(enrolment
		.getAllFinalEnrolmentEvaluations()) : run(enrolment.getEvaluations());
    }

    private InfoEnrolmentEvaluation run(List<EnrolmentEvaluation> enrolmentEvaluations) {
	if (enrolmentEvaluations == null || enrolmentEvaluations.isEmpty()) {
	    return null;
	}
	final EnrolmentEvaluation evaluation = (EnrolmentEvaluation) Collections
		.max(enrolmentEvaluations);
	return getInfoLatestEvaluation(evaluation);
    }

    private InfoEnrolmentEvaluation getInfoLatestEvaluation(EnrolmentEvaluation latestEvaluation) {
	
	final InfoEnrolmentEvaluation infolatestEvaluation = InfoEnrolmentEvaluation.newInfoFromDomain(latestEvaluation);
	if (latestEvaluation.hasEmployee()) {
	    if (latestEvaluation.getEmployee().hasPerson()) {
		infolatestEvaluation.setInfoEmployee(InfoPerson.newInfoFromDomain(latestEvaluation
			.getEmployee().getPerson()));
	    }
	    if (latestEvaluation.hasPersonResponsibleForGrade()) {
		infolatestEvaluation.setInfoPersonResponsibleForGrade(InfoPerson
			.newInfoFromDomain(latestEvaluation.getPersonResponsibleForGrade()));
	    }
	}
	return infolatestEvaluation;
    }
}