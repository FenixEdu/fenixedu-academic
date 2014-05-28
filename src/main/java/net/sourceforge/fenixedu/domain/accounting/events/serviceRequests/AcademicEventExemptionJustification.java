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
package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.accounting.events.AcademicEventExemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class AcademicEventExemptionJustification extends AcademicEventExemptionJustification_Base {

    private AcademicEventExemptionJustification() {
        super();
    }

    public AcademicEventExemptionJustification(final AcademicEventExemption exemption,
            final AcademicEventJustificationType justificationType, final LocalDate dispatchDate, final String reason) {

        this();
        String[] args = {};

        if (justificationType == null) {
            throw new DomainException("error.AcademicEventExemptionJustification.invalid.justificationType", args);
        }
        String[] args1 = {};
        if (dispatchDate == null) {
            throw new DomainException("error.AcademicEventExemptionJustification.invalid.dispatchDate", args1);
        }

        super.init(exemption, reason);
        setJustificationType(justificationType);
        setDispatchDate(dispatchDate);
    }

    @Override
    public LabelFormatter getDescription() {
        return new LabelFormatter().appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);
    }

    @Deprecated
    public boolean hasJustificationType() {
        return getJustificationType() != null;
    }

    @Deprecated
    public boolean hasDispatchDate() {
        return getDispatchDate() != null;
    }

}
