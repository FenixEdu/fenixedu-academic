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
import java.util.Locale;

import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgramCalendarUtil;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class PhdFinalProofRequestAlert extends PhdFinalProofRequestAlert_Base {

    static private int MAX_DAYS = 5 * 365; // years * days

    private PhdFinalProofRequestAlert() {
        super();
    }

    public PhdFinalProofRequestAlert(final PhdIndividualProgramProcess process) {
        this();
        super.init(process, buildSubject(process), buildBody(process));
    }

    private MultiLanguageString buildSubject(final PhdIndividualProgramProcess process) {
        return new MultiLanguageString(Locale.getDefault(), AlertService.getSubjectPrefixed(process,
                "message.phd.alert.final.proof.request.subject"));
    }

    private MultiLanguageString buildBody(final PhdIndividualProgramProcess process) {
        return new MultiLanguageString(Locale.getDefault(), AlertService.getBodyText(process,
                "message.phd.alert.final.proof.request.body"));
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString(Bundle.PHD, "message.phd.alert.final.proof.request.description");
    }

    @Override
    protected boolean isToDiscard() {
        return getProcess().getThesisProcess() != null || !getProcess().getActiveState().isPhdActive();
    }

    @Override
    protected boolean isToFire() {
        if (getFireDate() == null) {
            return !new LocalDate()
                    .isBefore(PhdProgramCalendarUtil.addWorkDaysTo(getProcess().getWhenStartedStudies(), MAX_DAYS));
        }

        return !new LocalDate().isBefore(PhdProgramCalendarUtil.addWorkDaysTo(getFireDate().toLocalDate(), 25));
    }

    @Override
    protected void generateMessage() {
        // initialize subject and body again with correct values
        super.init(buildSubject(getProcess()), buildBody(getProcess()));

        // TODO: add missing elements (Coordinator, AcademicOffice?)
        new PhdAlertMessage(getProcess(), getProcess().getPerson(), getFormattedSubject(), getFormattedBody());
        new Message(getSender(), new Recipient(Collections.singletonList(getProcess().getPerson())), buildMailSubject(),
                buildMailBody());
    }

    @Override
    public boolean isToSendMail() {
        return true;
    }

    @Override
    public boolean isSystemAlert() {
        return true;
    }

}
