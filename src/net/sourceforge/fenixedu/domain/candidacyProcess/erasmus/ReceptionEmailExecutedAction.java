package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;

import org.apache.commons.lang.StringUtils;

public class ReceptionEmailExecutedAction extends ReceptionEmailExecutedAction_Base {

    protected ReceptionEmailExecutedAction() {
	super();
    }

    protected ReceptionEmailExecutedAction(ExecutedActionType type, MobilityApplicationProcess applicationProcess,
	    List<MobilityIndividualApplicationProcess> subjectCandidacyProcesses, String subject, String body) {
	this();

	init(type, applicationProcess, subjectCandidacyProcesses, subject, body);

	sendEmails();
    }

    protected void init(ExecutedActionType type, MobilityApplicationProcess applicationProcess,
	    List<MobilityIndividualApplicationProcess> subjectCandidacyProcesses, String subject, String body) {
	super.init(type, applicationProcess, subjectCandidacyProcesses);

	if (StringUtils.isEmpty(subject)) {
	    throw new DomainException("error.reception.email.executed.action.subject.is.empty");
	}

	if (StringUtils.isEmpty(body)) {
	    throw new DomainException("error.reception.email.executed.action.body.is.empty");
	}

	if (!ExecutedActionType.SENT_RECEPTION_EMAIL.equals(type)) {
	    throw new DomainException("error.reception.email.executed.action.type.is.incorrect");
	}

	setSubject(subject);
	setBody(body);
    }

    private void sendEmails() {
	SystemSender systemSender = RootDomainObject.getInstance().getSystemSender();

	for (MobilityIndividualApplicationProcess process : getSubjectMobilityIndividualApplicationProcess())
	    new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, getSubject(), getBody(),
		    process.getCandidacyHashCode().getEmail());

    }

    protected static ReceptionEmailExecutedAction createAction(MobilityApplicationProcess applicationProcess,
	    List<MobilityIndividualApplicationProcess> subjectCandidacyProcesses, String subject, String body) {
	return new ReceptionEmailExecutedAction(ExecutedActionType.SENT_RECEPTION_EMAIL, applicationProcess,
		subjectCandidacyProcesses, subject, body);
    }

    public static ReceptionEmailExecutedAction createAction(MobilityApplicationProcess process, final SendReceptionEmailBean bean) {
	return createAction(bean.getMobilityApplicationProcess(), bean.getSubjectProcesses(), process.getReceptionEmailSubject(),
		process.getReceptionEmailBody());
    }
}
