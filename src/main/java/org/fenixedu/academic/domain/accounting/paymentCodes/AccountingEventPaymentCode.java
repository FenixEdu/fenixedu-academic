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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.insurance.InsuranceEvent;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.accounting.SibsTransactionDetailDTO;
import org.fenixedu.academic.util.Money;
import org.fenixedu.academic.util.sibs.incomming.SibsIncommingPaymentFileDetailLine;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

import java.util.Optional;

@Deprecated
public class AccountingEventPaymentCode extends AccountingEventPaymentCode_Base implements IEventPaymentCode {

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


    @Override protected void internalProcess(Person person, SibsIncommingPaymentFileDetailLine sibsDetailLine) {
        final Event event = getAccountingEvent();
        if (event.isCancelled()) {
            logger.warn("############################ PROCESSING CODE FOR CANCELLED EVENT ###############################");
            logger.warn("Event " + event.getExternalId() + " for person " + event.getPerson().getExternalId() + " is cancelled");
            logger.warn("Code Number: " + getCode());
            logger.warn("################################################################################################");
        }

        event.process(person.getUser(), this,
                sibsDetailLine.getAmount(), new SibsTransactionDetailDTO(sibsDetailLine,sibsDetailLine.getWhenOccuredTransaction(),
                        sibsDetailLine.getSibsTransactionId(), getCode(), ""));
    }

    @Override
    public void delete() {

        super.setAccountingEvent(null);

        super.delete();
    }

    @Deprecated
    protected void _setPerson(Person person) {
        super.setPerson(person);
    }

    @Override
    public boolean isAccountingEventPaymentCode() {
        return true;
    }

    @Override public Optional<Event> getEvent() {
        return Optional.ofNullable(getAccountingEvent());
    }
}
