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

import net.sourceforge.fenixedu.domain.accounting.Exemption;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.DomainExceptionWithLabelFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;

public class AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch extends
        AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch_Base {

    protected AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch() {
        super();
    }

    public AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch(final Exemption exemption,
            final AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, final String reason,
            final YearMonthDay dispatchDate) {
        this();
        init(exemption, justificationType, reason, dispatchDate);

    }

    private void init(Exemption exemption, AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType,
            String reason, YearMonthDay dispatchDate) {
        checkParameters(exemption, justificationType, reason, dispatchDate);

        super.init(exemption, justificationType, reason);

        super.setDispatchDate(dispatchDate);

    }

    private void checkParameters(Exemption exemption,
            AdministrativeOfficeFeeAndInsuranceExemptionJustificationType justificationType, String reason,
            YearMonthDay dispatchDate) {

        if (!exemption.isForAdministrativeOfficeFee()) {
            throw new DomainException(
                    "error.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch.exemption.must.be.form.administrativeOfficeFee.exemption");
        }

        if (dispatchDate == null || StringUtils.isEmpty(reason)) {
            throw new DomainExceptionWithLabelFormatter(
                    "error.accounting.events.AdministrativeOfficeFeeAndInsuranceExemptionJustificationByDispatch.dispatchDate.and.reason.are.required",
                    new LabelFormatter(justificationType.getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES));
        }
    }

    @Override
    public LabelFormatter getDescription() {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(getJustificationType().getQualifiedName(), LabelFormatter.ENUMERATION_RESOURCES);
        String dispatchDate = getDispatchDate() != null ? getDispatchDate().toString(DateFormatUtil.DEFAULT_DATE_FORMAT) : "-";
        labelFormatter.appendLabel(" (").appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES).appendLabel(" ")
                .appendLabel(dispatchDate).appendLabel(")");

        return labelFormatter;
    }

}
