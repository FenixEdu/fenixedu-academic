package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.parser.GroupExpressionMarker;

/**
 * This exception is thrown by the parser. It is used to signal problems as soon
 * as they are detected an to add line information to other exceptions that
 * typically occur durint the expression evaluation but may be thrown in the 
 * compilation due to some optimizations.
 * 
 * @author cfgi
 */
public class GroupExpressionParserException extends GroupExpressionException {

    private static final long serialVersionUID = 1L;

    protected GroupExpressionParserException(String key, String... args) {
        super(key, args);
    }

    public GroupExpressionParserException(GroupExpressionMarker marker, String key, String... args) {
        this(key, args);
        
        setMarker(marker);
    }

    public GroupExpressionParserException(GroupExpressionMarker marker, Throwable cause, String key, String... args) {
        super(cause, key, args);
        
        setMarker(marker);
    }

    public GroupExpressionParserException(GroupExpressionMarker marker, GroupExpressionException cause) {
        super(cause, cause.getKey(), cause.getArgs());
        
        setResource(cause.isResource());
        setMarker(marker);
    }

    public void setMarker(GroupExpressionMarker marker) {
        setStartLine(marker.getStart().getLine());
        setStartColumn(marker.getStart().getColumn());
        setEndLine(marker.getEnd().getLine());
        setEndColumn(marker.getEnd().getColumn());
    }

}
