package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.Message;
import net.sourceforge.fenixedu.renderers.components.state.Message.Type;

import org.apache.log4j.Logger;

public class MessagesTag extends BodyTagSupport {
    private static final Logger logger = Logger.getLogger(MessagesTag.class);
    
    private Iterator<Message> iterator;
    private Message message;
    
    private String forName;
    private String type;
    
    public String getFor() {
        return this.forName;
    }

    public void setFor(String forName) {
        this.forName = forName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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
        
        this.iterator = getMessagesIterator(viewState, getType());
        return goNext(true);
    }

    private Iterator<Message> getMessagesIterator(IViewState viewState, String type) {
        Type messageType = getMessageType();

        if (messageType == null) {
            return viewState.getMessages().iterator();
        }
        else {
            List<Message> messages = new ArrayList<Message>();
            
            for (Message message : viewState.getMessages()) {
                if (messageType.equals(message.getType())) {
                    messages.add(message);
                }
            }
            
            return messages.iterator();
        }
    }

    public Type getMessageType() {
        HasMessagesTag parent = (HasMessagesTag) findAncestorWithClass(this, HasMessagesTag.class);

        if (getType() != null) {
            if (LogLevel.WARN) {
                if (parent != null && parent.getMessageType() != null) {
                    logger.warn("parent 'hasMessage' tag is beeing ignored since the 'type' attribute was specified");
                }
            }
            
            return Type.valueOf(getType().toUpperCase());
        }
        else {
            if (parent != null) {
                return parent.getMessageType();
            }
        }
        
        return null;
    }

    private String getViewStateId() {
        HasMessagesTag parent = (HasMessagesTag) findAncestorWithClass(this, HasMessagesTag.class);

        if (getFor() != null) {
            if (LogLevel.WARN) {
                if (parent != null) {
                    logger.warn("parent 'hasMessages' tag is beeing ignore since 'for' attribute was specified");
                }
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

    private void setCurrentMessage(Message message) {
        this.message = message;
    }

    public Message getCurrentMessage() {
        return this.message;
    }

}
