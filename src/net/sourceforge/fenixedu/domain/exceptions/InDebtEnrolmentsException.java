package net.sourceforge.fenixedu.domain.exceptions;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Enrolment;

public class InDebtEnrolmentsException extends DomainException {
    
    private final Collection<Enrolment> enrolments;
    
    public InDebtEnrolmentsException(final String key, final Collection<Enrolment> enrolments) {
	super(key, (String[])null);
	this.enrolments = enrolments;
    }
    
    public Collection<Enrolment> getEnrolments() {
	return enrolments;
    }

}
