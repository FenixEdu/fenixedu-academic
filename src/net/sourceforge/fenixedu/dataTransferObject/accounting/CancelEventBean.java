package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class CancelEventBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5181800965583788267L;

    private Event event;

    private Person responsible;

    private String justification;

    public CancelEventBean(final Event event, final Person responsible) {
	setEvent(event);
	setResponsible(responsible);
    }

    public Event getEvent() {
	return this.event;
    }

    public void setEvent(Event event) {
	this.event = event;
    }

    public Person getResponsible() {
	return responsible;
    }

    public void setResponsible(Person responsible) {
	this.responsible = responsible;
    }

    public String getJustification() {
	return justification;
    }

    public void setJustification(String justification) {
	this.justification = justification;
    }
}
