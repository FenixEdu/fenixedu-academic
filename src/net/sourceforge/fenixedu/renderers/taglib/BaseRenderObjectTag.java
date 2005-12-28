package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.TagUtils;

public abstract class BaseRenderObjectTag extends TagSupport {

    private String name;

    private String scope;

    private String property;

    private String layout;

    private String schema;

    private Properties properties;
    
    private Object targetObject;
    
    public BaseRenderObjectTag() {
        super();
    }

    @Override
    public void release() {
        super.release();

        this.name = null;
        this.scope = null;
        this.property = null;
        this.layout = null;
        this.schema = null;
        this.properties = null;
        this.targetObject = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getLayout() {
        if (layout == null) {
            return null;
        }
        
        if (layout.equals("")) {
            return null;
        }
        
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public void setTemplate(String template) {
        // TODO: assign to a diferent field to respect the TagLib spec
        this.layout = "template";
        addRenderProperty("template", template);
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        if (schema == null) {
            return null;
        }
        
        if (schema.equals("")) {
            return null;
        }
        
        return schema;
    }

    public Properties getRenderProperties() {
        if (this.properties == null) {
            this.properties = new Properties();
        }
        
        return this.properties;
    }

    public void addRenderProperty(String name, String value) {
        getRenderProperties().setProperty(name, value);
    }

    protected int getScopeByName(String scope) throws JspException {
        return TagUtils.getInstance().getScope(scope);
    }
        
    protected Object getTargetObject() throws JspException {
        Object object = getTargetObjectByName(); 
        object = getTargetObjectByProperty(object);

        return this.targetObject = object;
    }

    protected Object getTargetObjectByProperty(Object object) {
        if (object != null && getProperty() != null) {
            try {
                return PropertyUtils.getProperty(object, getProperty());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        
        return object;
    }

    protected Object getTargetObjectByName() throws JspException {
        if (getName() != null) {
            if (getScope() != null && getScope().length() > 0) {
                return pageContext.getAttribute(getName(), getScopeByName(getScope()));
            } else {
                return pageContext.findAttribute(getName());
            }
        }
        
        return null;
    }

    public int doStartTag() throws JspException {
        return EVAL_BODY_INCLUDE;
    }
    
    public int doEndTag() throws JspException {
        Object object = getTargetObject();
        String layout = getLayout();
        String schema = getSchema();
        Properties properties = getRenderProperties();
        
        PresentationContext context = createPresentationContext(object, layout, schema, properties);
        HtmlComponent component = renderObject(context, object);

        try {
            drawComponent(context, component);
        } catch (IOException e) {
            throw new JspException("Failed to render component", e);
        }
        
        release(); // force release
        return EVAL_PAGE;
    }

    protected abstract PresentationContext createPresentationContext(Object object, String layout, String schema, Properties properties);

    protected abstract HtmlComponent renderObject(PresentationContext context, Object object);
    
    protected void drawComponent(PresentationContext context, HtmlComponent component) throws IOException, JspException {
        component.draw(pageContext);
    }
}
