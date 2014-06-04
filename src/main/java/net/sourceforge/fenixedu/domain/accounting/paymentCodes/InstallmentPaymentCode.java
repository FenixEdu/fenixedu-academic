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
package net.sourceforge.fenixedu.domain.accounting.paymentCodes;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.Money;

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
    public void setInstallment(Installment installment) {
        check(this, RolePredicates.MANAGER_PREDICATE);
        super.setInstallment(installment);
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

    @Deprecated
    public boolean hasInstallment() {
        return getInstallment() != null;
    }

}
