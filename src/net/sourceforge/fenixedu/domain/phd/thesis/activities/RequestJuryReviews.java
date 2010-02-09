/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;
import net.sourceforge.fenixedu.util.phd.PhdProperties;

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

	notifyJuryElements(process);
	sendEmailToJuryElement(process.getIndividualProgramProcess(), process.getPresidentJuryElement().getParticipant(),
		"message.phd.request.jury.reviews.external.access.jury.president.body");

	if (process.getActiveState() != PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK) {
	    process.createState(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK, userView.getPerson(), null);
	}

	return process;
    }

    private void notifyJuryElements(PhdThesisProcess process) {

	for (final ThesisJuryElement juryElement : process.getThesisJuryElements()) {

	    if (!isCoordinatorOrGuider(process, juryElement)) {
		createExternalAccess(juryElement);
	    }

	    final PhdParticipant participant = juryElement.getParticipant();

	    if (juryElement.getReporter().booleanValue()) {
		sendEmailToReporter(process.getIndividualProgramProcess(), participant);

		// TODO:
		// TODO: create alert to submit review
		// TODO:

	    } else {
		sendEmailToJuryElement(process.getIndividualProgramProcess(), participant,
			"message.phd.request.jury.reviews.external.access.jury.body");
	    }
	}
    }

    private String getAccessInformation(PhdIndividualProgramProcess process, PhdParticipant participant) {

	if (!participant.isInternal()) {
	    return AlertMessage.get("message.phd.request.jury.reviews.external.access", PhdProperties.getPhdExternalAccessLink(),
		    participant.getAccessHashCode(), participant.getPassword());

	} else {
	    final Person person = ((InternalPhdParticipant) participant).getPerson();

	    if (process.isCoordinatorForPhdProgram(person)) {
		return AlertMessage.get("message.phd.request.jury.reviews.coordinator.access");

	    } else if (process.isGuiderOrAssistentGuider(person) || person.hasRole(RoleType.TEACHER)) {
		return AlertMessage.get("message.phd.request.jury.reviews.teacher.access");
	    }
	}

	throw new DomainException("error.PhdThesisProcess.unexpected.participant.type");
    }

    private void sendEmailToReporter(PhdIndividualProgramProcess process, PhdParticipant participant) {

	final String subject = AlertMessage.get("message.phd.request.jury.reviews.external.access.subject", process
		.getPhdProgram().getName());

	final String body = AlertMessage.get("message.phd.request.jury.reviews.external.access.jury.body", process.getPerson()
		.getName(), process.getProcessNumber())
		+ "\n\n"
		+ AlertMessage.get("message.phd.request.jury.reviews.reporter.body")
		+ "\n\n"
		+ getAccessInformation(process, participant);

	email(participant.getEmail(), subject, body);
    }

    private void sendEmailToJuryElement(PhdIndividualProgramProcess process, PhdParticipant participant, String bodyMessage) {
	final String subject = AlertMessage.get("message.phd.request.jury.reviews.external.access.subject", process
		.getPhdProgram().getName());

	final String body = AlertMessage.get(bodyMessage, process.getPerson().getName(), process.getProcessNumber()) + "\n\n"
		+ getAccessInformation(process, participant) + "\n\n"
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

    private void email(String email, String subject, String body) {
	final SystemSender sender = RootDomainObject.getInstance().getSystemSender();
	new Message(sender, sender.getConcreteReplyTos(), null, null, null, subject, body, Collections.singleton(email));
    }

    private boolean isCoordinatorOrGuider(PhdThesisProcess process, ThesisJuryElement juryElement) {

	if (!juryElement.isInternal()) {
	    return false;
	}

	final Person person = ((InternalPhdParticipant) juryElement.getParticipant()).getPerson();
	return process.getIndividualProgramProcess().isCoordinatorForPhdProgram(person)
		|| (process.getIndividualProgramProcess().isGuiderOrAssistentGuider(person) && person.hasRole(RoleType.TEACHER));
    }

}