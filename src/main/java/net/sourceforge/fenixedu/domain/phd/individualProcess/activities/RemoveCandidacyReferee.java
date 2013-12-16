package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;

public class RemoveCandidacyReferee extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (!PhdProgramCandidacyProcessState.PRE_CANDIDATE.equals(process.getCandidacyProcess().getActiveState())) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        PhdCandidacyReferee referee = (PhdCandidacyReferee) object;

        if (referee.isLetterAvailable()) {
            throw new DomainException("error.PhdIndividualProgramProcess.candidacy.referee.has.letter.submitted");
        }

        referee.delete();

        return process;
    }
}