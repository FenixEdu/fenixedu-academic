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
package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.DateTime;

public class DepositAmountBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private EntryType entryType;

    private Event event;

    private Money amount;

    private DateTime whenRegistered;

    private String reason;

    public DepositAmountBean(final Event event) {
        setEvent(event);
        setWhenRegistered(new DateTime());
    }

    public Event getEvent() {
        return this.event;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public DateTime getWhenRegistered() {
        return whenRegistered;
    }

    public void setWhenRegistered(DateTime whenRegistered) {
        this.whenRegistered = whenRegistered;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

}
