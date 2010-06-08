package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

public class ScheduleThesisMeeting extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	if (!process.getActiveState().equals(PhdThesisProcessStateType.WAITING_FOR_THESIS_MEETING_SCHEDULING)) {
	    throw new PreConditionNotValidException();
	}

	if (PhdThesisProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    return;
	}

	if (!process.isPresidentJuryElement(userView.getPerson())) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {

	final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

	if (bean.isToNotify()) {
	    notifyJuryElements(process, bean);
	    alertAcademicOfficeAndCoordinatorAndGuiders(process, bean);
	}

	process.setMeetingDate(bean.getMeetingDate());
	process.setMeetingPlace(bean.getMeetingPlace());

	process.createState(PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING, userView.getPerson(), bean
		.getRemarks());

	return process;
    }

    private void alertAcademicOfficeAndCoordinatorAndGuiders(PhdThesisProcess process, final PhdThesisProcessBean bean) {

	final AlertMessage subject = AlertMessage.create(bean.getMailSubject()).isKey(false).withPrefix(false);
	final AlertMessage body = AlertMessage.create(buildBody(process.getIndividualProgramProcess(), null, bean)).isKey(false)
		.withPrefix(false);

	AlertService.alertAcademicOffice(process.getIndividualProgramProcess(), getThesisProcessPermission(), subject, body);
	AlertService.alertResponsibleCoordinators(process.getIndividualProgramProcess(), subject, body);
	AlertService.alertGuiders(process.getIndividualProgramProcess(), subject, body);

    }

    private void notifyJuryElements(final PhdThesisProcess process, final PhdThesisProcessBean bean) {

	for (final ThesisJuryElement juryElement : process.getThesisJuryElements()) {

	    if (!juryElement.isInternal()) {
		createExternalAccess(juryElement);
	    }

	    final PhdParticipant participant = juryElement.getParticipant();
	    sendEmailToJuryElement(process.getIndividualProgramProcess(), participant, bean);
	}
    }

    private void createExternalAccess(ThesisJuryElement juryElement) {
	final PhdParticipant participant = juryElement.getParticipant();
	participant.addAccessType(PhdProcessAccessType.JURY_REVIEW_DOCUMENTS_DOWNLOAD);
    }

    private void sendEmailToJuryElement(PhdIndividualProgramProcess process, PhdParticipant participant, PhdThesisProcessBean bean) {
	email(participant.getEmail(), bean.getMailSubject(), buildBody(process, participant, bean));
    }

    private String buildBody(PhdIndividualProgramProcess process, PhdParticipant participant, PhdThesisProcessBean bean) {
	return bean.getMailBody() + "\n\n ---- " + getScheduledMeetingInformation(bean)
		+ appendAccessInformation(process, participant, bean);
    }

    private String appendAccessInformation(PhdIndividualProgramProcess process, PhdParticipant participant,
	    PhdThesisProcessBean bean) {
	if (participant == null) {
	    return "";
	}

	return "\n\n ----"
		+ getAccessInformation(process, participant, "message.phd.thesis.schedule.thesis.meeting.coordinator.access",
			"message.phd.thesis.schedule.thesis.meeting.teacher.access");

    }

    private String getScheduledMeetingInformation(final PhdThesisProcessBean bean) {
	final StringBuilder sb = new StringBuilder();

	sb.append("\n");
	sb.append(AlertMessage.get("label.phd.date")).append(": ");
	sb.append(bean.getMeetingDate().toString("dd/MM/yyyy")).append("\n");
	sb.append(AlertMessage.get("label.phd.hour")).append(": ");
	sb.append(bean.getMeetingDate().toString("HH:mm")).append("\n");
	sb.append(AlertMessage.get("label.phd.meeting.place")).append(": ");
	sb.append(bean.getMeetingPlace());

	return sb.toString();
    }

}
