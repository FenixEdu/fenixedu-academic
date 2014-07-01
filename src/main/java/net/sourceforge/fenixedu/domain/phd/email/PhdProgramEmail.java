/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.phd.email;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

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
        if (bean.getSelectedElements().isEmpty() && StringUtils.isEmpty(bean.getBccs())) {
            throw new DomainException(BundleUtil.getString(Bundle.APPLICATION, "error.email.validation.no.recipients"));
        }

        if (StringUtils.isEmpty(bean.getSubject())) {
            throw new DomainException(BundleUtil.getString(Bundle.APPLICATION, "error.email.validation.subject.empty"));
        }

        if (StringUtils.isEmpty(bean.getMessage())) {
            throw new DomainException(BundleUtil.getString(Bundle.APPLICATION, "error.email.validation.message.empty"));
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
