package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class TransferPaymentsToOtherEventAndCancelBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2180918685540833101L;

    private Event sourceEvent;

    private Event targetEvent;

    private Employee employee;

    private String cancelJustification;

    public TransferPaymentsToOtherEventAndCancelBean(final Event sourceEvent, final Employee employee) {
	setSourceEvent(sourceEvent);
	setEmployee(employee);
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

    public Employee getEmployee() {
	return this.employee;
    }

    public void setEmployee(Employee employee) {
	this.employee = employee;
    }

    public String getCancelJustification() {
	return cancelJustification;
    }

    public void setCancelJustification(String cancelJustification) {
	this.cancelJustification = cancelJustification;
    }

}
