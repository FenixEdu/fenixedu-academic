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

/**
 * The base class for all renderers. Allows all renderers to be configured with
 * the common properties of all html elements: {@link #setClasses(String) classes},
 * {@link #setStyle(String) style}, and {@link #setTitle(String) title}.
 * 
 * Renderers are currently divided in a complete and disjunctive hierarchy. This
 * class provides the base behaviour for every renderer.
 * 
 * @see net.sourceforge.fenixedu.renderers.OutputRenderer
 * @see net.sourceforge.fenixedu.renderers.InputRenderer
 * 
 * @author cfgi
 */
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
    
   /**
    * Allows you to specify the css classes that are to be applied to the top level
    * html element that is generated.
    *  
    * @property
    */
   public void setClasses(String classes) {
        this.classes = classes;
    }

    /**
     * This property allows you to specify the value of the style attribute of the
     * top level html element that is generated.
     * 
     * @property
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Allows you to specify the title of the top level html element that is generated.
     * 
     *@property 
     */
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

    /**
     * Main method of a renderer that is invoked to do the renderization of a value 
     * into a component. The default behaviour is to {@linkplain #getLayout(Object, Class) get the layout} 
     * for this renderer, configure it with the {@linkplain Layout#getPropertyNames() declared properties} 
     * and {@linkplain #renderComponent(Layout, Object, Class) issue the rendering}.
     * 
     * @return a component representing the presentation of the given value
     */
    public HtmlComponent render(Object object, Class type) {
        Layout layout = getLayout(object, type);
        
        if (layout != null) {
            setLayoutProperties(layout);
        }
        
        HtmlComponent component = renderComponent(layout, object, type);
        return component;
    }
    
    /**
     * This is the base method that should be overriden but every renderer. Each renderer must
     * render the given value accordingly with a layout. The layout represents the way information
     * will be presented.
     * 
     * @return the layout to be used in the rendering
     */
    protected abstract Layout getLayout(Object object, Class type);

    /**
     * Creates the component according to the layout given. This method is called from {@link #render(Object, Class)}
     * and is basically delegates the creation of the component to the layout.
     * 
     * @return a component representing the presentation of the given value
     */
    protected HtmlComponent renderComponent(Layout layout, Object object, Class type) {
        return layout.createLayout(object, type);
    }

    /**
     * Copies all propreties from this renderer to the given layout. The layout 
     * declares the properties it's interested through {@link Layout#getPropertyNames()}. 
     * If this renderer has a property with the same name, it's copied to the layout.
     * 
     * @param the layout ot configure
     */
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
    
    /**
     * Starts the rendering process for a concrete value. The presentation context used to present the value
     * will be a subcontext of the {@linkplain #getContext() current context}. This means that the presentation
     * will made in the same mode. The subcontext will be configured with the given schema, layout, and properties.
     * <p>
     * If you want to present the <code>null</code> value or a value in a different presentation mode then
     * you must use {@link RenderKit#render(PresentationContext, Object, Class)} directly.
     */
    protected HtmlComponent renderValue(Object value, Class type, Schema schema, String layout, Properties properties) {
        MetaObject metaObject = MetaObjectFactory.createObject(value, schema);

        PresentationContext newContext = getContext().createSubContext(metaObject);
        newContext.setSchema(schema == null ? null : schema.getName());
        newContext.setLayout(layout);
        newContext.setProperties(properties);
        
        RenderKit kit = RenderKit.getInstance();
        return kit.render(newContext, value, type);
    }
    
    /**
     * This method is a convenience method for the previous one. It assumes that no properties are set in
     * the subcontext. 
     */
    protected HtmlComponent renderValue(Object value, Schema schema, String layout) {
        return renderValue(value, value.getClass(), schema, layout, new Properties());
    }
    
    /**
     * This method is a convenience method for the previous one. It assumes that the value is not null.
     */
    protected HtmlComponent renderValue(Object value, Class type, Schema schema, String layout) {
        return renderValue(value, type, schema, layout, new Properties());
    }

    /**
     * Starts the rendering process for the value of a meta slot. A subcontext will be created to present the
     * slot's value. That subcontext will be configured with the layout and properties defined in the slot.
     */
    protected HtmlComponent renderSlot(MetaSlot slot) {
        PresentationContext newContext = createPresentationContext(slot);
        
        Object value = slot.getObject();
        Class type = slot.getType();
        
        RenderKit kit = RenderKit.getInstance(); 
        return kit.render(newContext, value, type);
    }

    /**
     * Same as previous method but does the rendering with the specified renderer. The renderer
     * must know how to handle the value of the slot. 
     */
    protected HtmlComponent renderSlot(Renderer renderer, MetaSlot slot) {
        PresentationContext newContext = createPresentationContext(slot);
        
        Object value = slot.getObject();
        Class type = slot.getType();
        
        RenderKit kit = RenderKit.getInstance(); 
        return kit.renderUsing(renderer, newContext, value, type);
    }

    protected PresentationContext createPresentationContext(MetaSlot slot) {
        MetaObject metaObject = MetaObjectFactory.createObject(slot.getObject(), slot.getSchema());
        
        PresentationContext newContext = getContext().createSubContext(metaObject);
        newContext.setSchema(slot.getSchema() != null ? slot.getSchema().getName() : null);
        newContext.setLayout(slot.getLayout());
        newContext.setProperties(slot.getProperties());
        
        return newContext;
    }
    
}
