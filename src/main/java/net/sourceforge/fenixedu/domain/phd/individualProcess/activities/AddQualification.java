package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

public class AddQualification extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, User arg1) {
        // no precondition to check
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        return process.addQualification(userView != null ? userView.getPerson() : null, (QualificationBean) object);
    }
}
