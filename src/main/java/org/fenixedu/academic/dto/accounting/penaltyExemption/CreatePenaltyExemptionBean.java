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
package net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;

import org.joda.time.YearMonthDay;

public abstract class CreatePenaltyExemptionBean implements Serializable {

    private Event event;

    private String reason;

    private PenaltyExemptionJustificationType justificationType;

    private YearMonthDay dispatchDate;

    protected CreatePenaltyExemptionBean(final Event event) {
        setEvent(event);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;

    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public PenaltyExemptionJustificationType getJustificationType() {
        return justificationType;
    }

    public void setJustificationType(PenaltyExemptionJustificationType exemptionType) {
        this.justificationType = exemptionType;
    }

    public YearMonthDay getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(YearMonthDay dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

}
