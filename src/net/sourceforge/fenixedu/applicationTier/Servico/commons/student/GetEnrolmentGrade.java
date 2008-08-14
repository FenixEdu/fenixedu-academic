package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEnrolmentGrade {

    final public InfoEnrolmentEvaluation run(final Enrolment enrolment) {
	if (enrolment == null) {
	    return null;
	}

	return getInfoLatestEvaluation(enrolment.getLatestEnrolmentEvaluation());
    }

    final private InfoEnrolmentEvaluation getInfoLatestEvaluation(final EnrolmentEvaluation latestEvaluation) {

	final InfoEnrolmentEvaluation infolatestEvaluation = InfoEnrolmentEvaluation.newInfoFromDomain(latestEvaluation);
	if (latestEvaluation.hasEmployee()) {
	    if (latestEvaluation.getEmployee().hasPerson()) {
		infolatestEvaluation.setInfoEmployee(InfoPerson.newInfoFromDomain(latestEvaluation.getEmployee().getPerson()));
	    }

	    if (latestEvaluation.hasPersonResponsibleForGrade()) {
		infolatestEvaluation.setInfoPersonResponsibleForGrade(InfoPerson.newInfoFromDomain(latestEvaluation
			.getPersonResponsibleForGrade()));
	    }
	}

	return infolatestEvaluation;
    }

}
