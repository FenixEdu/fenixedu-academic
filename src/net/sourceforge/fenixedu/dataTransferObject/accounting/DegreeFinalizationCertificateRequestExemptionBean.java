package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.events.DegreeFinalizationCertificateRequestJustificationType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.DegreeFinalizationCertificateRequestEvent;
import net.sourceforge.fenixedu.util.Money;

public class DegreeFinalizationCertificateRequestExemptionBean implements Serializable {

    static private final long serialVersionUID = 1L;

    private DomainReference<DegreeFinalizationCertificateRequestEvent> event;

    private DegreeFinalizationCertificateRequestJustificationType justificationType;

    private Money value;

    private String reason;

    public DegreeFinalizationCertificateRequestExemptionBean(final DegreeFinalizationCertificateRequestEvent event) {
	setEvent(event);
    }

    public DegreeFinalizationCertificateRequestEvent getEvent() {
	return (this.event != null) ? this.event.getObject() : null;
    }

    public void setEvent(DegreeFinalizationCertificateRequestEvent event) {
	this.event = (event != null) ? new DomainReference<DegreeFinalizationCertificateRequestEvent>(event) : null;
    }

    public DegreeFinalizationCertificateRequestJustificationType getJustificationType() {
	return justificationType;
    }

    public void setJustificationType(DegreeFinalizationCertificateRequestJustificationType justificationType) {
	this.justificationType = justificationType;
    }

    public Money getValue() {
	return value;
    }

    public void setValue(Money value) {
	this.value = value;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

}
