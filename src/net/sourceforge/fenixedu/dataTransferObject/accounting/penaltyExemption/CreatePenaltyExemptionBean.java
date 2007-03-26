package net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;

import org.joda.time.YearMonthDay;

public abstract class CreatePenaltyExemptionBean implements Serializable {

    private DomainReference<Event> event;

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
	return (this.event != null) ? this.event.getObject() : null;
    }

    public void setEvent(Event event) {
	this.event = (event != null) ? new DomainReference<Event>(event) : null;
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
