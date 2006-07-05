package net.sourceforge.fenixedu.renderers.taglib;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;

public class EditViewStateTag extends EditObjectTag {

    @Override
    public int doEndTag() throws JspException {
        IViewState viewState = (IViewState) getTargetObject();

        if (viewState.getId() != null) {
            setId(viewState.getId());
        }
        else {
            setId(null);
        }
        
        return super.doEndTag();
    }

    @Override
    protected Object getTargetObject() throws JspException {
        if (!isPostBack()) {
            return super.getTargetObject();
        } else {
            return getViewState();
        }
    }

    @Override
    protected PresentationContext createPresentationContext(Object object, String layout, String schema, Properties properties) {
        IViewState viewState = (IViewState) object;
        
        viewState.setRequest((HttpServletRequest) pageContext.getRequest());
        setViewStateDestinations(viewState);
        
        InputContext inputContext = new InputContext();

        inputContext.setLayout(layout);
        inputContext.setSchema(schema);
        inputContext.setProperties(properties);

        inputContext.setViewState(viewState);
        
        return inputContext;
    }

    @Override
    protected HtmlComponent renderObject(PresentationContext context, Object object) throws JspException {
        return ((IViewState) object).getComponent();
    }
    
}
