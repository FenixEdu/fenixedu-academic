package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.LifeCycleConstants;
import net.sourceforge.fenixedu.renderers.components.state.ViewStateMessage;

public class MessageTag extends TagSupport {

    private String forName;
    
    public String getFor() {
        return this.forName;
    }

    public void setFor(String forName) {
        this.forName = forName;
    }

    @Override
    public int doStartTag() throws JspException {
        IViewState viewState = getViewStateWithId(getFor());
        
        if (viewState == null) {
            return SKIP_BODY;
        }

        List<ViewStateMessage> messages = viewState.getMessages();
        if (! messages.isEmpty()) {
            writeMessage(messages.get(0));
        }
        
        return SKIP_BODY;
    }

    private IViewState getViewStateWithId(String id) {
        List<IViewState> viewStates = (List<IViewState>) pageContext.findAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME);
        
        if (viewStates != null) {
            for (IViewState state : viewStates) {
                if (state.getId() != null && state.getId().equals(id)) {
                    return state;
                }
            }
        }
        
        return null;
    }

    private void writeMessage(ViewStateMessage message) throws JspException {
        try {
            pageContext.getOut().write(message.getMessage());
        } catch (IOException e) {
            throw new JspException(e);
        }
    }
    
    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
