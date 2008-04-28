package net.sourceforge.fenixedu.caseHandling;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PreConditionNotValidException extends DomainException {

    public PreConditionNotValidException() {
	super();
    }
    
    public PreConditionNotValidException(final String key, final String... args) {
	super(key, args);
    }

    public PreConditionNotValidException(final String key, final Throwable cause, final String... args) {
	super(key, cause, args);
    }

}
