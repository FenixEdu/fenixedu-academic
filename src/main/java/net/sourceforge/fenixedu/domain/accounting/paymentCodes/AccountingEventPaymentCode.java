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

import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.events.insurance.InsuranceEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class AccountingEventPaymentCode extends AccountingEventPaymentCode_Base {

    private static final Logger logger = LoggerFactory.getLogger(AccountingEventPaymentCode.class);

    static {
        getRelationPaymentCodeAccountingEvent().addListener(new RelationAdapter<AccountingEventPaymentCode, Event>() {
            @Override
            public void beforeAdd(AccountingEventPaymentCode accountingEventPaymentCode, Event event) {
                if (event instanceof InsuranceEvent) {
                    if (event.getAllPaymentCodes() != null) {
                        throw new DomainException(
                                "error.accounting.paymentCodes.AccountingEventPaymentCode.InsuranceEvent.already.has.payment.code.associated");
                    }
                }
            }
        });
    }

    protected AccountingEventPaymentCode() {
        super();
    }

    protected AccountingEventPaymentCode(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Event event, final Money minAmount, final Money maxAmount, final Person person) {
        this();
        init(paymentCodeType, startDate, endDate, event, minAmount, maxAmount, person);
    }

    public static AccountingEventPaymentCode create(final PaymentCodeType paymentCodeType, final YearMonthDay startDate,
            final YearMonthDay endDate, final Event event, final Money minAmount, final Money maxAmount, final Person person) {
        return PaymentCode.canGenerateNewCode(AccountingEventPaymentCode.class, paymentCodeType, person) ? new AccountingEventPaymentCode(
                paymentCodeType, startDate, endDate, event, minAmount, maxAmount, person) : findAndReuseExistingCode(
                paymentCodeType, startDate, endDate, event, minAmount, maxAmount, person);
    }

    protected static AccountingEventPaymentCode findAndReuseExistingCode(final PaymentCodeType paymentCodeType,
            final YearMonthDay startDate, final YearMonthDay endDate, final Event event, final Money minAmount,
            final Money maxAmount, final Person person) {
        for (PaymentCode code : person.getPaymentCodesBy(paymentCodeType)) {
            if (code.isAvailableForReuse() && getPaymentCodeGenerator(paymentCodeType).isCodeMadeByThisFactory(code)) {
                AccountingEventPaymentCode accountingEventPaymentCode = ((AccountingEventPaymentCode) code);
                accountingEventPaymentCode.reuse(startDate, endDate, minAmount, maxAmount, event);
                return accountingEventPaymentCode;
            }
        }
        return null;
    }

    protected void init(final PaymentCodeType paymentCodeType, YearMonthDay startDate, YearMonthDay endDate, Event event,
            Money minAmount, Money maxAmount, Person person) {
        super.init(paymentCodeType, startDate, endDate, minAmount, maxAmount, person);
        checkParameters(event, person);
        super.setAccountingEvent(event);
    }

    protected void checkParameters(Event event, final Person person) {
        if (person == null || person.getStudent() == null) {
            throw new DomainException("error.accounting.paymentCodes.AccountingEventPaymentCode.student.cannot.be.null");
        }
    }

    @Override
    public void setAccountingEvent(Event accountingEvent) {
        if (this.getAccountingEvent() != null || !this.isNew()) {
            throw new DomainException("error.accounting.paymentCodes.AccountingEventPaymentCode.cannot.modify.accountingEvent");
        }

        _setAccountingEvent(accountingEvent);
    }

    protected void _setAccountingEvent(Event accountingEvent) {
        super.setAccountingEvent(accountingEvent);
    }

    public void reuse(final YearMonthDay startDate, final YearMonthDay endDate, final Money minAmount, final Money maxAmount,
            final Event event) {

        reuseCode();
        update(startDate, endDate, minAmount, maxAmount);
        super.setAccountingEvent(event);
    }

    @Override
    protected void internalProcess(Person person, Money amount, DateTime whenRegistered, String sibsTransactionId, String comments) {
        final Event event = getAccountingEvent();
        if (event.isCancelled()) {
            logger.warn("############################ PROCESSING CODE FOR CANCELLED EVENT ###############################");
            logger.warn("Event " + event.getExternalId() + " for person " + event.getPerson().getExternalId() + " is cancelled");
            logger.warn("Code Number: " + getCode());
            logger.warn("################################################################################################");
        }

        event.process(person.getUser(), this, amount, new SibsTransactionDetailDTO(whenRegistered, sibsTransactionId, getCode(),
                comments));
    }

    @Override
    public void delete() {

        super.setAccountingEvent(null);

        super.delete();
    }

    @Override
    public void setPerson(Person student) {
        throw new DomainException("error.net.sourceforge.fenixedu.domain.accounting.PaymentCode.cannot.modify.person");
    }

    protected void _setPerson(Person person) {
        super.setPerson(person);
    }

    @Override
    public boolean isAccountingEventPaymentCode() {
        return true;
    }

    @Deprecated
    public boolean hasAccountingEvent() {
        return getAccountingEvent() != null;
    }

}
