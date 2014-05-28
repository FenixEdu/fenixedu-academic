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
package net.sourceforge.fenixedu.domain.accounting.events.penaltyExemptionJustifications;

import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemption;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

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
                    new LabelFormatter(justificationType.getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES));
        }

    }

    @Override
    public void setPenaltyExemptionDispatchDate(YearMonthDay penaltyExemptionDispatchDate) {
        throw new DomainException(
                "error.net.sourceforge.fenixedu.domain.accounting.events.penaltyExemptionJustifications.PenaltyExemptionJustificationByDispatch.cannot.modify.penaltyExemptionDispatchDate");
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getPenaltyExemptionJustificationType().getQualifiedName(),
                LabelFormatter.ENUMERATION_RESOURCES);
        String penaltyExemptionDate =
                getPenaltyExemptionDispatchDate() != null ? getPenaltyExemptionDispatchDate().toString(
                        DateFormatUtil.DEFAULT_DATE_FORMAT) : "-";
        labelFormatter.appendLabel(" (").appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ")
                .appendLabel(penaltyExemptionDate).appendLabel(")");

        return labelFormatter;
    }

    @Deprecated
    public boolean hasPenaltyExemptionDispatchDate() {
        return getPenaltyExemptionDispatchDate() != null;
    }

}
