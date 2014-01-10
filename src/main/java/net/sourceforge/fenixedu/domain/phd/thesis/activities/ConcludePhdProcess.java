package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess;
import net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

import org.fenixedu.bennu.core.domain.User;

public class ConcludePhdProcess extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
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
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        PhdConclusionProcessBean bean = (PhdConclusionProcessBean) object;
        PhdConclusionProcess.create(bean, userView.getPerson());

        PhdIndividualProgramProcess individualProgramProcess = process.getIndividualProgramProcess();

        if (!PhdIndividualProgramProcessState.CONCLUDED.equals(individualProgramProcess.getActiveState())) {
            individualProgramProcess.createState(PhdIndividualProgramProcessState.CONCLUDED, userView.getPerson(), "");
        }

        process.getPerson().addPersonRoleByRoleType(RoleType.ALUMNI);

        return process;
    }

}
