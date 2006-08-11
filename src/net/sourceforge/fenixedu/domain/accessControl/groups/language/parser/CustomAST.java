package net.sourceforge.fenixedu.domain.accessControl.groups.language.parser;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.parser.GroupExpressionMarker.Point;
import antlr.CommonAST;
import antlr.Token;
import antlr.collections.AST;

/**
 * This custom AST node is used to store line information that isn't always available
 * in the tree parser rules. 
 * 
 * @author cfgi
 */
public class CustomAST extends CommonAST {

    private static final long serialVersionUID = 1L;

    /**
     * The marker enclosing all the relevant elements of this AST. If this AST 
     * represents a single character then if contains no valid end point.
     */
    public GroupExpressionMarker marker;
    
    public CustomAST() {
        super();
        
        this.marker = new GroupExpressionMarker();
    }

    private void setupMarker(Token token) {
        if (token.getText() == null || token.getText().length() == 1) {
            this.marker = new GroupExpressionMarker(token.getLine(), token.getColumn());
        }
        else {
            Point start = new Point(token.getLine(), token.getColumn());
            Point end   = new Point(token.getLine(), token.getColumn() + token.getText().length());
            
            this.marker = new GroupExpressionMarker(start, end);
        }
    }

    public void initialize(AST ast) {
        super.initialize(ast);
        
        if (ast instanceof CustomAST) {
            this.marker = ((CustomAST) ast).marker;
        }
    }

    @Override
    public void initialize(Token token) {
        super.initialize(token);
        
        setupMarker(token);
    }

    @Override
    public void addChild(AST ast) {
        super.addChild(ast);
        
        if (ast instanceof CustomAST) {
            CustomAST customAST = (CustomAST) ast;
            
            expandMarker(customAST.marker);
        }
    }

    public void expandMarker(GroupExpressionMarker marker) {
        this.marker.setStart(Point.smaller(this.marker.getStart(), marker.getStart(), marker.getEnd()));
        this.marker.setEnd  (Point.greater(this.marker.getEnd(), marker.getStart(), marker.getEnd()));
    }

}
