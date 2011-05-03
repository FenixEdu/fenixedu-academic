package net.sourceforge.fenixedu.domain.phd.exceptions;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PhdDomainOperationException extends DomainException {

    private static final long serialVersionUID = 1L;

    public PhdDomainOperationException() {
	super();
    }

    public PhdDomainOperationException(String key, String... args) {
	super(key, args);
    }

    public PhdDomainOperationException(String key, Throwable cause, String... args) {
	super(key, cause, args);
    }
}
