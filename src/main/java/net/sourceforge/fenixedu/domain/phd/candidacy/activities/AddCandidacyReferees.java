package net.sourceforge.fenixedu.domain.phd.candidacy.activities;

import java.util.List;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyReferee;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;

public class AddCandidacyReferees extends PhdProgramCandidacyProcessActivity {

    @Override
    protected void activityPreConditions(PhdProgramCandidacyProcess process, User userView) {
    }

    @SuppressWarnings("unchecked")
    @Override
    protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, User userView, Object object) {
        for (final PhdCandidacyRefereeBean bean : (List<PhdCandidacyRefereeBean>) object) {
            process.addCandidacyReferees(new PhdCandidacyReferee(process, bean));
        }
        return process;
    }

}
