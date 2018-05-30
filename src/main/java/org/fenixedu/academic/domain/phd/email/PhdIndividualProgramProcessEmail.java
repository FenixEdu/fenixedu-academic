/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.phd.email;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.messaging.core.domain.Sender;
import org.joda.time.DateTime;
import pt.ist.fenixframework.Atomic;

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
    protected String getReplyTo() {
        return getSender().getReplyTo();
    }

    @Override
    protected Sender getSender() {
        return this.getPhdIndividualProgramProcess().getAdministrativeOffice().getUnit().getSender();
    }

    @Override
    protected Group getRecipients() {
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
        if (bean.getParticipantsGroup().isEmpty() && bean.getSelectedParticipants().isEmpty()
                && StringUtils.isEmpty(bean.getBccs())) {
            return BundleUtil.getString(Bundle.APPLICATION, "error.email.validation.no.recipients");
        }

        if (StringUtils.isEmpty(bean.getSubject())) {
            return BundleUtil.getString(Bundle.APPLICATION, "error.email.validation.subject.empty");
        }

        if (StringUtils.isEmpty(bean.getMessage())) {
            return BundleUtil.getString(Bundle.APPLICATION, "error.email.validation.message.empty");
        }

        return null;
    }

}
