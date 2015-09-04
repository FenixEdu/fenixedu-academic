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

import org.fenixedu.academic.domain.GratuitySituation;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.transactions.PaymentType;
import org.fenixedu.academic.util.Money;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class GratuitySituationPaymentCode extends GratuitySituationPaymentCode_Base {

    protected GratuitySituationPaymentCode() {
        super();
    }

    private GratuitySituationPaymentCode(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Money minAmount, final Money maxAmount, final Student student,
            final GratuitySituation gratuitySituation) {
        this();
        init(paymentCodeType, startDate, endDate, minAmount, maxAmount, student, gratuitySituation);
    }

    private void init(PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate, Money minAmount,
            Money maxAmount, Student student, GratuitySituation gratuitySituation) {

        super.init(paymentCodeType, startDate, endDate, minAmount, maxAmount, student.getPerson());

        checkParameters(gratuitySituation, student);
        super.setGratuitySituation(gratuitySituation);

    }

    private void checkParameters(GratuitySituation gratuitySituation, final Student student) {
        if (gratuitySituation == null) {
            throw new DomainException(
                    "error.accounting.paymentCodes.GratuitySituationPaymentCode.gratuitySituation.cannot.be.null");
        }

        if (student == null) {
            throw new DomainException("error.accounting.paymentCodes.GratuitySituationPaymentCode.student.cannot.be.null");
        }
    }

    public static GratuitySituationPaymentCode create(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Money minAmount, final Money maxAmount, final Student student,
            final GratuitySituation gratuitySituation) {

        if (PaymentCode.canGenerateNewCode(GratuitySituationPaymentCode.class, paymentCodeType, student.getPerson())) {
            return new GratuitySituationPaymentCode(paymentCodeType, startDate, endDate, minAmount, maxAmount, student,
                    gratuitySituation);
        }

        throw new DomainException("error.accounting.paymentCodes.MasterDegreeInsurancePaymentCode.could.not.generate.new.code");
    }

    @Override
    public void setGratuitySituation(GratuitySituation gratuitySituation) {
        throw new DomainException("error.accounting.paymentCodes.GratuitySituationPaymentCode.cannot.modify.gratuitySituation");
    }

    @Override
    protected void internalProcess(Person responsiblePerson, Money amount, DateTime whenRegistered, String sibsTransactionId,
            String comments) {
        getGratuitySituation().processAmount(responsiblePerson, amount, whenRegistered, PaymentType.SIBS);
    }

    @Override
    public void delete() {
        super.setGratuitySituation(null);

        super.delete();
    }
}
