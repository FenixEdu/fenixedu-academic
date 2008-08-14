package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class CancelEventBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5181800965583788267L;

    private DomainReference<Event> event;

    private DomainReference<Employee> employee;

    private String justification;

    public CancelEventBean(final Event event, final Employee employee) {
	setEvent(event);
	setEmployee(employee);

    }

    public Event getEvent() {
	return (this.event != null) ? this.event.getObject() : null;
    }

    public void setEvent(Event event) {
	this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }

    public Employee getEmployee() {
	return (this.employee != null) ? this.employee.getObject() : null;

    }

    public void setEmployee(Employee employee) {
	this.employee = (employee != null) ? new DomainReference<Employee>(employee) : null;
    }

    public String getJustification() {
	return justification;
    }

    public void setJustification(String justification) {
	this.justification = justification;
    }
}
