package net.sourceforge.fenixedu.renderers;

import java.util.Properties;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

public abstract class Renderer {

    private PresentationContext context;

    private String classes;

    private String style;

    private String title;
    
    public PresentationContext getContext() {
        return context;
    }
    
    public void setContext(PresentationContext context) {
        this.context = context;
    }
    
    public void setClasses(String classes) {
        this.classes = classes;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setTitle(String title) {
        this.title = title;
    }
        
    public String getClasses() {
        return classes;
    }

    public String getStyle() {
        return style;
    }

    public String getTitle() {
        return title;
    }

    public HtmlComponent render(Object object, Class type) {
        Layout layout = getLayout(object, type);
        
        if (layout != null) {
            setLayoutProperties(layout);
        }
        
        HtmlComponent component = renderComponent(layout, object, type);
        return component;
    }
    
    protected abstract Layout getLayout(Object object, Class type);

    protected HtmlComponent renderComponent(Layout layout, Object object, Class type) {
        return layout.createLayout(object, type);
    }

    protected void setLayoutProperties(Layout layout) {
        String[] names = layout.getAndVerifyPropertyNames();
        
        Properties layoutProperties = new Properties();
        
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            
            try {
                if (PropertyUtils.getProperty(this, name) != null) {
                    layoutProperties.setProperty(name, BeanUtils.getProperty(this, name));
                }
            } catch (Exception e) {
                // ignore
            }
        }

        RenderUtils.setProperties(layout, layoutProperties);
    }

    //
    // rendering support
    //
    
    protected HtmlComponent renderValue(Object value, Schema schema, String layout, Properties properties) {
        MetaObject metaObject = MetaObjectFactory.createObject(value, schema);

        PresentationContext newContext = getContext().createSubContext(metaObject);
        newContext.setLayout(layout);
        newContext.setProperties(properties);
        
        RenderKit kit = RenderKit.getInstance();
        return kit.render(newContext, value);
    }
    
    protected HtmlComponent renderValue(Object value, Schema schema, String layout) {
        return renderValue(value, schema, layout, new Properties());
    }
    
    protected HtmlComponent renderSlot(MetaSlot slot) {
        PresentationContext newContext = getContext().createSubContext(slot);
        newContext.setLayout(slot.getLayout());
        newContext.setProperties(slot.getProperties());
        
        Object value = slot.getObject();
        Class type = slot.getType();
        
        RenderKit kit = RenderKit.getInstance(); 
        return kit.render(newContext, value, type);
    }
}
