package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class UploadDocuments extends PhdProgramCandidacyProcessActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
		if (process.getActiveState() != PhdProgramCandidacyProcessState.PRE_CANDIDATE) {
			if (!process.isAllowedToManageProcess(userView)) {
				throw new PreConditionNotValidException();
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
		final List<PhdProgramDocumentUploadBean> documents = (List<PhdProgramDocumentUploadBean>) object;

		for (final PhdProgramDocumentUploadBean each : documents) {
			if (each.hasAnyInformation()) {
				process.addDocument(each, userView != null ? userView.getPerson() : null);
			}
		}

		return process;
	}

}
