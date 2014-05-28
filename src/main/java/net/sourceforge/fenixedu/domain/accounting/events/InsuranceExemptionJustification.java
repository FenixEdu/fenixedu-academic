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
package net.sourceforge.fenixedu.domain.accounting.events;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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
        labelFormatter.appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);

        return labelFormatter;
    }

    @Deprecated
    public boolean hasJustificationType() {
        return getJustificationType() != null;
    }

}
