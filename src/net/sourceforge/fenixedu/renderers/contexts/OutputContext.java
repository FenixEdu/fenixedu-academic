package net.sourceforge.fenixedu.renderers.contexts;

import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

public class OutputContext extends PresentationContext {

    public OutputContext() {
        super();
        
        setRenderMode(RenderMode.getMode("output"));
    }
    
    protected OutputContext(OutputContext parent) {
        super(parent);
    }

    @Override
    public OutputContext createSubContext(MetaObject metaObject) {
        OutputContext context = new OutputContext(this);
        
        //context.setLayout(getLayout());
        context.setMetaObject(metaObject);
        //context.setProperties(metaObject.getProperties());
        
        context.setRenderMode(getRenderMode());
        
        return context;
    }
}
