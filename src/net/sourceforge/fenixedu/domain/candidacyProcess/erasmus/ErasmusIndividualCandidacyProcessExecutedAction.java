package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ErasmusIndividualCandidacyProcessExecutedAction extends ErasmusIndividualCandidacyProcessExecutedAction_Base {

    private ErasmusIndividualCandidacyProcessExecutedAction() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ErasmusIndividualCandidacyProcessExecutedAction(MobilityIndividualApplicationProcess process, ExecutedActionType type) {
	this();

	if (type == null) {
	    throw new DomainException("error.executed.action.type.mandatory");
	}

	if (process == null) {
	    throw new DomainException("error.executed.action.process.mandatory");
	}

	init(type);
	setMobilityIndividualApplicationProcess(process);
    }

    public boolean isSentEmailForRequiredDocumentsExecutedAction() {
	return ExecutedActionType.SENT_EMAIL_FOR_MISSING_REQUIRED_DOCUMENTS.equals(getType());
    }
}
