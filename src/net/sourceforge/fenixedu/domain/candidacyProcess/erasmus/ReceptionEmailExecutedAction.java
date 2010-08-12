package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.SystemSender;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ReceptionEmailExecutedAction extends ReceptionEmailExecutedAction_Base {
    
    protected ReceptionEmailExecutedAction() {
	super();
    }

    protected ReceptionEmailExecutedAction(ExecutedActionType type, ErasmusCandidacyProcess candidacyProcess,
	    List<ErasmusIndividualCandidacyProcess> subjectCandidacyProcesses, String subject, String body) {
	this();

	init(type, candidacyProcess, subjectCandidacyProcesses, subject, body);

	sendEmails();
    }
    

    protected void init(ExecutedActionType type, ErasmusCandidacyProcess candidacyProcess,
	    List<ErasmusIndividualCandidacyProcess> subjectCandidacyProcesses, String subject, String body) {
	super.init(type, candidacyProcess, subjectCandidacyProcesses);
	
	if(StringUtils.isEmpty(subject)) {
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

	for (ErasmusIndividualCandidacyProcess process : getSubjectErasmusIndividualCandidacyProcess())
	    new Message(systemSender, systemSender.getConcreteReplyTos(), Collections.EMPTY_LIST, getSubject(), getBody(),
		    process.getCandidacyHashCode().getEmail());

    }
    
    protected static ReceptionEmailExecutedAction createAction(ErasmusCandidacyProcess candidacyProcess,
	    List<ErasmusIndividualCandidacyProcess> subjectCandidacyProcesses, String subject, String body) {
	return new ReceptionEmailExecutedAction(ExecutedActionType.SENT_RECEPTION_EMAIL, candidacyProcess,
		subjectCandidacyProcesses, subject, body);
    }

    public static ReceptionEmailExecutedAction createAction(final SendReceptionEmailBean bean) {
	String subject = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale()).getString(
		"message.erasmus.reception.email.executed.action.subject");
	String body = ResourceBundle.getBundle("resources.CandidateResources", Language.getLocale()).getString(
		"message.erasmus.reception.email.executed.action.body");
	
	return createAction(bean.getErasmusCandidacyProcess(), bean.getSubjectProcesses(), subject, body);
    }
}
