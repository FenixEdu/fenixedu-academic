package net.sourceforge.fenixedu.renderers.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.LifeCycleConstants;
import net.sourceforge.fenixedu.renderers.components.state.Message;
import net.sourceforge.fenixedu.renderers.components.state.Message.Type;

public class HasMessagesTag extends TagSupport {

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
        
        this.forName = null;
        this.type = null;
    }

    @Override
    public int doStartTag() throws JspException {
        IViewState viewState = getViewStateWithId(this.pageContext, getFor());
        
        if (viewState == null) {
            return SKIP_BODY;
        }
        else if (! hasMessages(viewState, getType())) {
            return SKIP_BODY;
        }
        else {
            return EVAL_BODY_INCLUDE;
        }
    }

    private boolean hasMessages(IViewState viewState, String type) {
        if (type == null) {
            return ! viewState.getMessages().isEmpty();
        }
        else {
            Type messageType = getMessageType();
            
            for (Message message : viewState.getMessages()) {
                if (messageType.equals(message.getType())) {
                    return true;
                }
            }
            
            return false;
        }
    }

    public Type getMessageType() {
        String type = getType();
        
        if (type != null) {
            return Type.valueOf(type.toUpperCase());
        }
        else {
            return null;
        }
    }
    
    public static IViewState getViewStateWithId(PageContext context, String id) {
        List<IViewState> viewStates = (List<IViewState>) context.findAttribute(LifeCycleConstants.VIEWSTATE_PARAM_NAME);
        
        if (viewStates != null) {
            for (IViewState state : viewStates) {
                if (id == null) {
                    return state;
                }
                
                if (id.equals(state.getId())) {
                    return state;
                }
            }
        }
        
        return null;
    }

}
