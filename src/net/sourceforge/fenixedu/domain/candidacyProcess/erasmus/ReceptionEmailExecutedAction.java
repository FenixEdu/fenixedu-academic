package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

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

	setSubject(subject);
	setBody(body);
    }

    private void sendEmails() {
	// TODO Auto-generated method stub

    }
    
    public static List<ErasmusIndividualCandidacyProcess> obtainAllAcceptedCandidateProcesses(
	    final ErasmusCandidacyProcess candidacyProcess, final DateTime untilDate) {
	return null;
    }
}
