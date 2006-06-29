package net.sourceforge.fenixedu.renderers.contexts;

import java.util.Properties;

import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.utils.RenderMode;

public abstract class PresentationContext {

    private PresentationContext parentContext;
    
    private RenderMode renderMode;
    
    private String schema;
    
    private String layout;
    
    private Properties properties;

    private IViewState viewState;

    private MetaObject metaObject;
    
    public PresentationContext() {
        super();
        
        this.parentContext = null;
    }
    
    protected PresentationContext(PresentationContext parent) {
        super();
        
        this.parentContext = parent;
    }
    
    public RenderMode getRenderMode() {
        return renderMode;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
    }

    public PresentationContext getParentContext() {
        return parentContext;
    }

    public void setParentContext(PresentationContext parentContext) {
        this.parentContext = parentContext;
    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public boolean hasViewState() {
        return getViewState() != null;
    }
    
    public IViewState getViewState() {
        if (viewState != null) {
            return viewState; 
        }
        
        if (this.parentContext != null) {
            return this.parentContext.getViewState();
        }

        return null;
    }

    public void setViewState(IViewState viewState) {
        this.viewState = viewState;
        
        if (viewState != null) {
            setMetaObject(viewState.getMetaObject());
        }
    }

    public MetaObject getMetaObject() {
        return this.metaObject;
    }
    
    public void setMetaObject(MetaObject metaObject) {
        this.metaObject = metaObject;
    }
    
    public abstract PresentationContext createSubContext(MetaObject metaObject);
}
