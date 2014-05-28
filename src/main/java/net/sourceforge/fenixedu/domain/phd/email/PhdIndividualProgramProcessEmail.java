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
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.domain.util.email.Sender;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.I18N;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import java.util.Locale;

public class PhdIndividualProgramProcessEmail extends PhdIndividualProgramProcessEmail_Base {

    protected PhdIndividualProgramProcessEmail() {
        super();
    }

    protected PhdIndividualProgramProcessEmail(String subject, String message, String additionalTo, String additionalBcc,
            Person creator, PhdIndividualProgramProcess individualProcess) {
        init(subject, message, additionalTo, additionalBcc, creator, new DateTime());
        setPhdIndividualProgramProcess(individualProcess);
    }

    @Override
    protected Collection<? extends ReplyTo> getReplyTos() {
        return getSender().getReplyTosSet();
    }

    @Override
    protected Sender getSender() {
        return this.getPhdIndividualProgramProcess().getAdministrativeOffice().getUnit().getUnitBasedSender().iterator().next();
    }

    @Override
    protected Collection<Recipient> getRecipients() {
        return null;
    }

    @Override
    protected String getBccs() {
        return getAdditionalBcc();
    }

    @Atomic
    static public PhdIndividualProgramProcessEmail createEmail(PhdIndividualProgramProcessEmailBean bean) {
        final String errorWhileValidating = validateEmailBean(bean);
        if (errorWhileValidating != null) {
            throw new DomainException(errorWhileValidating);
        }

        final Person creator = AccessControl.getPerson();

        return new PhdIndividualProgramProcessEmail(bean.getSubject(), bean.getMessage(), null,
                bean.getBccsWithSelectedParticipants(), creator, bean.getProcess());
    }

    private static String validateEmailBean(PhdIndividualProgramProcessEmailBean bean) {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ApplicationResources", I18N.getLocale());

        if (bean.getParticipantsGroup().isEmpty() && bean.getSelectedParticipants().isEmpty()
                && StringUtils.isEmpty(bean.getBccs())) {
            return resourceBundle.getString("error.email.validation.no.recipients");
        }

        if (StringUtils.isEmpty(bean.getSubject())) {
            return resourceBundle.getString("error.email.validation.subject.empty");
        }

        if (StringUtils.isEmpty(bean.getMessage())) {
            return resourceBundle.getString("error.email.validation.message.empty");
        }

        return null;
    }

    @Deprecated
    public boolean hasPhdIndividualProgramProcess() {
        return getPhdIndividualProgramProcess() != null;
    }

}
