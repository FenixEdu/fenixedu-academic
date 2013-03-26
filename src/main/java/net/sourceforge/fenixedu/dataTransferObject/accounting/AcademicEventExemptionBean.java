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
