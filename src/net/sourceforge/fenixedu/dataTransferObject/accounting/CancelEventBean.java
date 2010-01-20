package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class CancelEventBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5181800965583788267L;

    private Event event;

    private Employee employee;

    private String justification;

    public CancelEventBean(final Event event, final Employee employee) {
	setEvent(event);
	setEmployee(employee);

    }

    public Event getEvent() {
	return this.event;
    }

    public void setEvent(Event event) {
	this.event = event;
    }

    public Employee getEmployee() {
	return this.employee;

    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public String getJustification() {
	return justification;
    }

    public void setJustification(String justification) {
	this.justification = justification;
    }
}
