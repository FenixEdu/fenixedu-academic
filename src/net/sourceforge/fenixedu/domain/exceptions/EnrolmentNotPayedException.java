package net.sourceforge.fenixedu.domain.exceptions;

import net.sourceforge.fenixedu.domain.Enrolment;

public class EnrolmentNotPayedException extends DomainException {

    private final Enrolment enrolment;

    public EnrolmentNotPayedException(final String key, final Enrolment enrolment) {
	super(key, (String[]) null);
	this.enrolment = enrolment;
    }

    public Enrolment getEnrolment() {
	return this.enrolment;
    }

}
