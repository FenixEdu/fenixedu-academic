package net.sourceforge.fenixedu.domain.residence;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResidenceManagementUnit;
import net.sourceforge.fenixedu.util.Month;

public class ResidenceMonth extends ResidenceMonth_Base {

    protected ResidenceMonth() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ResidenceMonth(Month month, ResidenceYear residenceYear) {
	this();
	setMonth(month);
	setYear(residenceYear);
    }

    public ResidenceManagementUnit getManagementUnit() {
	return getYear().getUnit();
    }

    public boolean isEventPresent(Person person) {
	for (ResidenceEvent event : getEvents()) {
	    if (event.getPerson() == person) {
		return true;
	    }
	}
	return false;

    }

}
