package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.renderers.components.state.ErrorMessage;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;

import org.apache.log4j.Logger;

public class MessagesTag extends BodyTagSupport {
    private static final Logger logger = Logger.getLogger(MessagesTag.class);
    
    private Iterator<ErrorMessage> iterator;
    private ErrorMessage message;
    
    private String forName;
    
    public String getFor() {
        return this.forName;
    }

    public void setFor(String forName) {
        this.forName = forName;
    }

    @Override
    public void release() {
        super.release();
        
        this.iterator = null;
        this.message = null;
        this.forName = null;
    }

    @Override
    public int doStartTag() throws JspException {
        IViewState viewState = HasMessagesTag.getViewStateWithId(this.pageContext, getViewStateId());

        if (viewState == null) {
            return SKIP_BODY;
        }
        
        this.iterator = viewState.getMessages().iterator();
        return goNext(true);
    }

    private String getViewStateId() {
        HasMessagesTag parent = (HasMessagesTag) findAncestorWithClass(this, HasMessagesTag.class);

        if (getFor() != null) {
            if (parent != null) {
                logger.warn("parent 'hasMessages' tag is beeing ignore since 'for' attribute was specified");
            }
            
            return getFor();
        }
        
        if (parent == null) {
            return null;
        }
        
        return parent.getFor();
    }

    private int goNext(boolean starting) {
        if (! this.iterator.hasNext()) {
            return SKIP_BODY;
        }
        
        setCurrentMessage(iterator.next());
        
        if (starting) {
            return EVAL_BODY_INCLUDE;
        }
        else {
            return EVAL_BODY_AGAIN;
        }
    }

    @Override
    public int doAfterBody() throws JspException {
        try {
            BodyContent body = getBodyContent();
            
            if (body != null) {
                JspWriter out = body.getEnclosingWriter();
                out.println(body.getString());
                body.clearBody(); // Clear for next evaluation
            }
        } catch (IOException e) {
            throw new JspException("could not write body", e);
        }
        
        return goNext(false);
    }

    @Override
    public int doEndTag() throws JspException {
        this.iterator = null;
        this.message = null;
        
        return EVAL_PAGE;
    }

    private void setCurrentMessage(ErrorMessage message) {
        this.message = message;
    }

    public ErrorMessage getCurrentMessage() {
        return this.message;
    }

}
