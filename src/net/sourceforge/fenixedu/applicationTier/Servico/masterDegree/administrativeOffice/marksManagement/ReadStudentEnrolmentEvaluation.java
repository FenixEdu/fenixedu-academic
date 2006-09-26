package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentEvaluationWithResponsibleForGrade;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEnrolmentEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;

public class ReadStudentEnrolmentEvaluation extends Service {

    public InfoSiteEnrolmentEvaluation run(Integer studentEvaluationCode) {

	final EnrolmentEvaluation enrolmentEvaluation = rootDomainObject
		.readEnrolmentEvaluationByOID(studentEvaluationCode);
	final InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(enrolmentEvaluation
		.getPersonResponsibleForGrade().getTeacher());

	final List<InfoEnrolmentEvaluation> infoEnrolmentEvaluations = new ArrayList<InfoEnrolmentEvaluation>();
	final InfoEnrolment infoEnrolment = InfoEnrolment.newInfoFromDomain(enrolmentEvaluation
		.getEnrolment());

	final InfoEnrolmentEvaluation infoEnrolmentEvaluation = InfoEnrolmentEvaluationWithResponsibleForGrade
		.newInfoFromDomain(enrolmentEvaluation);
	infoEnrolmentEvaluation.setInfoPersonResponsibleForGrade(infoTeacher.getInfoPerson());
	if (enrolmentEvaluation.hasEmployee()) {
	    infoEnrolmentEvaluation.setInfoEmployee(InfoPerson.newInfoFromDomain(enrolmentEvaluation
		    .getEmployee().getPerson()));
	}
	infoEnrolmentEvaluation.setInfoEnrolment(infoEnrolment);
	infoEnrolmentEvaluations.add(infoEnrolmentEvaluation);

	// enrolmenEvaluation.setEnrolment

	InfoSiteEnrolmentEvaluation infoSiteEnrolmentEvaluation = new InfoSiteEnrolmentEvaluation();
	infoSiteEnrolmentEvaluation.setEnrolmentEvaluations(infoEnrolmentEvaluations);
	infoSiteEnrolmentEvaluation.setInfoTeacher(infoTeacher);

	return infoSiteEnrolmentEvaluation;

    }

}