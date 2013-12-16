package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;

public class RequestPublicThesisPresentation extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {

        if (!process.hasSeminarProcess()
                || (!process.getSeminarProcess().isExempted() && !process.getSeminarProcess().isConcluded())) {
            throw new PreConditionNotValidException();
        }

        if (process.hasThesisProcess() || process.getActiveState() != PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }

    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess individualProcess, User userView,
            Object object) {

        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
        bean.setProcess(individualProcess);

        Process.createNewProcess(userView, PhdThesisProcess.class, bean);
        individualProcess.createState(PhdIndividualProgramProcessState.THESIS_DISCUSSION, userView.getPerson(), "");

        return individualProcess;
    }

}