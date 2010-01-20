package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestJustificationType;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.LocalDate;

public class AcademicServiceRequestExemptionBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private AcademicServiceRequestEvent event;

    private AcademicServiceRequestJustificationType justificationType;

    private Money value;

    private LocalDate dispatchDate;

    private String reason;

    public AcademicServiceRequestExemptionBean(final AcademicServiceRequestEvent event) {
	setEvent(event);
    }

    public AcademicServiceRequestEvent getEvent() {
	return this.event;
    }

    public void setEvent(AcademicServiceRequestEvent event) {
	this.event = event;
    }

    public AcademicServiceRequestJustificationType getJustificationType() {
	return justificationType;
    }

    public void setJustificationType(AcademicServiceRequestJustificationType justificationType) {
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
