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
package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.util.List;

import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixPeriod;

public class FenixPayment {

    public static class PaymentEvent {
        String amount;
        String type;
        String description;
        String date;

        public PaymentEvent(String amount, String type, String description, String date) {
            super();
            this.amount = amount;
            this.type = type;
            this.description = description;
            this.date = date;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

    }

    public static class PendingEvent {
        String description;
        FenixPeriod paymentPeriod;
        String entity;
        String reference;
        String amount;

        public PendingEvent(String description, FenixPeriod paymentPeriod, String entity, String reference, String amount) {
            super();
            this.description = description;
            this.paymentPeriod = paymentPeriod;
            this.entity = entity;
            this.reference = reference;
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public FenixPeriod getPaymentPeriod() {
            return paymentPeriod;
        }

        public void setPaymentPeriod(FenixPeriod paymentPeriod) {
            this.paymentPeriod = paymentPeriod;
        }

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

    }

    private List<PaymentEvent> completed;
    private List<PendingEvent> pending;

    public FenixPayment(List<PaymentEvent> completed, List<PendingEvent> pending) {
        super();
        this.completed = completed;
        this.pending = pending;
    }

    public List<PaymentEvent> getCompleted() {
        return completed;
    }

    public void setCompleted(List<PaymentEvent> completed) {
        this.completed = completed;
    }

    public List<PendingEvent> getPending() {
        return pending;
    }

    public void setPending(List<PendingEvent> pending) {
        this.pending = pending;
    }

}
