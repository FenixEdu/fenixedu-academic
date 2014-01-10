package net.sourceforge.fenixedu.domain.phd.email;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdProgramEmail extends PhdProgramEmail_Base {

    protected PhdProgramEmail() {
        super();
    }

    protected PhdProgramEmail(String subject, String body, String additionalTo, String additionalBccs, Person creator,
            DateTime date, PhdProgram program, List<PhdIndividualProgramProcess> individualProcessList) {
        init(subject, body, "", additionalBccs, creator, date);
        setPhdProgram(program);
        for (final PhdIndividualProgramProcess process : individualProcessList) {
            addPhdIndividualProgramProcesses(process);
        }
    }

    @Override
    protected Collection<? extends ReplyTo> getReplyTos() {
        return getPhdProgram().getPhdProgramUnit().getUnitBasedSender().iterator().next().getReplyTos();
    }

    @Override
    protected Sender getSender() {
        return getPhdProgram().getPhdProgramUnit().getUnitBasedSender().iterator().next();
    }

    @Override
    protected Collection<Recipient> getRecipients() {
        return Collections.emptyList();
    }

    @Override
    protected String getBccs() {
        StringBuilder builder = new StringBuilder();

        for (PhdIndividualProgramProcess process : getPhdIndividualProgramProcesses()) {
            if (process.getPerson().getEmailForSendingEmails() != null) {
                builder.append(process.getPerson().getEmailForSendingEmails()).append(",");
            }
        }

        builder.append(getAdditionalBcc());

        return builder.toString();
    }

    @Atomic
    static public PhdProgramEmail createEmail(PhdProgramEmailBean bean) {
        return new PhdProgramEmail(bean.getSubject(), bean.getMessage(), null, bean.getBccsWithSelectedParticipants(),
                bean.getCreator(), bean.getCreationDate(), bean.getPhdProgram(), bean.getSelectedElements());
    }

    @Atomic
    static public void validateEmailBean(PhdProgramEmailBean bean) {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ApplicationResources", Language.getLocale());

        if (bean.getSelectedElements().isEmpty() && StringUtils.isEmpty(bean.getBccs())) {
            throw new DomainException(resourceBundle.getString("error.email.validation.no.recipients"));
        }

        if (StringUtils.isEmpty(bean.getSubject())) {
            throw new DomainException(resourceBundle.getString("error.email.validation.subject.empty"));
        }

        if (StringUtils.isEmpty(bean.getMessage())) {
            throw new DomainException(resourceBundle.getString("error.email.validation.message.empty"));
        }

    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess> getPhdIndividualProgramProcesses() {
        return getPhdIndividualProgramProcessesSet();
    }

    @Deprecated
    public boolean hasAnyPhdIndividualProgramProcesses() {
        return !getPhdIndividualProgramProcessesSet().isEmpty();
    }

    @Deprecated
    public boolean hasPhdProgram() {
        return getPhdProgram() != null;
    }

}
