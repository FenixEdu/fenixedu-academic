package net.sourceforge.fenixedu.domain.exceptions;

public class NotDeletableException extends DomainException {

    public NotDeletableException(String key, String[] args) {
        super(key, args);
    }

}
