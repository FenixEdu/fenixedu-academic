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
package net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.ServiceAgreementTemplate;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR extends
        EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR_Base {

    private EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR() {
        super();
    }

    public EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount, Money amountPerUnit, Money amountPerPage,
            Money maximumAmount) {
        this();

        init(EntryType.ENROLMENT_CERTIFICATE_REQUEST_FEE, EventType.ENROLMENT_CERTIFICATE_REQUEST, startDate, endDate,
                serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

    @Override
    public Money getAmountForUnits(Integer numberOfUnits) {
        if (numberOfUnits <= 1) {
            return Money.ZERO;
        }

        Money totalAmountOfUnits = getAmountPerUnit().multiply(new BigDecimal(numberOfUnits - 1));

        if (this.getMaximumAmount().greaterThan(Money.ZERO)) {
            if (totalAmountOfUnits.greaterThan(this.getMaximumAmount())) {
                totalAmountOfUnits = this.getMaximumAmount();
            }
        }

        return totalAmountOfUnits;
    }

    @Override
    public EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR edit(final Money baseAmount, final Money amountPerUnit,
            final Money amountPerPage, final Money maximumAmount) {
        deactivate();
        return new EnrolmentCertificateRequestWithCeilingInTotalAmountForUnitsPR(new DateTime().minus(1000), null,
                getServiceAgreementTemplate(), baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

}
