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
package net.sourceforge.fenixedu.domain.accounting.events.candidacy;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle.SecondCycleIndividualCandidacy;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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
