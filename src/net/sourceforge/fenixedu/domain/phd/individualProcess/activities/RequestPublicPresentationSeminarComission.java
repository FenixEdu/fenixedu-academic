package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;

public class RequestPublicPresentationSeminarComission extends PhdIndividualProgramProcessActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
		if (process.hasSeminarProcess() || process.getActiveState() != PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
			throw new PreConditionNotValidException();
		}

		if (!process.isAllowedToManageProcess(userView) && !process.isGuider(userView.getPerson())) {
			throw new PreConditionNotValidException();
		}
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess individualProcess, IUserView userView,
			Object object) {

		PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
		bean.setPhdIndividualProgramProcess(individualProcess);

		final PublicPresentationSeminarProcess publicPresentationSeminarProcess =
				Process.createNewProcess(userView, PublicPresentationSeminarProcess.class, object);

		if (((PublicPresentationSeminarProcessBean) object).getGenerateAlert()) {
			AlertService.alertCoordinators(individualProcess,
					"message.phd.alert.public.presentation.seminar.comission.definition.subject",
					"message.phd.alert.public.presentation.seminar.comission.definition.body");

		}

		return individualProcess;
	}
}