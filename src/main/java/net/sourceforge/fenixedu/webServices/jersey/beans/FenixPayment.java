package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.util.List;

public class FenixPayment {

    public static class PaymentEvent {
        String amount;
        String name;
        String description;
        String date;

        public PaymentEvent(String amount, String name, String description, String date) {
            super();
            this.amount = amount;
            this.name = name;
            this.description = description;
            this.date = date;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

    public static class NotPayedEvent {
        String description;
        String startDate;
        String endDate;
        String entity;
        String reference;
        String amount;

        public NotPayedEvent(String description, String startDate, String endDate, String entity, String reference, String amount) {
            super();
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
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

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
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

    private List<PaymentEvent> payed;
    private List<NotPayedEvent> notPayed;

    public FenixPayment(List<PaymentEvent> payed, List<NotPayedEvent> notPayed) {
        super();
        this.payed = payed;
        this.notPayed = notPayed;
    }

    public List<PaymentEvent> getPayed() {
        return payed;
    }

    public void setPayed(List<PaymentEvent> payed) {
        this.payed = payed;
    }

    public List<NotPayedEvent> getNotPayed() {
        return notPayed;
    }

    public void setNotPayed(List<NotPayedEvent> notPayed) {
        this.notPayed = notPayed;
    }

}
