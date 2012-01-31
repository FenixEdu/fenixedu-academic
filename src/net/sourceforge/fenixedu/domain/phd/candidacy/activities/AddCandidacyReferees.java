package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class AddCandidacyReferees extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
    }

	@Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	for (final PhdCandidacyRefereeBean bean : (List<PhdCandidacyRefereeBean>) object) {
	    process.addCandidacyReferees(new PhdCandidacyReferee(process, bean));
	}
	return process;
    }

}
