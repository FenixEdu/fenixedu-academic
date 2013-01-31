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
