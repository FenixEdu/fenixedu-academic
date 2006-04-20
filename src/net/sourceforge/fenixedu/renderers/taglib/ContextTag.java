package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.renderers.components.HtmlHiddenField;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.LifeCycleConstants;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;

// TODO: cfgi, check if extending the struts from tag is the best option
//       it could be a completly separated tag that acted only as a container
//       problem: would need to be inside a form <form><tag></tag></form>
public class ContextTag extends TagSupport {

    private List<IViewState> viewStates;
    
    public ContextTag() {
        super();
        
        this.viewStates = new ArrayList<IViewState>();
    }

    @Override
    public void release() {
        super.release();
        
        this.viewStates = new ArrayList<IViewState>();
    }

    @Override
    public int doStartTag() throws JspException {
        return EVAL_BODY_INCLUDE;
    }
    
    @Override
    public int doEndTag() throws JspException {
        try {
            HtmlHiddenField hidden = new HtmlHiddenField(LifeCycleConstants.VIEWSTATE_LIST_PARAM_NAME, encodeViewStates());
        
            hidden.draw(this.pageContext);
        } catch (IOException e) {
            throw new JspException(e);
        }
        
        release();
        return EVAL_PAGE;
    }

    private String encodeViewStates() throws IOException {
        return ViewState.encodeListToBase64(this.viewStates);
    }

    public void addViewState(IViewState viewState) {
        this.viewStates.add(viewState);
    }
}
