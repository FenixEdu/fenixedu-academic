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

public class ApprovementCertificateRequestPR extends ApprovementCertificateRequestPR_Base {

    private ApprovementCertificateRequestPR() {
        super();
    }

    public ApprovementCertificateRequestPR(DateTime startDate, DateTime endDate,
            ServiceAgreementTemplate serviceAgreementTemplate, Money baseAmount, Money amountPerUnit, Money amountPerPage,
            Money maximumAmount) {
        init(EntryType.APPROVEMENT_CERTIFICATE_REQUEST_FEE, EventType.APPROVEMENT_CERTIFICATE_REQUEST, startDate, endDate,
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
    public ApprovementCertificateRequestPR edit(Money baseAmount, Money amountPerUnit, Money amountPerPage, Money maximumAmount) {

        deactivate();

        return new ApprovementCertificateRequestPR(new DateTime().minus(1000), null, getServiceAgreementTemplate(), baseAmount,
                amountPerUnit, amountPerPage, maximumAmount);
    }

}
