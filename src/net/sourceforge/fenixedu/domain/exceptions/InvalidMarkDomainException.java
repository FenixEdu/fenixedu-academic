package net.sourceforge.fenixedu.domain.exceptions;

public class InvalidMarkDomainException extends DomainException {
    public InvalidMarkDomainException(final String key, final String... args) {
	super(key, args);
    }

    public InvalidMarkDomainException(final String key, final Throwable cause, final String... args) {
	super(key, cause, args);
    }
}
