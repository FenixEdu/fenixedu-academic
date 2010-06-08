/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

public class RequestJuryReviews extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	if (!process.isJuryValidated()) {
	    throw new PreConditionNotValidException();
	}

	if (!PhdThesisProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    throw new PreConditionNotValidException();
	}

    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {

	final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

	if (bean.isToNotify()) {
	    notifyJuryElements(process);
	    sendEmailToJuryElement(process.getIndividualProgramProcess(), process.getPresidentJuryElement().getParticipant(),
		    "message.phd.request.jury.reviews.external.access.jury.president.body");
	}

	if (process.getActiveState() != PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK) {
	    process.createState(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK, userView.getPerson(), bean
		    .getRemarks());
	}

	return process;
    }

    private void notifyJuryElements(PhdThesisProcess process) {

	for (final ThesisJuryElement juryElement : process.getThesisJuryElements()) {

	    if (juryElement.isDocumentValidated()) {
		continue;
	    }

	    if (!juryElement.isInternal()) {
		createExternalAccess(juryElement);
	    }

	    final PhdParticipant participant = juryElement.getParticipant();

	    if (juryElement.getReporter().booleanValue()) {
		sendEmailToReporter(process.getIndividualProgramProcess(), participant);

		// TODO:
		// TODO: create alert to submit review?
		// TODO:

	    } else {
		sendEmailToJuryElement(process.getIndividualProgramProcess(), participant,
			"message.phd.request.jury.reviews.external.access.jury.body");
	    }
	}
    }

    private void sendEmailToReporter(PhdIndividualProgramProcess process, PhdParticipant participant) {

	final String subject = AlertMessage.get("message.phd.request.jury.reviews.external.access.subject", process
		.getPhdProgram().getName());

	final String body = AlertMessage.get("message.phd.request.jury.reviews.external.access.jury.body", process.getPerson()
		.getName(), process.getProcessNumber())
		+ "\n\n"
		+ AlertMessage.get("message.phd.request.jury.reviews.reporter.body")
		+ "\n\n"
		+ getAccessInformation(process, participant, "message.phd.request.jury.reviews.coordinator.access",
			"message.phd.request.jury.reviews.teacher.access");

	email(participant.getEmail(), subject, body);
    }

    private void sendEmailToJuryElement(PhdIndividualProgramProcess process, PhdParticipant participant, String bodyMessage) {
	final String subject = AlertMessage.get("message.phd.request.jury.reviews.external.access.subject", process
		.getPhdProgram().getName());

	final String body = AlertMessage.get(bodyMessage, process.getPerson().getName(), process.getProcessNumber())
		+ "\n\n"
		+ getAccessInformation(process, participant, "message.phd.request.jury.reviews.coordinator.access",
			"message.phd.request.jury.reviews.teacher.access") + "\n\n"
		+ AlertMessage.get("message.phd.request.jury.external.access.reviews.body");

	email(participant.getEmail(), subject, body);
    }

    private void createExternalAccess(final ThesisJuryElement juryElement) {

	final PhdParticipant participant = juryElement.getParticipant();
	participant.addAccessType(PhdProcessAccessType.JURY_DOCUMENTS_DOWNLOAD);

	if (juryElement.getReporter().booleanValue()) {
	    participant.addAccessType(PhdProcessAccessType.JURY_REPORTER_FEEDBACK_UPLOAD);
	}
    }

}