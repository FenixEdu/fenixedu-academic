package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess;
import net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

public class ConcludePhdProcess extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	if (!PhdThesisProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    throw new PreConditionNotValidException();
	}

	if (!process.isConcluded()) {
	    throw new PreConditionNotValidException();
	}

	if (process.getIndividualProgramProcess().hasRegistration()
		&& !process.getIndividualProgramProcess().getRegistration().isRegistrationConclusionProcessed()) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	PhdConclusionProcessBean bean = (PhdConclusionProcessBean) object;
	PhdConclusionProcess.create(bean, userView.getPerson());

	return process;
    }

}
