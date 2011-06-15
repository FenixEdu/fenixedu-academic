package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlan;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class AddStudyPlan extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {

        if (!PhdIndividualProgramProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
    	throw new PreConditionNotValidException();
        }

        final PhdProgramCandidacyProcess candidacyProcess = process.getCandidacyProcess();

        if (candidacyProcess.hasState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION)) {
    	return;
        }

        throw new PreConditionNotValidException();
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
    	Object object) {

        new PhdStudyPlan((PhdStudyPlanBean) object);
        return process;
    }

}