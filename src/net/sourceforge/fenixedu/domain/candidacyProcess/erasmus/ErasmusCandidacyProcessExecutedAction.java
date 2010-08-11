package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.List;

public class ErasmusCandidacyProcessExecutedAction extends ErasmusCandidacyProcessExecutedAction_Base {
    
    protected ErasmusCandidacyProcessExecutedAction() {
	super();
    }

    protected ErasmusCandidacyProcessExecutedAction(ExecutedActionType type, ErasmusCandidacyProcess candidacyProcess,
	    List<ErasmusIndividualCandidacyProcess> subjectCandidacyProcesses) {
	this();
	init(type, candidacyProcess, subjectCandidacyProcesses);
    }

    protected void init(ExecutedActionType type, ErasmusCandidacyProcess candidacyProcess,
	    List<ErasmusIndividualCandidacyProcess> subjectCandidacyProcesses) {
	super.init(type);
	check(candidacyProcess, "error.erasmus.candidacy.process.executed.action.candidacy,process.is.null", null);

	setErasmusCandidacyProcess(candidacyProcess);
	getSubjectErasmusIndividualCandidacyProcess().addAll(subjectCandidacyProcesses);
    }

    public boolean isReceptionEmailExecutedAction() {
	return ExecutedActionType.SENT_RECEPTION_EMAIL.equals(getType());
    }
}
