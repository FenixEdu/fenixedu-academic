package net.sourceforge.fenixedu.dataTransferObject.accounting.penaltyExemption;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.Event;
import net.sourceforge.fenixedu.domain.accounting.events.PenaltyExemptionJustificationType;

import org.joda.time.YearMonthDay;

public abstract class CreatePenaltyExemptionBean implements Serializable {

    private DomainReference<Event> event;

    private String comments;

    private PenaltyExemptionJustificationType justificationType;

    private YearMonthDay directiveCouncilDispatchDate;

    protected CreatePenaltyExemptionBean(final Event event) {
	setEvent(event);
    }

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;

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

    public YearMonthDay getDirectiveCouncilDispatchDate() {
	return directiveCouncilDispatchDate;
    }

    public void setDirectiveCouncilDispatchDate(YearMonthDay directiveCouncilDispatchDate) {
	this.directiveCouncilDispatchDate = directiveCouncilDispatchDate;
    }

}
