package net.sourceforge.fenixedu.renderers.taglib;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.state.ViewState;
import net.sourceforge.fenixedu.renderers.contexts.OutputContext;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

public class ViewObjectTag extends BaseRenderObjectTag {

    @Override
    protected HtmlComponent renderObject(PresentationContext context, Object object) throws JspException  {
        if (getType() == null) {
            return RenderKit.getInstance().render(context, object);
        }
        else {
            try {
                Class type = Class.forName(getType());

                return RenderKit.getInstance().render(context, object, type);
            } catch (ClassNotFoundException e) {
                throw new JspException("could not get class named " + getType(), e);
            }
        }
    }

    @Override
    protected PresentationContext createPresentationContext(Object object, String layout, String schema, Properties properties) {
        OutputContext context = new OutputContext();
        
        context.setSchema(schema);
        context.setLayout(layout);
        context.setProperties(properties);
        
        Schema realSchema = RenderKit.getInstance().findSchema(schema);
        MetaObject metaObject = MetaObjectFactory.createObject(object, realSchema);

        ViewState viewState = new ViewState(null);
        viewState.setMetaObject(metaObject);

        viewState.setInputDestination(getInputDestination());
        
        viewState.setLayout(getLayout());
        viewState.setProperties(getRenderProperties());
        viewState.setContextClass(context.getClass());
        viewState.setRequest((HttpServletRequest) pageContext.getRequest());
        
        setViewStateDestinations(viewState);
        
        context.setViewState(viewState);
        context.setMetaObject(metaObject);
        
        return context;
    }
}
