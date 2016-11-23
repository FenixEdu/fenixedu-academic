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
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.YearMonthDay;

@Deprecated
public class InstallmentPaymentCode extends InstallmentPaymentCode_Base {

    private InstallmentPaymentCode() {
        super();
    }

    private InstallmentPaymentCode(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Event event, final Installment installment, final Money minAmount,
            final Money maxAmount, final Student student) {
        this();
        init(paymentCodeType, startDate, endDate, event, installment, minAmount, maxAmount, student);
    }

    public static InstallmentPaymentCode create(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Event event, final Installment installment, final Money minAmount,
            final Money maxAmount, final Student student) {
        return PaymentCode.canGenerateNewCode(InstallmentPaymentCode.class, paymentCodeType, student.getPerson()) ? new InstallmentPaymentCode(
                paymentCodeType, startDate, endDate, event, installment, minAmount, maxAmount, student) : findAndReuseExistingCode(
                paymentCodeType, startDate, endDate, event, minAmount, maxAmount, student, installment);

    }

    protected static InstallmentPaymentCode findAndReuseExistingCode(final PaymentCodeType paymentCodeType,
            final YearMonthDay startDate, final YearMonthDay endDate, final Event event, final Money minAmount,
            final Money maxAmount, final Student student, final Installment installment) {
        for (PaymentCode code : student.getPerson().getPaymentCodesBy(paymentCodeType)) {
            if (code.isAvailableForReuse() && getPaymentCodeGenerator(paymentCodeType).isCodeMadeByThisFactory(code)) {
                InstallmentPaymentCode accountingEventPaymentCode = ((InstallmentPaymentCode) code);
                accountingEventPaymentCode.reuse(startDate, endDate, minAmount, maxAmount, event);
                return accountingEventPaymentCode;
            }
        }
        return null;
    }

    public void reuse(YearMonthDay startDate, YearMonthDay endDate, Money minAmount, Money maxAmount, Event event,
            Installment installment) {
        super.reuse(startDate, endDate, minAmount, maxAmount, event);
        super.setInstallment(installment);

    }

    protected void init(final PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate, final Event event,
            Installment installment, final Money minAmount, final Money maxAmount, final Student student) {
        super.init(paymentCodeType, startDate, endDate, event, minAmount, maxAmount, student.getPerson());
        checkParameters(installment, student);
        super.setInstallment(installment);

    }

    private void checkParameters(Installment installment, final Student student) {
        if (installment == null) {
            throw new DomainException("error.accounting.paymentCodes.InstallmentPaymentCode.installment.cannot.be.null");
        }

        if (student == null) {
            throw new DomainException("error.accounting.paymentCodes.InstallmentPaymentCode.student.cannot.be.null");
        }

    }

    @Override
    public void delete() {
        super.setInstallment(null);
        super.delete();
    }

    @Override
    public String getDescription() {
        if (getInstallment().getPaymentPlan().hasSingleInstallment()) {
            return BundleUtil.getString(Bundle.ENUMERATION, PaymentCodeType.TOTAL_GRATUITY.getQualifiedName());
        }

        return super.getDescription();

    }

    @Override
    public boolean isInstallmentPaymentCode() {
        return true;
    }

}
