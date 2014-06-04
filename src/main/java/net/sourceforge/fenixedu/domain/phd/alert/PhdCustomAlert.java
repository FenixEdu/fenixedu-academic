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
package net.sourceforge.fenixedu.domain.phd.alert;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;

public class PhdCustomAlert extends PhdCustomAlert_Base {

    protected PhdCustomAlert() {
        super();
    }

    public PhdCustomAlert(PhdIndividualProgramProcess process, Group targetGroup, MultiLanguageString subject,
            MultiLanguageString body, Boolean sendMail, LocalDate fireDate, Boolean userDefined, Boolean shared) {
        this();
        init(process, targetGroup, subject, body, sendMail, fireDate, userDefined, shared);
    }

    public PhdCustomAlert(PhdCustomAlertBean bean) {
        this(bean.getProcess(), bean.calculateTargetGroup(), new MultiLanguageString(Locale.getDefault(), bean.getSubject()),
                new MultiLanguageString(Locale.getDefault(), bean.getBody()), bean.isToSendEmail(), bean.getFireDate(), bean
                        .getUserDefined(), bean.getShared());
    }

    protected void init(PhdIndividualProgramProcess process, Group targetGroup, MultiLanguageString subject,
            MultiLanguageString body, Boolean sendEmail, LocalDate whenToFire, Boolean userDefined, Boolean shared) {

        super.init(process, subject, body);
        String[] args = {};

        if (whenToFire == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert.whenToFire.cannot.be.null",
                    args);
        }
        String[] args1 = {};
        if (targetGroup == null) {
            throw new DomainException("error.phd.alert.PhdAlert.targetGroup.cannot.be.null", args1);
        }
        String[] args2 = {};
        if (sendEmail == null) {
            throw new DomainException("error.phd.alert.PhdAlert.sendEmail.cannot.be.null", args2);
        }
        String[] args3 = {};
        if (userDefined == null) {
            throw new DomainException(
                    "error.net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert.userDefined.cannot.be.null", args3);
        }
        String[] args4 = {};
        if (shared == null) {
            throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert.shared.cannot.be.null",
                    args4);
        }

        super.setWhenToFire(whenToFire);
        super.setSendEmail(sendEmail);
        super.setTargetGroup(targetGroup.toPersistentGroup());
        super.setUserDefined(userDefined);
        super.setShared(shared);
    }

    protected Group getTargetAccessGroup() {
        return getTargetGroup().toGroup();
    }

    protected ImmutableSet<Person> getTargetPeople() {
        return FluentIterable.from(getTargetAccessGroup().getMembers()).transform(Person.userToPerson).toSet();
    }

    @Override
    public boolean isToFire() {
        return !new LocalDate().isBefore(getWhenToFire());
    }

    @Override
    protected boolean isToDiscard() {
        return getFireDate() != null;
    }

    @Override
    public boolean isCustomAlert() {
        return true;
    }

    @Override
    public String getDescription() {
        return MessageFormat.format(BundleUtil.getString(Bundle.PHD, "message.phd.alert.custom.description"),
                getTargetAccessGroup().getPresentationName(), getWhenToFire().toString(DateFormatUtil.DEFAULT_DATE_FORMAT),
                getFormattedSubject(), getFormattedBody());
    }

    @Override
    protected void generateMessage() {

        if (getShared().booleanValue()) {
            new PhdAlertMessage(getProcess(), getTargetPeople(), getFormattedSubject(), getFormattedBody());
        } else {
            for (final Person person : getTargetPeople()) {
                new PhdAlertMessage(getProcess(), person, getFormattedSubject(), getFormattedBody());
            }
        }

        if (isToSendMail()) {
            final Recipient recipient = new Recipient(getTargetAccessGroup());
            new Message(getSender(), recipient, buildMailSubject(), buildMailBody());

        }

    }

    public String getTargetGroupInText() {
        Group targetGroup = getTargetAccessGroup();

        Set<User> elements = targetGroup.getMembers();

        StringBuilder builder = new StringBuilder();

        for (User user : elements) {
            builder.append(user.getPerson().getName()).append(" (").append(user.getPerson().getEmailForSendingEmails())
                    .append(")\n");
        }

        return builder.toString();
    }

    @Override
    public void setSendEmail(Boolean sendEmail) {
        throw new DomainException("error.phd.alert.PhdAlert.cannot.modify.sendEmail");
    }

    @Override
    public void setUserDefined(Boolean userDefined) {
        throw new DomainException("error.phd.alert.PhdCustomAlert.cannot.modify.userDefined");
    }

    @Override
    public void setShared(Boolean shared) {
        throw new DomainException("error.phd.alert.PhdCustomAlert.cannot.modify.shared");
    }

    @Override
    public boolean isToSendMail() {
        return getSendEmail().booleanValue();
    }

    @Override
    public boolean isSystemAlert() {
        return !getUserDefined().booleanValue();
    }

    @Deprecated
    public boolean hasSendEmail() {
        return getSendEmail() != null;
    }

    @Deprecated
    public boolean hasShared() {
        return getShared() != null;
    }

    @Deprecated
    public boolean hasUserDefined() {
        return getUserDefined() != null;
    }

    @Deprecated
    public boolean hasWhenToFire() {
        return getWhenToFire() != null;
    }

}
