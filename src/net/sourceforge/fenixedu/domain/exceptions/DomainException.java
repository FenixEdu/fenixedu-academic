package net.sourceforge.fenixedu.domain.exceptions;

public class DomainException extends RuntimeException {

    private final String key;

    private final String[] args;

    protected DomainException() {
	this(null, (String[]) null);
    }

    public DomainException(final String key, final String... args) {
	super(key); // setting the message attribute
	this.key = key;
	this.args = args;
    }

    public DomainException(final String key, final Throwable cause, final String... args) {
	super(key, cause);
	this.key = key;
	this.args = args;
    }

    public String getKey() {
	return key;
    }

    public String[] getArgs() {
	return args;
    }

}
