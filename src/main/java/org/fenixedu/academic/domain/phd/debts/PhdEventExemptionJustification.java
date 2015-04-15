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
package org.fenixedu.academic.domain.phd.debts;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.joda.time.LocalDate;

public class PhdEventExemptionJustification extends PhdEventExemptionJustification_Base {

    private PhdEventExemptionJustification() {
        super();
    }

    public PhdEventExemptionJustification(PhdEventExemption exemption, PhdEventExemptionJustificationType justificationType,
            LocalDate dispatchDate, String reason) {

        this();
        String[] args = {};

        if (justificationType == null) {
            throw new DomainException("error.PhdEventExemptionJustificationType.invalid.justification.type", args);
        }
        String[] args1 = {};
        if (dispatchDate == null) {
            throw new DomainException("error.PhdEventExemptionJustificationType.invalid.dispatch.date", args1);
        }

        init(exemption, reason);
        setJustificationType(justificationType);
        setDispatchDate(dispatchDate);

    }

    @Override
    public LabelFormatter getDescription() {
        return new LabelFormatter().appendLabel(getJustificationType().getQualifiedName(), Bundle.ENUMERATION);
    }

}
