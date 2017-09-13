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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;

public class IndividualCandidacyPaymentCode extends IndividualCandidacyPaymentCode_Base {

    protected IndividualCandidacyPaymentCode(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Money minAmount, final Money maxAmount) {
        super();
        init(paymentCodeType, startDate, endDate, minAmount, maxAmount);
    }

    public static IndividualCandidacyPaymentCode create(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Money minAmount, final Money maxAmount) {
        return PaymentCode.canGenerateNewCode(IndividualCandidacyPaymentCode.class, paymentCodeType, null) ? new IndividualCandidacyPaymentCode(
                paymentCodeType, startDate, endDate, minAmount, maxAmount) : findAndReuseExistingCode(paymentCodeType, startDate,
                endDate, minAmount, maxAmount);

    }

    public static IndividualCandidacyPaymentCode getAvailablePaymentCodeAndUse(final PaymentCodeType paymentCodeType,
            final YearMonthDay date, Event event, Person person) {
        Set<PaymentCode> individualCandidacyPaymentCodes = Bennu.getInstance().getPaymentCodesSet();

        for (PaymentCode paymentCode : individualCandidacyPaymentCodes) {

            if (!(paymentCode instanceof IndividualCandidacyPaymentCode)) {
                continue;
            }

            IndividualCandidacyPaymentCode individualCandidacyPaymentCode = (IndividualCandidacyPaymentCode) paymentCode;

            if (individualCandidacyPaymentCode.isAvailable(paymentCodeType, date)) {
                individualCandidacyPaymentCode.use(event, person);

                return individualCandidacyPaymentCode;
            }
        }

        return null;
    }

    public static List<IndividualCandidacyPaymentCode> getAvailablePaymentCodes(final PaymentCodeType paymentCodeType,
            final YearMonthDay date) {
        List<IndividualCandidacyPaymentCode> result = new ArrayList<IndividualCandidacyPaymentCode>();

        Set<PaymentCode> individualCandidacyPaymentCodes = Bennu.getInstance().getPaymentCodesSet();
        for (PaymentCode paymentCode : individualCandidacyPaymentCodes) {

            if (!(paymentCode instanceof IndividualCandidacyPaymentCode)) {
                continue;
            }

            IndividualCandidacyPaymentCode individualCandidacyPaymentCode = (IndividualCandidacyPaymentCode) paymentCode;

            if (individualCandidacyPaymentCode.isAvailable(paymentCodeType, date)) {
                result.add(individualCandidacyPaymentCode);
            }
        }

        return result;
    }

    protected void init(final PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate, Money minAmount,
            Money maxAmount) {
        super.init(paymentCodeType, startDate, endDate, minAmount, maxAmount, null);
    }

    protected Boolean isAvailable(final PaymentCodeType paymentCodeType, final YearMonthDay date) {
        return this.getType().equals(paymentCodeType) && !this.getStartDate().isAfter(date) && !this.getEndDate().isBefore(date)
                && this.getPerson() == null;
    }

    protected static IndividualCandidacyPaymentCode findAndReuseExistingCode(final PaymentCodeType paymentCodeType,
            final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount, final Money maxAmount) {

        IndividualCandidacyPaymentCode paymentCode = getAvailablePaymentCodeForReuse();
        paymentCode.reuse(startDate, endDate, minAmount, maxAmount, null);
        paymentCode.setType(paymentCodeType);
        paymentCode.setPerson(null);

        return paymentCode;
    }

    protected static IndividualCandidacyPaymentCode getAvailablePaymentCodeForReuse() {
        return (IndividualCandidacyPaymentCode) Bennu.getInstance().getPaymentCodesSet().stream()
                .filter((IndividualCandidacyPaymentCode.class)::isInstance).filter(PaymentCode::isAvailableForReuse)
                .sorted(PaymentCode.COMPARATOR_BY_END_DATE).findFirst()
                .orElse(null);
    }

    @Override
    protected void checkParameters(Event event, final Person person) {
        if (event == null) {
            throw new DomainException("error.accounting.paymentCodes.IndividualCandidacyPaymentCode.event.cannot.be.null");
        }

        if (person == null) {
            throw new DomainException("error.accounting.paymentCodes.IndividualCandidacyPaymentCode.person.cannot.be.null");
        }
    }

    protected void use(Event event, Person person) {
        checkParameters(event, person);

        this.setPerson(person);
        this.setAccountingEvent(event);
    }

    @Atomic
    public static List<IndividualCandidacyPaymentCode> createPaymentCodes(PaymentCodeType type, LocalDate beginDate,
            LocalDate endDate, Money minimum, Money maximum, Integer numberOfPaymentCodes) {

        List<IndividualCandidacyPaymentCode> result = new ArrayList<IndividualCandidacyPaymentCode>();

        for (int i = 0; i < numberOfPaymentCodes; i++) {

            result.add(IndividualCandidacyPaymentCode.create(type, beginDate.toDateTimeAtStartOfDay().toYearMonthDay(), endDate
                    .toDateTimeAtStartOfDay().toYearMonthDay(), minimum, maximum));
        }

        return result;

    }

}
