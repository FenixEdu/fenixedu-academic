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
package org.fenixedu.academic.domain.accounting.events.gratuity;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.bennu.core.domain.Bennu;

public class ExternalScholarshipGratuityExemptionJustification extends ExternalScholarshipGratuityExemptionJustification_Base {

    protected ExternalScholarshipGratuityExemptionJustification() {
    }

    public ExternalScholarshipGratuityExemptionJustification(final ExternalScholarshipGratuityExemption exemption,
            final ExternalScholarshipGratuityExemptionJustificationType justificationType, final String reason) {
        super();
        super.setRootDomainObject(Bennu.getInstance());
        init(exemption, justificationType, reason);
    }

    protected void init(ExternalScholarshipGratuityExemption exemption, ExternalScholarshipGratuityExemptionJustificationType justificationType,
            String reason) {
        super.init(exemption, reason);
        checkParameters(justificationType);
        super.setExternalScholarshipGratuityExemptionJustificationType(justificationType);
    }

    private void checkParameters(ExternalScholarshipGratuityExemptionJustificationType justificationType) {
        if (justificationType == null) {
            throw new DomainException("error.accounting.events.gratuity.ExternalScholarshipGratuityExemptionJustificationType.justificationType.cannot.be.null");
        }
    }

    @Override
    public void setExternalScholarshipGratuityExemptionJustificationType(
            ExternalScholarshipGratuityExemptionJustificationType justificationType) {
        throw new DomainException("error.accounting.events.gratuity.ExternalScholarshipGratuityExemptionJustificationType.cannot.modify.justificationType");
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();

        labelFormatter
                .appendLabel(getExternalScholarshipGratuityExemptionJustificationType().getQualifiedName(), Bundle.ENUMERATION)
                .appendLabel(" - ").appendLabel(getExemption().getEvent().getDescription().toString());

        return labelFormatter;
    }

}
