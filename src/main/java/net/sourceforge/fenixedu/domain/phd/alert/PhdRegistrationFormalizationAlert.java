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

import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCalendarUtil;
import net.sourceforge.fenixedu.domain.util.email.Message;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.collect.FluentIterable;

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
        return hasFireDate() || getProcess().isRegistrationFormalized();
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
                AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES, this
        .getProcess().getPhdProgram());

        Set<Person> members = FluentIterable.from(group.getMembers()).transform(Person.userToPerson).toSet();
        new PhdAlertMessage(getProcess(), members, getFormattedSubject(), getFormattedBody());

        new Message(getSender(), new Recipient(group), buildMailSubject(), buildMailBody());

    }

    @Override
    public boolean isToSendMail() {
        return true;
    }

    @Deprecated
    public boolean hasMaxDays() {
        return getMaxDays() != null;
    }

}
