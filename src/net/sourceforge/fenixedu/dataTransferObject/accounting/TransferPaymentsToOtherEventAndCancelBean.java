package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class TransferPaymentsToOtherEventAndCancelBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2180918685540833101L;

    private DomainReference<Event> sourceEvent;

    private DomainReference<Event> targetEvent;

    private DomainReference<Employee> employee;

    private String cancelJustification;

    public TransferPaymentsToOtherEventAndCancelBean(final Event sourceEvent, final Employee employee) {
	setSourceEvent(sourceEvent);
	setEmployee(employee);
    }

    public Event getTargetEvent() {
	return (this.targetEvent != null) ? this.targetEvent.getObject() : null;
    }

    public void setTargetEvent(Event event) {
	this.targetEvent = (event != null) ? new DomainReference<Event>(event) : null;
    }

    public Event getSourceEvent() {
	return (this.sourceEvent != null) ? this.sourceEvent.getObject() : null;
    }

    public void setSourceEvent(Event event) {
	this.sourceEvent = (event != null) ? new DomainReference<Event>(event) : null;
    }

    public Employee getEmployee() {
	return (this.employee != null) ? this.employee.getObject() : null;
    }

    public void setEmployee(Employee employee) {
	this.employee = (employee != null) ? new DomainReference<Employee>(employee) : null;
    }

    public String getCancelJustification() {
	return cancelJustification;
    }

    public void setCancelJustification(String cancelJustification) {
	this.cancelJustification = cancelJustification;
    }

}
