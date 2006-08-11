package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupExpressionException;

/**
 * A ParserReport is a wrapper to the expression and line information about the error.
 * This object is used to provide a specific presentation to the expression and 
 * error message.
 * 
 * @author cfgi
 */
public class ParserReport {

    private GroupExpressionException e;
    private String expression;

    public ParserReport(GroupExpressionException e, String expression) {
        super();

        this.e = e;
        this.expression = expression;
    }

    public String getExpression() {
        return this.expression;
    }

    public String getKey() {
        return this.e.getKey();
    }

    public String[] getArgs() {
        return this.e.getArgs();
    }

    public boolean isResource() {
        return this.e.isResource();
    }
    
    public int getEndColumn() {
        return this.e.getEndColumn();
    }

    public int getEndLine() {
        return this.e.getEndLine();
    }

    public int getStartColumn() {
        return this.e.getStartColumn();
    }

    public int getStartLine() {
        return this.e.getStartLine();
    }

    public boolean hasLineInformation() {
        return this.e.hasLineInformation();
    }

    public boolean hasRangedLineInformation() {
        return this.e.hasRangedLineInformation();
    }

}
