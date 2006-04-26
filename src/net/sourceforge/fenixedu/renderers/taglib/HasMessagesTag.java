package net.sourceforge.fenixedu.renderers.taglib;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.LifeCycleConstants;

public class HasMessagesTag extends TagSupport {

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
        
        this.forName = null;
    }

    @Override
    public int doStartTag() throws JspException {
        IViewState viewState = getViewStateWithId(this.pageContext, getFor());
        
        if (viewState == null) {
            return SKIP_BODY;
        }
        else if (viewState.getMessages().isEmpty()) {
            return SKIP_BODY;
        }
        else {
            return EVAL_BODY_INCLUDE;
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
