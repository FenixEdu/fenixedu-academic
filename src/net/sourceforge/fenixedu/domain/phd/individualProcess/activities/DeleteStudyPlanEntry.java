package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanEntry;

public class DeleteStudyPlanEntry extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
        if (!PhdIndividualProgramProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
    	throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
    	Object object) {

        ((PhdStudyPlanEntry) object).delete();

        return process;
    }

}