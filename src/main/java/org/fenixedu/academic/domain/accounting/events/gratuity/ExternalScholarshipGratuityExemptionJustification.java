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

import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

/**
 * Use {@link org.fenixedu.academic.domain.accounting.events.EventExemptionJustification}
 */
@Deprecated
public class ExternalScholarshipGratuityExemptionJustification extends ExternalScholarshipGratuityExemptionJustification_Base {

    private ExternalScholarshipGratuityExemptionJustification() { }

    @Override
    public void setJustificationType(EventExemptionJustificationType justificationType) {
        throw new DomainException("error.accounting.events.gratuity.ExternalScholarshipGratuityExemptionJustificationType.cannot.modify.justificationType");
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();

        labelFormatter
                .appendLabel(getJustificationType().getQualifiedName(), Bundle.ENUMERATION)
                .appendLabel(" - ").appendLabel(getExemption().getEvent().getDescription().toString());

        return labelFormatter;
    }

}
