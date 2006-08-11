package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.Serializable;

/**
 * Helper used to collect a single expression string from the user.
 * 
 * @author cfgi
 */
public class ExpressionBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String expression;
    
    public ExpressionBean() {
        super();
    }

    public ExpressionBean(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        if (expression == null) {
            this.expression = null;
        }
        else {
            // replace \r\n or \r newlines
            this.expression = expression.replaceAll("\\r\\n?", "\n");
        }
    }
}
