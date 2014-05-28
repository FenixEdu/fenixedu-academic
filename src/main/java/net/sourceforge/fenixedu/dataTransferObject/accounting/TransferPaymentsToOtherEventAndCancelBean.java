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

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class TransferPaymentsToOtherEventAndCancelBean implements Serializable {
    private static final long serialVersionUID = -2180918685540833101L;

    private Event sourceEvent;

    private Event targetEvent;

    private Person responsible;

    private String cancelJustification;

    public TransferPaymentsToOtherEventAndCancelBean(final Event sourceEvent, final Person responsible) {
        setSourceEvent(sourceEvent);
        setResponsible(responsible);
    }

    public Event getTargetEvent() {
        return this.targetEvent;
    }

    public void setTargetEvent(Event event) {
        this.targetEvent = event;
    }

    public Event getSourceEvent() {
        return this.sourceEvent;
    }

    public void setSourceEvent(Event event) {
        this.sourceEvent = event;
    }

    public Person getResponsible() {
        return responsible;
    }

    public void setResponsible(Person responsible) {
        this.responsible = responsible;
    }

    public String getCancelJustification() {
        return cancelJustification;
    }

    public void setCancelJustification(String cancelJustification) {
        this.cancelJustification = cancelJustification;
    }

}
