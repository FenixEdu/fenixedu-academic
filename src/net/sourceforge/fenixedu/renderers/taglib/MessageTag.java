package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.Message;
import net.sourceforge.fenixedu.renderers.components.state.SlotMessage;
import net.sourceforge.fenixedu.renderers.components.state.Message.Type;

import org.apache.log4j.Logger;

public class MessageTag extends TagSupport {
    private static final Logger logger = Logger.getLogger(MessageTag.class);
    
    private String forName;
    private String type;
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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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
        Message message = getMessage();
        
        if (message != null) {
            writeMessage(message);
        }
        
        return SKIP_BODY;
    }

    private Message getMessage() {
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

            List<Message> messages = viewState.getMessages();
            if (! messages.isEmpty()) {
                return messages.get(0);
            }
        }

        return null;
    }

    private void writeMessage(Message message) throws JspException {
        Type type = getMessageType();
        
        if (type == null || type.equals(message.getType())) {
            try {
                if ("slot".equalsIgnoreCase(getShow()) || "label".equalsIgnoreCase(getShow())) {
                    if (message instanceof SlotMessage) {
                        SlotMessage slotMessage = (SlotMessage) message;
    
                        if (slotMessage.getSlot() != null) {
                            pageContext.getOut().write(slotMessage.getSlot().getLabel());
                        }
                        else {
                            logger.warn("asked to show " + getShow() + " but not " + getShow() + " was defined");
                        }
                    }
                }
                else {
                    pageContext.getOut().write(message.getMessage());
                }
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
    }
    
    private Type getMessageType() {
        MessagesTag parent = (MessagesTag) findAncestorWithClass(this, MessagesTag.class);

        if (getType() != null) {
            if (parent != null && parent.getMessageType() != null) {
                logger.warn("parent 'messages' tag is beeing ignored since the 'type' attribute was specified");
            }
            
            return Type.valueOf(String.valueOf(getType()).toUpperCase());
        }
        else {
            if (parent != null) {
                return parent.getMessageType();
            }
        }
        
        return null;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
