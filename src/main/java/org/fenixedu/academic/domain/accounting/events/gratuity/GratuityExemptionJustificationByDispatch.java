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

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.exceptions.DomainExceptionWithLabelFormatter;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.joda.time.YearMonthDay;

public class GratuityExemptionJustificationByDispatch extends GratuityExemptionJustificationByDispatch_Base {

    protected GratuityExemptionJustificationByDispatch() {
        super();
    }

    public GratuityExemptionJustificationByDispatch(final GratuityExemption gratuityExemption,
            final GratuityExemptionJustificationType justificationType, final String reason, final YearMonthDay dispatchDate) {
        this();
        init(gratuityExemption, justificationType, reason, dispatchDate);
    }

    protected void init(GratuityExemption gratuityExemption, GratuityExemptionJustificationType justificationType, String reason,
            YearMonthDay dispatchDate) {
        checkParameters(justificationType, reason, dispatchDate);
        super.init(gratuityExemption, justificationType, reason);
        super.setGratuityExemptionDispatchDate(dispatchDate);
    }

    private void checkParameters(final GratuityExemptionJustificationType justificationType, final String reason,
            final YearMonthDay dispatchDate) {
        if (dispatchDate == null || StringUtils.isEmpty(reason)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.events.GratuityExemptionJustificationByDispatch.dispatchDate.and.reason.are.required",
                    new LabelFormatter(justificationType.getQualifiedName(), Bundle.ENUMERATION));
        }

    }

    @Override
    public void setGratuityExemptionDispatchDate(YearMonthDay dispatchDate) {
        throw new DomainException(
                "error.org.fenixedu.academic.domain.accounting.events.gratuity.GratuityExemptionJustificationByDispatch.cannot.modify.dispatchDate");
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getGratuityExemptionJustificationType().getQualifiedName(), Bundle.ENUMERATION);
        String gratuityExemptionDate =
                getGratuityExemptionDispatchDate() != null ? getGratuityExemptionDispatchDate().toString("dd/MM/yyyy") : "-";
        labelFormatter.appendLabel(" (").appendLabel("label.in", Bundle.APPLICATION).appendLabel(" ")
                .appendLabel(gratuityExemptionDate).appendLabel(")");

        return labelFormatter;
    }

}
