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
package org.fenixedu.academic.domain.accounting.postingRules.serviceRequests;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.ServiceAgreementTemplate;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;

public class DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR extends
        DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR_Base {

    private DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR() {
        super();
    }

    public DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR(final DateTime startDate, DateTime endDate,
            final ServiceAgreementTemplate serviceAgreementTemplate, final Money baseAmount, final Money amountPerUnit,
            final Money amountPerPage, final Money maximumAmount) {
        this();
        init(EntryType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST_FEE, EventType.DEGREE_FINALIZATION_CERTIFICATE_REQUEST, startDate,
                endDate, serviceAgreementTemplate, baseAmount, amountPerUnit, amountPerPage, maximumAmount);
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
    public DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR edit(Money baseAmount, Money amountPerUnit,
            Money amountPerPage, Money maximumAmount) {

        deactivate();

        return new DegreeFinalizationCertificateRequestWithCeilingInTotalAmountForUnitsPR(new DateTime().minus(1000), null,
                getServiceAgreementTemplate(), baseAmount, amountPerUnit, amountPerPage, maximumAmount);
    }

}
