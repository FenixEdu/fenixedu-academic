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

import net.sourceforge.fenixedu.domain.accounting.AcademicEvent;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicEventJustificationType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.LocalDate;

public class AcademicEventExemptionBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private AcademicEvent event;

    private AcademicEventJustificationType justificationType;

    private Money value;

    private LocalDate dispatchDate;

    private String reason;

    public AcademicEventExemptionBean(final AcademicEvent event) {
        setEvent(event);
    }

    public AcademicEvent getEvent() {
        return event;
    }

    public void setEvent(AcademicEvent event) {
        this.event = event;
    }

    public AcademicEventJustificationType getJustificationType() {
        return justificationType;
    }

    public void setJustificationType(AcademicEventJustificationType justificationType) {
        this.justificationType = justificationType;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    public LocalDate getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(LocalDate dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
