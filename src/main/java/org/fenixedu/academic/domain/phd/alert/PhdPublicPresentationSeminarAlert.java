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

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.phd.InternalPhdParticipant;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.alert.AlertService.AlertMessage;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.domain.util.email.ReplyTo;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class PhdPublicPresentationSeminarAlert extends PhdPublicPresentationSeminarAlert_Base {

    static private final int FIRST_WARNING_DAYS = 30;

    static private int MAX_DAYS = 30 * 24; // days * months

    static private int MAX_DAYS_AFTER_LIMIT_REACHED = 30 * 3;

    private PhdPublicPresentationSeminarAlert() {
        super();
    }

    public PhdPublicPresentationSeminarAlert(final PhdIndividualProgramProcess process) {
        this();
        super.init(process, buildSubject(process), buildBody(process));
    }

    private LocalizedString buildSubject(final PhdIndividualProgramProcess process) {
        return new LocalizedString(Locale.getDefault(), AlertService.getSubjectPrefixed(process,
                AlertMessage.create("message.phd.alert.public.presentation.seminar.subject")));
    }

    private LocalizedString buildBody(final PhdIndividualProgramProcess process) {
        int days = getDaysUntilNow(process.getWhenStartedStudies());
        return new LocalizedString(Locale.getDefault(), AlertService.getBodyText(process, AlertMessage.create(
                "message.phd.alert.public.presentation.seminar.body", process.getWhenStartedStudies().toString("dd/MM/yyyy"),
                String.valueOf(days < 1 ? 1 : days), getGuidersNames(process))));
    }

    private int getDaysUntilNow(final LocalDate begin) {
        return Days.daysBetween(begin, new LocalDate()).getDays();
    }

    private String getGuidersNames(final PhdIndividualProgramProcess process) {
        final StringBuilder builder = new StringBuilder();
        final Iterator<PhdParticipant> values = process.getGuidingsSet().iterator();
        while (values.hasNext()) {
            builder.append(values.next().getName()).append(values.hasNext() ? ", " : "");
        }

        if (!process.getGuidingsSet().isEmpty()) {
            builder.insert(0, "(").insert(builder.length(), ")");
        }

        return builder.toString();
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString(Bundle.PHD, "message.phd.alert.public.presentation.seminar.description");
    }

    @Override
    protected boolean isToDiscard() {
        return !getProcess().getActiveState().isActive() || getProcess().getSeminarProcess() != null;
    }

    @Override
    public boolean isToFire() {

        if (hasExceedLimitDate()) {

            if (getFireDate() == null) {
                return true;
            }

            if (getDaysUntilNow(getFireDate().toLocalDate()) > MAX_DAYS_AFTER_LIMIT_REACHED) {
                return true;
            }
        }

        if (hasExceedFirstAlertDate() && getFireDate() == null) {
            return true;
        }

        return false;
    }

    private boolean hasExceedFirstAlertDate() {
        return !new LocalDate().isBefore(getFirstAlertDate());
    }

    private LocalDate getFirstAlertDate() {
        return getLimitDate().minusDays(FIRST_WARNING_DAYS);
    }

    private boolean hasExceedLimitDate() {
        return !new LocalDate().isBefore(getLimitDate());
    }

    private LocalDate getLimitDate() {
        return getProcess().getWhenStartedStudies().plusDays(MAX_DAYS);
    }

    @Override
    protected void generateMessage() {
        // initialize subject and body again with correct values
        super.init(buildSubject(getProcess()), buildBody(getProcess()));

        generateMessageForStudent();

        generateMessageForCoodinator();
        generateMessageForAcademicOffice();
        generateMessageForGuiders();

    }

    private void generateMessageForCoodinator() {
        generateMessage(Person.convertToUserGroup(getProcess().getPhdProgram().getCoordinatorsFor(
                ExecutionYear.readCurrentExecutionYear())));

    }

    private void generateMessageForAcademicOffice() {
        generateMessage(AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES, this.getProcess()
                .getPhdProgram()));
    }

    private void generateMessageForStudent() {
        generateMessage(getProcess().getPerson().getUser().groupOf());
    }

    private void generateMessageForGuiders() {
        for (final PhdParticipant guiding : getProcess().getGuidingsSet()) {
            if (guiding.isInternal()) {
                generateMessage(((InternalPhdParticipant) guiding).getPerson().getUser().groupOf());
            } else {
                new Message(getSender(), Collections.<ReplyTo> emptyList(), Collections.<Recipient> emptyList(),
                        buildMailSubject(), buildMailBody(), Collections.singleton(guiding.getEmail()));
            }
        }
    }

    private void generateMessage(Group group) {
        Set<Person> members = group.getMembers().map(User::getPerson).collect(Collectors.toSet());
        new PhdAlertMessage(getProcess(), members, getFormattedSubject(), getFormattedBody());
        new Message(getSender(), new Recipient("", group), buildMailSubject(), buildMailBody());
    }

    @Override
    public boolean isSystemAlert() {
        return true;
    }

    @Override
    public boolean isToSendMail() {
        return true;
    }

}
