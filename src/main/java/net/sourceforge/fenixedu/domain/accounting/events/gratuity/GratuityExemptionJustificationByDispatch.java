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
package net.sourceforge.fenixedu.domain.accounting.events.gratuity;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

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
                    new LabelFormatter(justificationType.getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES));
        }

    }

    @Override
    public void setGratuityExemptionDispatchDate(YearMonthDay dispatchDate) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityExemptionJustificationByDispatch.cannot.modify.dispatchDate");
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getGratuityExemptionJustificationType().getQualifiedName(),
                LabelFormatter.ENUMERATION_RESOURCES);
        String gratuityExemptionDate =
                getGratuityExemptionDispatchDate() != null ? getGratuityExemptionDispatchDate().toString(
                        DateFormatUtil.DEFAULT_DATE_FORMAT) : "-";
        labelFormatter.appendLabel(" (").appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ")
                .appendLabel(gratuityExemptionDate).appendLabel(")");

        return labelFormatter;
    }

    @Deprecated
    public boolean hasGratuityExemptionDispatchDate() {
        return getGratuityExemptionDispatchDate() != null;
    }

}
