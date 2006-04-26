package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.renderers.components.state.ErrorMessage;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;

import org.apache.log4j.Logger;

public class MessageTag extends TagSupport {
    private static final Logger logger = Logger.getLogger(MessageTag.class);
    
    private String forName;

    private String showWhat;
    
    public String getFor() {
        return this.forName;
    }

    public void setFor(String forName) {
        this.forName = forName;
    }

    public String getShow() {
        return this.showWhat;
    }

    public void setShow(String show) {
        this.showWhat = show;
    }

    @Override
    public void release() {
        super.release();
        
        this.forName = null;
        this.showWhat = null;
    }

    @Override
    public int doStartTag() throws JspException {
        ErrorMessage message = getMessage();
        
        if (message != null) {
            writeMessage(message);
        }
        
        return SKIP_BODY;
    }

    private ErrorMessage getMessage() {
        MessagesTag parent = (MessagesTag) findAncestorWithClass(this, MessagesTag.class);
        
        if (getFor() == null || parent != null) {
            return parent.getCurrentMessage();
        }
        else {
            if (parent != null) {
                logger.warn("parent 'messages' tag is beeing ignored since 'for' attribute was defined");
            }
            
            IViewState viewState = HasMessagesTag.getViewStateWithId(this.pageContext, getFor());
            
            if (viewState == null) {
                return null;
            }

            List<ErrorMessage> messages = viewState.getMessages();
            if (! messages.isEmpty()) {
                return messages.get(0);
            }
        }

        return null;
    }

    private void writeMessage(ErrorMessage message) throws JspException {
        try {
            if ("slot".equalsIgnoreCase(getShow()) || "label".equalsIgnoreCase(getShow())) {
                if (message.getSlot() != null) {
                    pageContext.getOut().write(message.getSlot().getLabel());
                }
                else {
                    logger.warn("asked to show " + getShow() + " but not " + getShow() + " was defined");
                }
            }
            else {
                pageContext.getOut().write(message.getMessage());
            }
        } catch (IOException e) {
            throw new JspException(e);
        }
    }
    
    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
