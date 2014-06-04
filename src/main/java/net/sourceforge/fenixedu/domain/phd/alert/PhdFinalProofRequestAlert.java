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

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCalendarUtil;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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
        return getProcess().hasThesisProcess() || !getProcess().getActiveState().isPhdActive();
    }

    @Override
    protected boolean isToFire() {
        if (!hasFireDate()) {
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
