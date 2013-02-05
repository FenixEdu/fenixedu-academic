package net.sourceforge.fenixedu.domain.exceptions;

/**
 * This exception is used when a certain entity name is duplicated acoording to
 * the domain logic in question.
 * 
 * @author cfgi
 */
public class DuplicatedNameException extends DomainException {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    public DuplicatedNameException(String key) {
        super(key);
    }

    public DuplicatedNameException(String key, String[] args) {
        super(key, args);
    }

}
