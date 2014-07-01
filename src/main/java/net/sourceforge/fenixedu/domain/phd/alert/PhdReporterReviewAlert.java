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
import java.util.Locale;

import net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService.AlertMessage;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.PhdThesisActivity;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.domain.util.email.ReplyTo;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class PhdReporterReviewAlert extends PhdReporterReviewAlert_Base {

    static private final int DAYS_UNTIL_WARNING = 35;
    static private final int DAYS_UNTIL_DEADLINE = 40;

    public PhdReporterReviewAlert(final PhdIndividualProgramProcess process, PhdParticipant participant) {
        super();
        super.init(process, buildSubject(process), buildBody(process, participant));
        setPhdParticipant(participant);
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString(Bundle.PHD, "message.phd.request.reminder.jury.reviews.report.description");
    }

    @Override
    protected boolean isToDiscard() {
        if (hasFireDate()) {
            return true;
        }

        PhdProgramProcessDocument feedbackDocument =
                getProcess().getLatestDocumentVersionFor(PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK);
        if (feedbackDocument != null && feedbackDocument.getUploadTime().isAfter(getWhenCreated())) {
            return true;
        }

        return false;
    }

    @Override
    protected boolean isToFire() {
        if (!hasExceededAlertDate()) {
            return false;
        }

        if (!hasFireDate()) {
            return true;
        }

        return false;
    }

    private boolean hasExceededAlertDate() {
        return !new LocalDate().isBefore(getLimitDateForAlert());
    }

    private LocalDate getLimitDateForAlert() {
        return getProcess().getThesisProcess().getWhenJuryValidated().plusDays(DAYS_UNTIL_WARNING);
    }

    private int getDaysLeftUntilDeadline(PhdIndividualProgramProcess process) {
        return Days.daysBetween(new LocalDate(), getLimitDateForDeadline(process)).getDays();
    }

    private LocalDate getLimitDateForDeadline(PhdIndividualProgramProcess process) {
        return process.getThesisProcess().getWhenJuryValidated().plusDays(DAYS_UNTIL_DEADLINE);
    }

    @Override
    protected void generateMessage() {
        generateMessageForReporters();
    }

    private MultiLanguageString buildSubject(final PhdIndividualProgramProcess process) {
        return new MultiLanguageString(Locale.getDefault(), AlertMessage.get(
                "message.phd.request.jury.reviews.external.access.subject", process.getPhdProgram().getName()));
    }

    private MultiLanguageString buildBody(final PhdIndividualProgramProcess process, PhdParticipant participant) {
        return new MultiLanguageString(Locale.getDefault(), AlertMessage.get(
                "message.phd.request.reminder.jury.reviews.reporter.body", process.getPerson().getName(),
                process.getProcessNumber(), getDaysLeftUntilDeadline(process))
                + "\n\n"
                + PhdThesisActivity.getAccessInformation(process, participant,
                        "message.phd.request.jury.reviews.coordinator.access", "message.phd.request.jury.reviews.teacher.access"));
    }

    private void generateMessageForReporters() {
        PhdParticipant participant = getPhdParticipant();
        if (participant.isInternal()) {
            InternalPhdParticipant internalParticipant = (InternalPhdParticipant) participant;
            new PhdAlertMessage(getProcess(), internalParticipant.getPerson(), getFormattedSubject(), buildBody(getProcess(),
                    participant));
            new Message(getSender(), new Recipient(Collections.singleton(internalParticipant.getPerson())), buildMailSubject(),
                    buildMailBody());
        } else {
            new Message(getSender(), Collections.<ReplyTo> emptyList(), Collections.<Recipient> emptyList(), buildMailSubject(),
                    buildMailBody(), Collections.singleton(participant.getEmail()));
        }

    }

    @Override
    public boolean isToSendMail() {
        return true;
    }

    public static int getReporterReviewDeadlineDays() {
        return DAYS_UNTIL_DEADLINE;
    }

    @Deprecated
    public boolean hasPhdParticipant() {
        return getPhdParticipant() != null;
    }

}
