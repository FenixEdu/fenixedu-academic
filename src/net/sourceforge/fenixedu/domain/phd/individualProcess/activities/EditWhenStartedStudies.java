package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean;

public class EditWhenStartedStudies extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
        if (!PhdIndividualProgramProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
    	throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
    	Object object) {

        final RegistrationFormalizationBean bean = (RegistrationFormalizationBean) object;

        process.check(bean.getWhenStartedStudies(),
    	    "error.PhdIndividualProgramProcess.EditWhenStartedStudies.invalid.when.started.studies");

        process.setWhenStartedStudies(bean.getWhenStartedStudies());

        if (process.hasRegistration()) {
    	process.getRegistration().editStartDates(bean.getWhenStartedStudies(),
    		process.getCandidacyProcess().getWhenRatified(), bean.getWhenStartedStudies());
        }

        return process;
    }

}