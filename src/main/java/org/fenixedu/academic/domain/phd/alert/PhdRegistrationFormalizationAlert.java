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

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgramCalendarUtil;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.Recipient;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

public class PhdRegistrationFormalizationAlert extends PhdRegistrationFormalizationAlert_Base {

    public PhdRegistrationFormalizationAlert(PhdIndividualProgramProcess process, final int maxDays) {
        super();
        init(process);
        setMaxDays(maxDays);
    }

    private void init(final PhdIndividualProgramProcess process) {
        super.init(process, buildSubject(process), buildBody(process));
    }

    private MultiLanguageString buildSubject(final PhdIndividualProgramProcess process) {
        return new MultiLanguageString(Locale.getDefault(), AlertService.getSubjectPrefixed(process,
                "message.phd.alert.registration.formalization.subject"));
    }

    private MultiLanguageString buildBody(final PhdIndividualProgramProcess process) {
        return new MultiLanguageString(Locale.getDefault(), AlertService.getBodyText(process,
                "message.phd.alert.registration.formalization.body"));
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString(Bundle.PHD, "message.phd.alert.registration.formalization.description");
    }

    private LocalDate getWhenToFire() {
        return PhdProgramCalendarUtil.addWorkDaysTo(getProcess().getCandidacyProcess().getWhenRatified(), getMaxDays());
    }

    @Override
    protected boolean isToDiscard() {
        return getFireDate() != null || getProcess().isRegistrationFormalized();
    }

    @Override
    protected boolean isToFire() {
        return getProcess().getCandidacyProcess().getWhenRatified() != null && !new LocalDate().isBefore(getWhenToFire());
    }

    @Override
    public boolean isSystemAlert() {
        return true;
    }

    @Override
    protected void generateMessage() {
        final Group group =
                AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES, this.getProcess().getPhdProgram());

        Set<Person> members = group.getMembers().map(User::getPerson).collect(Collectors.toSet());
        new PhdAlertMessage(getProcess(), members, getFormattedSubject(), getFormattedBody());

        new Message(getSender(), new Recipient(group), buildMailSubject(), buildMailBody());

    }

    @Override
    public boolean isToSendMail() {
        return true;
    }

}
