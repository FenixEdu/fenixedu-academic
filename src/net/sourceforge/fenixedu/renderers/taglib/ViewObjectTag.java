package net.sourceforge.fenixedu.renderers.taglib;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

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
    protected HtmlComponent renderObject(PresentationContext context, Object object)  {
        RenderKit kit = RenderKit.getInstance();
        
        return kit.render(context, object);
    }

    @Override
    protected PresentationContext createPresentationContext(Object object, String layout, String schema, Properties properties) {
        OutputContext context = new OutputContext();
        
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
        
        context.setViewState(viewState);
        context.setMetaObject(metaObject);
        
        return context;
    }
}
