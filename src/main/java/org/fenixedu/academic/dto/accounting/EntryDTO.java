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
package org.fenixedu.academic.dto.accounting;

import java.io.Serializable;
import java.util.Comparator;

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;
import org.joda.time.LocalDate;

public class EntryDTO implements Serializable {

    protected boolean isForPenalty;

    private boolean selected;

    private EntryType entryType;

    private Event event;

    private Money totalAmount;

    private Money payedAmount;

    private Money amountToPay;

    private LabelFormatter description;

    private Money debtAmount;

    private LocalDate dueDate;

    public EntryDTO(){
    }

    public EntryDTO(EntryType entryType, Event event, Money totalAmount, Money payedAmount, Money amountToPay,
            LabelFormatter description, Money debtAmount) {
        this(entryType, event, amountToPay);
        setTotalAmount(totalAmount);
        setPayedAmount(payedAmount);
        setDescription(description);
        setDebtAmount(debtAmount);
    }

    public EntryDTO(EntryType entryType, Event event, Money amountToPay) {
        setForPenalty(false);
        setEntryType(entryType);
        setEvent(event);
        setAmountToPay(amountToPay);
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Money getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(Money payedAmount) {
        this.payedAmount = payedAmount;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Money getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Money amountToPay) {
        this.amountToPay = amountToPay;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LabelFormatter getDescription() {
        return description;
    }

    public void setDescription(LabelFormatter description) {
        this.description = description;
    }

    public void setDebtAmount(Money debtAmount) {
        this.debtAmount = debtAmount;
    }

    public Money getDebtAmount() {
        return this.debtAmount;
    }

    public void setForPenalty(boolean forPenalty) {
        isForPenalty = forPenalty;
    }
    
    public boolean isForPenalty() {
        return isForPenalty;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntryDTO)) {
            return false;
        }

        EntryDTO entryDTO = (EntryDTO) o;

        if (isForPenalty() != entryDTO.isForPenalty()) {
            return false;
        }
        if (getEntryType() != entryDTO.getEntryType()) {
            return false;
        }
        if (getEvent() != null ? !getEvent().equals(entryDTO.getEvent()) : entryDTO.getEvent() != null) {
            return false;
        }
        if (getTotalAmount() != null ? !getTotalAmount().equals(entryDTO.getTotalAmount()) : entryDTO.getTotalAmount() != null) {
            return false;
        }
        if (getPayedAmount() != null ? !getPayedAmount().equals(entryDTO.getPayedAmount()) : entryDTO.getPayedAmount() != null) {
            return false;
        }
        if (getAmountToPay() != null ? !getAmountToPay().equals(entryDTO.getAmountToPay()) : entryDTO.getAmountToPay() != null) {
            return false;
        }
        if (getDescription() != null ? !getDescription().toString().equals(entryDTO.getDescription().toString()) : entryDTO.getDescription() != null) {
            return false;
        }
        if (getDueDate() != null ? !getDueDate().equals(entryDTO.getDueDate()) : entryDTO.getDueDate() != null) {
            return false;
        }

        return getDebtAmount() != null ? getDebtAmount().equals(entryDTO.getDebtAmount()) : entryDTO.getDebtAmount() == null;
    }

    @Override
    public int hashCode() {
        int result = (isForPenalty() ? 1 : 0);
        result = 31 * result + (getEntryType() != null ? getEntryType().hashCode() : 0);
        result = 31 * result + (getEvent() != null ? getEvent().hashCode() : 0);
        result = 31 * result + (getTotalAmount() != null ? getTotalAmount().hashCode() : 0);
        result = 31 * result + (getPayedAmount() != null ? getPayedAmount().hashCode() : 0);
        result = 31 * result + (getAmountToPay() != null ? getAmountToPay().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getDebtAmount() != null ? getDebtAmount().hashCode() : 0);
        result = 31 * result + (getDueDate() != null ? getDueDate().hashCode() : 0);
        return result;
    }

}
