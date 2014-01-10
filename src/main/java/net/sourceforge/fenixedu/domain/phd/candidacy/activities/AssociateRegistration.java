package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean;

import org.fenixedu.bennu.core.domain.User;

public class AssociateRegistration extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {

        if (!process.isInState(PhdProgramCandidacyProcessState.CONCLUDED)) {
            throw new PreConditionNotValidException();
        }

        if (!process.hasStudyPlan() || process.getIndividualProgramProcess().hasRegistration()) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {
        return process.associateRegistration((RegistrationFormalizationBean) object);
    }

}
