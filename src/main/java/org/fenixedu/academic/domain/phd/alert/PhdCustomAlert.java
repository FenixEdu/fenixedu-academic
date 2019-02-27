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
package org.fenixedu.academic.domain.phd.alert;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.messaging.core.domain.Message;
import org.fenixedu.messaging.core.domain.MessagingSystem;
import org.joda.time.LocalDate;

public class PhdCustomAlert extends PhdCustomAlert_Base {

    protected PhdCustomAlert() {
        super();
    }

    public PhdCustomAlert(PhdIndividualProgramProcess process, Group targetGroup, LocalizedString subject,
            LocalizedString body, Boolean sendMail, String otherRecipientsEmails, LocalDate fireDate, Boolean userDefined, Boolean shared) {
        this();
        init(process, targetGroup, subject, body, sendMail, otherRecipientsEmails, fireDate, userDefined, shared);
    }

    public PhdCustomAlert(PhdCustomAlertBean bean) {
        this(bean.getProcess(), bean.calculateTargetGroup(), new LocalizedString(Locale.getDefault(), bean.getSubject()),
                new LocalizedString(Locale.getDefault(), bean.getBody()), bean.isToSendEmail(), bean.getOtherRecipientsEmails(), bean.getFireDate(), bean
                        .getUserDefined(), bean.getShared());
    }

    protected void init(PhdIndividualProgramProcess process, Group targetGroup, LocalizedString subject,
            LocalizedString body, Boolean sendEmail, String otherRecipientsEmails, LocalDate whenToFire, Boolean userDefined, Boolean shared) {

        super.init(process, subject, body);
        String[] args = {};

        if (whenToFire == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.phd.alert.PhdCustomAlert.whenToFire.cannot.be.null",
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
            throw new DomainException("error.org.fenixedu.academic.domain.phd.alert.PhdCustomAlert.userDefined.cannot.be.null",
                    args3);
        }
        String[] args4 = {};
        if (shared == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.phd.alert.PhdCustomAlert.shared.cannot.be.null", args4);
        }

        super.setWhenToFire(whenToFire);
        super.setSendEmail(sendEmail);
        super.setOtherRecipientsEmails(otherRecipientsEmails);
        super.setTargetGroup(targetGroup.toPersistentGroup());
        super.setUserDefined(userDefined);
        super.setShared(shared);
    }

    protected Group getTargetAccessGroup() {
        return getTargetGroup().toGroup();
    }

    protected Set<Person> getTargetPeople() {
        return getTargetAccessGroup().getMembers().map(User::getPerson).collect(Collectors.toSet());
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
                getTargetAccessGroup().getPresentationName(), getWhenToFire().toString("dd/MM/yyyy"), getFormattedSubject(),
                getFormattedBody());
    }

    @Override
    protected void generateMessage() {

        if (getShared()) {
            new PhdAlertMessage(getProcess(), getTargetPeople(), getFormattedSubject(), getFormattedBody());
        } else {
            for (final Person person : getTargetPeople()) {
                new PhdAlertMessage(getProcess(), person, getFormattedSubject(), getFormattedBody());
            }
        }

        if (isToSendMail()) {
            Message.from(getSender()).to(getTargetAccessGroup())
                    .singleBcc(MessagingSystem.Util.toEmailSet(getOtherRecipientsEmails()))
                    .subject(buildMailSubject()).textBody(buildMailBody())
                    .send();
        }

    }

    public String getTargetGroupInText() {
        Group targetGroup = getTargetAccessGroup();

        StringBuilder builder = new StringBuilder();
        targetGroup.getMembers().forEach(
                user -> builder.append(user.getPerson().getName()).append(" (")
                        .append(user.getPerson().getEmailForSendingEmails()).append(")\n"));
        return builder.toString();
    }

    public String getOtherRecipientsInFormattedText() {
        StringBuilder builder = new StringBuilder();

        Arrays.stream(getOtherRecipientsEmails().split(",")).forEach(
                email -> builder.append(email.trim()).append("\n"));
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
        return getSendEmail();
    }

    @Override
    public boolean isSystemAlert() {
        return !getUserDefined();
    }

    @Override
    public void delete() {
        this.setTargetGroup(null);
        super.delete();
    }
}
