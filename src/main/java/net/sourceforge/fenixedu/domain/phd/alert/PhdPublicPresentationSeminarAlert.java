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

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.collect.FluentIterable;

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

    private MultiLanguageString buildSubject(final PhdIndividualProgramProcess process) {
        return new MultiLanguageString(Locale.getDefault(), AlertService.getSubjectPrefixed(process,
                AlertMessage.create("message.phd.alert.public.presentation.seminar.subject")));
    }

    private MultiLanguageString buildBody(final PhdIndividualProgramProcess process) {
        int days = getDaysUntilNow(process.getWhenStartedStudies());
        return new MultiLanguageString(Locale.getDefault(), AlertService.getBodyText(process, AlertMessage.create(
                "message.phd.alert.public.presentation.seminar.body", process.getWhenStartedStudies().toString("dd/MM/yyyy"),
                String.valueOf(days < 1 ? 1 : days), getGuidersNames(process))));
    }

    private int getDaysUntilNow(final LocalDate begin) {
        return Days.daysBetween(begin, new LocalDate()).getDays();
    }

    private String getGuidersNames(final PhdIndividualProgramProcess process) {
        final StringBuilder builder = new StringBuilder();
        final Iterator<PhdParticipant> values = process.getGuidings().iterator();
        while (values.hasNext()) {
            builder.append(values.next().getName()).append(values.hasNext() ? ", " : "");
        }

        if (process.hasAnyGuidings()) {
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
        return !getProcess().getActiveState().isActive() || getProcess().hasSeminarProcess();
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
        generateMessage(UserGroup.of(Person.convertToUsers(getProcess().getPhdProgram().getCoordinatorsFor(
        ExecutionYear.readCurrentExecutionYear()))));

    }

    private void generateMessageForAcademicOffice() {
        generateMessage(AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES, this.getProcess().getPhdProgram()));
    }

    private void generateMessageForStudent() {
        generateMessage(UserGroup.of(getProcess().getPerson().getUser()));
    }

    private void generateMessageForGuiders() {
        for (final PhdParticipant guiding : getProcess().getGuidings()) {
            if (guiding.isInternal()) {
                generateMessage(UserGroup.of(((InternalPhdParticipant) guiding).getPerson().getUser()));
            } else {
                new Message(getSender(), Collections.<ReplyTo> emptyList(), Collections.<Recipient> emptyList(),
                        buildMailSubject(), buildMailBody(), Collections.singleton(guiding.getEmail()));
            }
        }
    }

    private void generateMessage(Group group) {
        Set<Person> members = FluentIterable.from(group.getMembers()).transform(Person.userToPerson).toSet();
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
