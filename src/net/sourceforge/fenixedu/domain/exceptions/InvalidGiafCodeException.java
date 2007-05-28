package net.sourceforge.fenixedu.domain.exceptions;

public class InvalidGiafCodeException extends DomainException {
    public InvalidGiafCodeException(final String key, final String... args) {
	super(key, args);
    }
}
