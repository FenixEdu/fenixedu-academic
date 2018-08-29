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
package org.fenixedu.academic.domain.accounting.paymentCodes;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.util.Money;
import org.joda.time.YearMonthDay;

@Deprecated
public class InstallmentPaymentCode extends InstallmentPaymentCode_Base {

    private InstallmentPaymentCode() {
        super();
    }

    public void reuse(YearMonthDay startDate, YearMonthDay endDate, Money minAmount, Money maxAmount, Event event,
            Installment installment) {
        super.reuse(startDate, endDate, minAmount, maxAmount, event);
        super.setInstallment(installment);

    }

    @Override
    public void delete() {
        super.setInstallment(null);
        super.delete();
    }

    @Override
    public String getDescription() {
        final Installment installment = getInstallment();
        return installment.getDescription().toString();

    }

    @Override
    public boolean isInstallmentPaymentCode() {
        return true;
    }

}
