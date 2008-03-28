package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.StudentReingressionRequest;
import net.sourceforge.fenixedu.util.Money;

public class PastStudentReingressionRequestEvent extends PastStudentReingressionRequestEvent_Base implements IPastRequestEvent {

    protected PastStudentReingressionRequestEvent() {
	super();
    }

    public PastStudentReingressionRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
	    final StudentReingressionRequest request) {
	this();
	super.init(administrativeOffice, EventType.PAST_STUDENT_REINGRESSION_REQUEST, person, request);
    }

    @Override
    public void setPastAmount(Money pastAmount) {
	throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

}
