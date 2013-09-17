package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.List;

import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityApplicationProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplate;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityEmailTemplateType;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;

public class ReceptionEmailExecutedAction extends ReceptionEmailExecutedAction_Base {

    protected ReceptionEmailExecutedAction() {
        super();
    }

    protected ReceptionEmailExecutedAction(ExecutedActionType type, MobilityApplicationProcess applicationProcess,
            List<MobilityIndividualApplicationProcess> subjectCandidacyProcesses, MobilityEmailTemplate template) {
        this();

        init(type, applicationProcess, subjectCandidacyProcesses, template);

        sendEmails();
    }

    protected void init(ExecutedActionType type, MobilityApplicationProcess applicationProcess,
            List<MobilityIndividualApplicationProcess> subjectCandidacyProcesses, MobilityEmailTemplate template) {
        super.init(type, applicationProcess, subjectCandidacyProcesses);

        if (StringUtils.isEmpty(template.getSubject())) {
            throw new DomainException("error.reception.email.executed.action.subject.is.empty");
        }

        if (StringUtils.isEmpty(template.getBody())) {
            throw new DomainException("error.reception.email.executed.action.body.is.empty");
        }

        if (!ExecutedActionType.SENT_RECEPTION_EMAIL.equals(type)) {
            throw new DomainException("error.reception.email.executed.action.type.is.incorrect");
        }

        setMobilityEmailTemplate(template);
    }

    private void sendEmails() {
        getMobilityEmailTemplate().sendMultiEmailFor(getSubjectMobilityIndividualApplicationProcess());

    }

    protected static ReceptionEmailExecutedAction createAction(MobilityApplicationProcess applicationProcess,
            List<MobilityIndividualApplicationProcess> subjectCandidacyProcesses, MobilityEmailTemplate template) {
        return new ReceptionEmailExecutedAction(ExecutedActionType.SENT_RECEPTION_EMAIL, applicationProcess,
                subjectCandidacyProcesses, template);
    }

    public static ReceptionEmailExecutedAction createAction(MobilityApplicationProcess process, final SendReceptionEmailBean bean) {
        MobilityEmailTemplate receptionTemplate =
                process.getCandidacyPeriod().getEmailTemplateFor(bean.getMobilityProgram(),
                        MobilityEmailTemplateType.IST_RECEPTION);
        return createAction(bean.getMobilityApplicationProcess(), bean.getSubjectProcesses(), receptionTemplate);
    }
    @Deprecated
    public boolean hasBody() {
        return getBody() != null;
    }

    @Deprecated
    public boolean hasMobilityEmailTemplate() {
        return getMobilityEmailTemplate() != null;
    }

    @Deprecated
    public boolean hasBeginIntervalOfAcceptedStudents() {
        return getBeginIntervalOfAcceptedStudents() != null;
    }

    @Deprecated
    public boolean hasSubject() {
        return getSubject() != null;
    }

    @Deprecated
    public boolean hasEndIntervalOfAcceptedStudents() {
        return getEndIntervalOfAcceptedStudents() != null;
    }

}
