package net.sourceforge.fenixedu.domain.accounting;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class AcademicEvent extends AcademicEvent_Base {

    public AcademicEvent() {
	super();
    }

    protected void init(AdministrativeOffice administrativeOffice, EventType eventType, Person person) {
	init(eventType, person);
	super.setAdministrativeOffice(administrativeOffice);
    }

    @Override
    public void setAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	throw new DomainException("error.accounting.Event.cannot.modify.administrativeOffice");
    }

    @Override
    public boolean isPayableOnAdministrativeOffice(AdministrativeOffice administrativeOffice) {
	return (!hasAdministrativeOffice() || getAdministrativeOffice() == administrativeOffice);
    }

    @Override
    public void delete() {
	super.setAdministrativeOffice(null);
	super.delete();
    }

}
