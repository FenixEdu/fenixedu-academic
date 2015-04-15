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
package org.fenixedu.academic.domain.accounting;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.ResidenceManagementUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.residence.ResidenceMonth;
import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.SibsTransactionDetailDTO;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class ResidenceEvent extends ResidenceEvent_Base {

    protected ResidenceEvent() {
        super();
    }

    public ResidenceEvent(ResidenceMonth month, Person person, Money roomValue, String room) {
        init(EventType.RESIDENCE_PAYMENT, person, month, roomValue, room);
    }

    protected void init(EventType eventType, Person person, ResidenceMonth month, Money roomValue, String room) {
        super.init(eventType, person);
        if (month == null) {
            throw new DomainException("error.accounting.events.ResidenceEvent.ResidenceMonth.cannot.be.null");
        }
        setResidenceMonth(month);
        setRoomValue(roomValue);
        setRoom(room);
    }

    @Override
    public LabelFormatter getDescription() {
        return getDescriptionForEntryType(EntryType.RESIDENCE_FEE);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();

        labelFormatter.appendLabel(entryType.name(), Bundle.ENUMERATION);
        labelFormatter.appendLabel(" - ");
        labelFormatter.appendLabel(getResidenceMonth().getMonth().getName(), Bundle.ENUMERATION);
        labelFormatter.appendLabel("-");
        labelFormatter.appendLabel(getResidenceMonth().getYear().getYear().toString());
        return labelFormatter;
    }

    @Override
    protected Account getFromAccount() {
        return getPerson().getAccountBy(AccountType.EXTERNAL);
    }

    @Override
    public PostingRule getPostingRule() {
        return getManagementUnit().getUnitServiceAgreementTemplate().findPostingRuleBy(getEventType(), getWhenOccured(), null);
    }

    @Override
    public Account getToAccount() {
        return getManagementUnit().getAccountBy(AccountType.INTERNAL);
    }

    public ResidenceManagementUnit getManagementUnit() {
        return getResidenceMonth().getManagementUnit();
    }

    public DateTime getPaymentStartDate() {
        return getResidenceMonth().getPaymentStartDate();
    }

    public DateTime getPaymentLimitDate() {
        return getResidenceMonth().getPaymentLimitDateTime();
    }

    @Override
    public void cancel(Person responsible) {
        super.cancel(responsible);
    }

    public DateTime getPaymentDate() {
        return getNonAdjustingTransactions().isEmpty() ? null : getNonAdjustingTransactions().iterator().next()
                .getTransactionDetail().getWhenRegistered();
    }

    public PaymentMode getPaymentMode() {
        return getNonAdjustingTransactions().isEmpty() ? null : getNonAdjustingTransactions().iterator().next()
                .getTransactionDetail().getPaymentMode();
    }

    @Override
    public Money getAmountToPay() {
        return calculateAmountToPay(new DateTime());
    }

    @Override
    protected List<AccountingEventPaymentCode> createPaymentCodes() {
        final EntryDTO entryDTO = calculateEntries(new DateTime()).iterator().next();

        return Collections.singletonList(AccountingEventPaymentCode.create(PaymentCodeType.RESIDENCE_FEE, new YearMonthDay(),
                getPaymentLimitDate().toYearMonthDay(), this, entryDTO.getAmountToPay(), entryDTO.getAmountToPay(), getPerson()));
    }

    @Override
    protected List<AccountingEventPaymentCode> updatePaymentCodes() {
        final EntryDTO entryDTO = calculateEntries(new DateTime()).iterator().next();
        getNonProcessedPaymentCodes()
                .iterator()
                .next()
                .update(new YearMonthDay(), getPaymentLimitDate().toYearMonthDay(), entryDTO.getAmountToPay(),
                        entryDTO.getAmountToPay());

        return getNonProcessedPaymentCodes();
    }

    @Override
    protected Set<Entry> internalProcess(User responsibleUser, AccountingEventPaymentCode paymentCode, Money amountToPay,
            SibsTransactionDetailDTO transactionDetail) {
        return internalProcess(responsibleUser,
                Collections.singletonList(new EntryDTO(EntryType.RESIDENCE_FEE, this, amountToPay)), transactionDetail);
    }

    public boolean isFor(int year) {
        return getResidenceMonth().isFor(year);
    }

    @Override
    public boolean isResidenceEvent() {
        return true;
    }

    @Override
    public Unit getOwnerUnit() {
        return getManagementUnit();
    }

}
