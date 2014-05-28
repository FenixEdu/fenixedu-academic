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

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class SecondCycleIndividualCandidacyExemptionJustification extends
        SecondCycleIndividualCandidacyExemptionJustification_Base {

    private SecondCycleIndividualCandidacyExemptionJustification() {
        super();
    }

    SecondCycleIndividualCandidacyExemptionJustification(final SecondCycleIndividualCandidacyExemption exemption,
            final CandidacyExemptionJustificationType justificationType) {
        this();
        checkParameters(justificationType);
        super.init(exemption, "");
        setCandidacyExemptionJustificationType(justificationType);
    }

    private void checkParameters(final CandidacyExemptionJustificationType justificationType) {
        if (justificationType == null) {
            throw new DomainException("error.SecondCycleIndividualCandidacyExemptionJustification.invalid.justificationType");
        }
    }

    @Override
    public LabelFormatter getDescription() {
        return new LabelFormatter().appendLabel(getCandidacyExemptionJustificationType().getQualifiedName(),
                LabelFormatter.ENUMERATION_RESOURCES);
    }

}
