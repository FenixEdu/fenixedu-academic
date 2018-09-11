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
package org.fenixedu.academic.dto.accounting.penaltyExemption;

import java.io.Serializable;

import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.events.EventExemptionJustificationType;
import org.joda.time.YearMonthDay;

public abstract class CreatePenaltyExemptionBean implements Serializable {

    private Event event;

    private String reason;

    private EventExemptionJustificationType justificationType;

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

    public EventExemptionJustificationType getJustificationType() {
        return justificationType;
    }

    public void setJustificationType(EventExemptionJustificationType exemptionType) {
        this.justificationType = exemptionType;
    }

    public YearMonthDay getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(YearMonthDay dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

}
