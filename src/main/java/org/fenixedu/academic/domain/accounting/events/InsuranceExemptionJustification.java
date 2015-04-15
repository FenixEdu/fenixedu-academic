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
package org.fenixedu.academic.domain.accounting.events;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

public class InsuranceExemptionJustification extends InsuranceExemptionJustification_Base {

    public InsuranceExemptionJustification() {
        super();
    }

    public InsuranceExemptionJustification(InsuranceExemption exemption, InsuranceExemptionJustificationType justificationType,
            String reason) {
        this();
        init(exemption, justificationType, reason);
    }

    protected void init(InsuranceExemption exemption, InsuranceExemptionJustificationType justificationType, String reason) {
        super.init(exemption, reason);
        checkParameters(justificationType);
        super.setJustificationType(justificationType);
    }

    private void checkParameters(InsuranceExemptionJustificationType justificationType) {
        if (justificationType == null) {
            throw new DomainException("error.accounting.events.InsuranceExemptionJustification.justificationType.cannot.be.null");
        }

    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getJustificationType().getQualifiedName(), Bundle.ENUMERATION);

        return labelFormatter;
    }

}
