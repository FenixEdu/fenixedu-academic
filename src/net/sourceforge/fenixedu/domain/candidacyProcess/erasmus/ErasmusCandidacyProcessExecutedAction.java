package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;

public class ErasmusCandidacyProcessExecutedAction extends ErasmusCandidacyProcessExecutedAction_Base {

    protected ErasmusCandidacyProcessExecutedAction() {
        super();
    }

    protected ErasmusCandidacyProcessExecutedAction(ExecutedActionType type, MobilityApplicationProcess applicationProcess,
            List<MobilityIndividualApplicationProcess> subjectCandidacyProcesses) {
        this();
        init(type, applicationProcess, subjectCandidacyProcesses);
    }

    protected void init(ExecutedActionType type, MobilityApplicationProcess applicationProcess,
            List<MobilityIndividualApplicationProcess> subjectCandidacyProcesses) {
        super.init(type);
        check(applicationProcess, "error.erasmus.candidacy.process.executed.action.candidacy,process.is.null", null);

        setMobilityApplicationProcess(applicationProcess);
        getSubjectMobilityIndividualApplicationProcess().addAll(subjectCandidacyProcesses);
    }

    public boolean isReceptionEmailExecutedAction() {
        return ExecutedActionType.SENT_RECEPTION_EMAIL.equals(getType());
    }
}
