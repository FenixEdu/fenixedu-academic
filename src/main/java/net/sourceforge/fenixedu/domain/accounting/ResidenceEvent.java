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
package net.sourceforge.fenixedu.domain.accounting;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.SibsTransactionDetailDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.predicates.EventsPredicates;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

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

        labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
        labelFormatter.appendLabel(" - ");
        labelFormatter.appendLabel(getResidenceMonth().getMonth().getName(), "enum");
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
        check(this, EventsPredicates.MANAGER_OR_RESIDENCE_UNIT_EMPLOYEE);
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

    @Deprecated
    public boolean hasRoom() {
        return getRoom() != null;
    }

    @Deprecated
    public boolean hasResidenceMonth() {
        return getResidenceMonth() != null;
    }

    @Deprecated
    public boolean hasRoomValue() {
        return getRoomValue() != null;
    }

}
