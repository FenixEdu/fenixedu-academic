package net.sourceforge.fenixedu.domain.functionalities.exceptions;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * This exception is thrown when some path changes in the functionaties module
 * and two functionalities end up with the same public path, thus generating a
 * conflict. The change can be either a change to the path, to the path
 * relativity or to a module prefix.
 * 
 * @see Functionality#setPath(String)
 * @see Functionality#setRelative(Boolean)
 * @see net.sourceforge.fenixedu.domain.functionalities.Module#setPrefix(String)
 * 
 * @author cfgi
 */
public class MatchPathConflictException extends DomainException {

    /**
     * Serialization id.
     */
    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "functionalities.functionality.matchPath.conflict";

    public MatchPathConflictException(String path) {
        super(MESSAGE, path);
    }

}
