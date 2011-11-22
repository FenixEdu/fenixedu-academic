package net.sourceforge.fenixedu.domain;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class StrikeDay extends StrikeDay_Base {
    
    public StrikeDay() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public boolean matches(final LocalDate date) {
	return date.equals(getDate());
    }

    @Service
    public void delete() {
	removeRootDomainObject();
	deleteDomainObject();
    }
    
}
