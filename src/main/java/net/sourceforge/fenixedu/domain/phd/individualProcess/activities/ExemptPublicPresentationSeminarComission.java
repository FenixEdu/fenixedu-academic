package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessState;
import net.sourceforge.fenixedu.domain.phd.alert.PhdAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdPublicPresentationSeminarAlert;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessStateType;

import org.joda.time.LocalDate;

public class ExemptPublicPresentationSeminarComission extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (process.hasSeminarProcess() || process.getActiveState() != PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
        bean.setPresentationRequestDate(new LocalDate());
        bean.setPhdIndividualProgramProcess(process);

        final PublicPresentationSeminarProcess seminarProcess =
                Process.createNewProcess(userView, PublicPresentationSeminarProcess.class, object);

        seminarProcess.createState(PublicPresentationSeminarProcessStateType.EXEMPTED, userView.getPerson(), "");

        discardPublicSeminarAlerts(process);

        return process;
    }

    private void discardPublicSeminarAlerts(final PhdIndividualProgramProcess process) {
        for (final PhdAlert alert : process.getActiveAlerts()) {
            if (alert instanceof PhdPublicPresentationSeminarAlert) {
                alert.discard();
            }
        }
    }

}