package net.sourceforge.fenixedu.domain.exceptions;

public class DomainException extends RuntimeException {

    private final String key;
    private final String[] args;

    public DomainException(final String key, final String ... args) {
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
