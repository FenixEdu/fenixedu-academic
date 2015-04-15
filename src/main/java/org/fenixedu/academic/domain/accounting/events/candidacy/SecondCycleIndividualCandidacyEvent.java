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
package org.fenixedu.academic.domain.accounting.events.candidacy;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.Exemption;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOfficeType;
import org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacy;
import org.fenixedu.academic.util.LabelFormatter;

public class SecondCycleIndividualCandidacyEvent extends SecondCycleIndividualCandidacyEvent_Base {

    private SecondCycleIndividualCandidacyEvent() {
        super();
    }

    public SecondCycleIndividualCandidacyEvent(final SecondCycleIndividualCandidacy candidacy, final Person person) {
        this();
        super.init(candidacy, EventType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY, person);

        attachAvailablePaymentCode(person);
    }

    @Override
    protected AdministrativeOffice readAdministrativeOffice() {
        return AdministrativeOffice.readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

    @Override
    public boolean isExemptionAppliable() {
        return true;
    }

    public boolean hasSecondCycleIndividualCandidacyExemption() {
        return getSecondCycleIndividualCandidacyExemption() != null;
    }

    public SecondCycleIndividualCandidacyExemption getSecondCycleIndividualCandidacyExemption() {
        for (final Exemption exemption : getExemptionsSet()) {
            if (exemption instanceof SecondCycleIndividualCandidacyExemption) {
                return (SecondCycleIndividualCandidacyExemption) exemption;
            }
        }
        return null;
    }

    @Override
    protected EntryType getEntryType() {
        return EntryType.SECOND_CYCLE_INDIVIDUAL_CANDIDACY_FEE;
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = super.getDescription();
        labelFormatter.appendLabel(" - ");
        for (Degree s : getIndividualCandidacy().getAllDegrees()) {
            labelFormatter.appendLabel(s.getSigla()).appendLabel(" ");
        }
        labelFormatter.appendLabel(" - ").appendLabel(getIndividualCandidacy().getCandidacyDate().toString());

        return labelFormatter;
    }
}
