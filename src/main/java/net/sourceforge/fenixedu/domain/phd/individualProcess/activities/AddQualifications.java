package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import java.util.List;

import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

import org.fenixedu.bennu.core.domain.User;

public class AddQualifications extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, User arg1) {
        // no precondition to check
    }

    @SuppressWarnings("unchecked")
    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        for (final QualificationBean bean : (List<QualificationBean>) object) {
            process.addQualification(userView != null ? userView.getPerson() : null, bean);
        }
        return process;
    }
}
