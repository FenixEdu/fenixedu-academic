package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean;

public class EditWhenStartedStudies extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {

        final RegistrationFormalizationBean bean = (RegistrationFormalizationBean) object;
        Object obj = bean.getWhenStartedStudies();
        String[] args = {};

        if (obj == null) {
            throw new DomainException("error.PhdIndividualProgramProcess.EditWhenStartedStudies.invalid.when.started.studies",
                    args);
        }

        process.setWhenStartedStudies(bean.getWhenStartedStudies());

        if (process.hasRegistration()) {
            process.getRegistration().editStartDates(bean.getWhenStartedStudies(),
                    process.getCandidacyProcess().getWhenRatified(), bean.getWhenStartedStudies());
        }

        return process;
    }

}