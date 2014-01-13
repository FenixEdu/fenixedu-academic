package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlan;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

import org.fenixedu.bennu.core.domain.User;

public class AddStudyPlan extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

        final PhdProgramCandidacyProcess candidacyProcess = process.getCandidacyProcess();

        if (candidacyProcess.hasState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
            return;
        }

        throw new PreConditionNotValidException();
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {

        new PhdStudyPlan((PhdStudyPlanBean) object);
        return process;
    }

}