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
package org.fenixedu.academic.domain.accounting.events.penaltyExemptionJustifications;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.accounting.events.PenaltyExemption;
import org.fenixedu.academic.domain.accounting.events.PenaltyExemptionJustificationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.joda.time.YearMonthDay;

public class PenaltyExemptionJustificationByDispatch extends PenaltyExemptionJustificationByDispatch_Base {

    protected PenaltyExemptionJustificationByDispatch() {
        super();
    }

    public PenaltyExemptionJustificationByDispatch(final PenaltyExemption penaltyExemption,
            final PenaltyExemptionJustificationType justificationType, final String reason, final YearMonthDay dispatchDate) {
        this();
        init(penaltyExemption, justificationType, reason, dispatchDate);
    }

    private void init(PenaltyExemption penaltyExemption, PenaltyExemptionJustificationType justificationType, String reason,
            YearMonthDay dispatchDate) {
        super.init(penaltyExemption, justificationType, reason);
        checkParameters(justificationType, dispatchDate, reason);
        super.setPenaltyExemptionDispatchDate(dispatchDate);
    }

    private void checkParameters(final PenaltyExemptionJustificationType justificationType, final YearMonthDay dispatchDate,
            final String reason) {
        if (dispatchDate == null || StringUtils.isEmpty(reason)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.events.penaltyExemptionJustifications.PenaltyExemptionJustificationByDispatch.dispatchDate.and.reason.are.required",
                    new LabelFormatter(justificationType.getQualifiedName(), Bundle.ENUMERATION));
        }

    }

    @Override
    public void setPenaltyExemptionDispatchDate(YearMonthDay penaltyExemptionDispatchDate) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.accounting.events.penaltyExemptionJustifications.PenaltyExemptionJustificationByDispatch.cannot.modify.penaltyExemptionDispatchDate");
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getPenaltyExemptionJustificationType().getQualifiedName(), Bundle.ENUMERATION);
        String penaltyExemptionDate =
                getPenaltyExemptionDispatchDate() != null ? getPenaltyExemptionDispatchDate().toString("dd/MM/yyyy") : "-";
        labelFormatter.appendLabel(" (").appendLabel("label.in", Bundle.APPLICATION).appendLabel(" ")
                .appendLabel(penaltyExemptionDate).appendLabel(")");

        return labelFormatter;
    }

}
