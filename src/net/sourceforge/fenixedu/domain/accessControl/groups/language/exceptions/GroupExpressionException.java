package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import antlr.RecognitionException;

/**
 * This exception is the top level exceptions for all exceptions related
 * to a group expression. This can include problems in the compilation
 * of the expression or problems during the execution of the expression due
 * to it's dynamic nature.
 *
 * <p>
 * This exception allows access to the especific message about what is wrong
 * with the expression and in some cases it gives information about the line
 * and column were is problem occured. The line and column information can
 * even be given as a range, that is, the error is reported for a region of 
 * an expression.
 * 
 * @author cfgi
 */
public class GroupExpressionException extends DomainException {

    private static final long serialVersionUID = 1L;

    private int startLine   = -1;
    private int startColumn = -1;
    private int endLine     = -1;
    private int endColumn   = -1;
    
    private boolean resource = true;
    
    protected GroupExpressionException(Throwable cause, String message, String ... args) {
        super(message, cause, args);
    }
    
    public GroupExpressionException(Throwable cause) {
        this(cause, cause.getMessage());
        
        setResource(false);
    }

    protected GroupExpressionException(String message, String ... args) {
        this(null, message, args);
    }

    /**
     * 
     * @param e the parser exception to wrap
     */
    public GroupExpressionException(RecognitionException e) {
        this((Throwable) e);
        
        this.startLine = e.getLine();
        this.startColumn = e.getColumn();
    }
    
    /**
     * Indicates the the value of {@link DomainException#getKey()} should
     * be threated as the whole message and not as a key to the resources.
     * 
     * <p>
     * This is needed because some parser messages are not localized and 
     * as such are reported directly in it's original form.
     * 
     * @return <code>false</code> if the key is in fact the whole message 
     */
    public boolean isResource() {
        return this.resource;
    }

    protected void setResource(boolean resource) {
        this.resource = resource;
    }

    public int getEndColumn() {
        return this.endColumn;
    }

    public int getEndLine() {
        return this.endLine;
    }

    public int getStartColumn() {
        return this.startColumn;
    }

    public int getStartLine() {
        return this.startLine;
    }
    
    protected void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    protected void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    protected void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    protected void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    /**
     * @return <code>true</code> if you can use the line information available
     */
    public boolean hasLineInformation() {
        return this.startLine != -1 && this.startColumn != -1;
    }
    
    /**
     * @return <code>true</code> if there is an end line and end column point available
     */
    public boolean hasRangedLineInformation() {
        return hasLineInformation() && this.endLine != -1 && this.endColumn != -1;
    }
}
