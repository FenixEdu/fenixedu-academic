/**
 * 
 */
package net.sourceforge.fenixedu.injectionCode;

import net.sourceforge.fenixedu.domain.Person;

public class IllegalDataAccessException extends RuntimeException {
    private static final long serialVersionUID = 2264135195805915798L;

    public IllegalDataAccessException() {
	super();
    }

    public IllegalDataAccessException(String msg) {
	super(msg);
    }

    public IllegalDataAccessException(String msg, Person person) {
	super(msg);
    }
}