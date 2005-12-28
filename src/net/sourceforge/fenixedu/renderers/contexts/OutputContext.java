package net.sourceforge.fenixedu.renderers.contexts;

import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

public class OutputContext extends PresentationContext {

    public OutputContext() {
        super();
    }
    
    protected OutputContext(OutputContext parent) {
        super(parent);
    }

    @Override
    public RenderMode getRenderMode() {
        return RenderMode.getMode("output");
    }

    @Override
    public OutputContext createSubContext(MetaObject metaObject) {
        OutputContext context = new OutputContext(this);
        
        //context.setLayout(getLayout());
        context.setMetaObject(metaObject);
        //context.setProperties(metaObject.getProperties());
        
        return context;
    }
}
