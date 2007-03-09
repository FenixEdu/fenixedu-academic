package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.contexts.PresentationContext;
import net.sourceforge.fenixedu.renderers.model.UserIdentityFactory;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;

public abstract class BaseRenderObjectTag extends TagSupport {

    private String name;

    private String scope;

    private String property;

    private String type;
    
    private String layout;

    private String schema;

    private Properties properties;
    
    private String sortBy;
    
    private Map<String, ViewDestination> destinations;
    
    public BaseRenderObjectTag() {
        super();
        
        this.destinations = new Hashtable<String, ViewDestination>();
    }

    @Override
    public void release() {
        super.release();

        this.name = null;
        this.scope = null;
        this.property = null;
        this.type = null;
        this.layout = null;
        this.schema = null;
        this.properties = null;
        this.destinations = new Hashtable<String, ViewDestination>();
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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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
        // TODO: cfgi, assign to a different field to respect the TagLib spec
        this.layout = "template";
        addRenderProperty("template", template);
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSortBy() {
        return this.sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
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
        
        return getTargetObjectByProperty(object);
    }

    protected Object getTargetObjectByProperty(Object object) {
        if (object != null && getProperty() != null) {
            try {
                return PropertyUtils.getProperty(object, getProperty());
            } catch (Exception e) {
                throw new RuntimeException("object '" + object + "' does not have property '" + getProperty() + "'", e);
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
        
        if (object == null && !isNullAccepted()) {
            throw new RuntimeException("cannot present the null value, name='" + getName() + "' property='" + getProperty() + "' scope='" + getScope() + "'");
        }
        
        // TODO: cfgi, verify if this is more usefull than problematic
        if (object instanceof List) {
            object = sortCollection((Collection) object);
        }
        
        String layout = getLayout();
        String schema = getSchema();
        Properties properties = getRenderProperties();
        
        PresentationContext context = createPresentationContext(object, layout, schema, properties);
        context.getViewState().setUser(UserIdentityFactory.create((HttpServletRequest) this.pageContext.getRequest()));
        
        HtmlComponent component = renderObject(context, object);

        try {
            drawComponent(context, component);
        } catch (IOException e) {
            e.printStackTrace();
            throw new JspException("failed to render component", e);
        }
        
        release(); // force release
        return EVAL_PAGE;
    }

    protected boolean isNullAccepted() {
        return getType() != null;
    }

    protected Collection sortCollection(Collection collection) {
        if (getSortBy() != null) {
            return RenderUtils.sortCollectionWithCriteria(collection, getSortBy());
        }
        else {
            return collection;
        }
    }

    protected abstract PresentationContext createPresentationContext(Object object, String layout, String schema, Properties properties);

    protected abstract HtmlComponent renderObject(PresentationContext context, Object object) throws JspException;
    
    protected void drawComponent(PresentationContext context, HtmlComponent component) throws IOException, JspException {
        component.draw(pageContext);
    }

    protected ViewDestination normalizeDestination(ViewDestination destination, String currentPath, String module) {
        if (destination.getModule() == null) {
            destination.setModule(module);
        }

        if (destination.getPath() == null) {
            destination.setPath(currentPath);
        }

        return destination;
    }

    public void addDestination(String name, String path, String module, boolean redirect) {
        this.destinations.put(name, new ViewDestination(path, module, redirect));
    }

    public Map<String, ViewDestination> getDestinations() {
        return this.destinations;
    }
    
    protected void setViewStateDestinations(IViewState viewState) {
        viewState.setInputDestination(getInputDestination());

        String currentPath = getCurrentPath();
        ModuleConfig module = TagUtils.getInstance().getModuleConfig(pageContext);
   
        for (String name : getDestinations().keySet()) {
            ViewDestination destination = getDestinations().get(name);
            
            viewState.addDestination(name, normalizeDestination(destination, currentPath, module.getPrefix()));
        }
    }

    protected ViewDestination getInputDestination() {
        String currentPath = getCurrentPath();
        ModuleConfig module = TagUtils.getInstance().getModuleConfig(pageContext);
        
        return new ViewDestination(currentPath, module.getPrefix(), false);       
    }

    protected String getCurrentPath() {
        ActionMapping mapping = (ActionMapping) pageContext.findAttribute(Globals.MAPPING_KEY);
        
        String currentPath;
        String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
        
        if (mapping != null) {
            currentPath = TagUtils.getInstance().getActionMappingURL(mapping.getPath(), pageContext);
        }
        else {
            currentPath = ((HttpServletRequest) pageContext.getRequest()).getServletPath();
        }
        
        if (currentPath.startsWith(contextPath)) {
            currentPath = currentPath.substring(contextPath.length());
        }
    
        ModuleConfig module = TagUtils.getInstance().getModuleConfig(pageContext);
        if (module != null && currentPath.startsWith(module.getPrefix())) {
            currentPath = currentPath.substring(module.getPrefix().length());
        }
        
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        if (request.getQueryString() != null) {
            currentPath = currentPath + "?" + request.getQueryString();
        }
        
        return currentPath;
       
    }
 
}
