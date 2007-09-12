package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.contexts.OutputContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class GenericOutputWithPrefix extends OutputRenderer {

    private String prefixBundle;
    private String prefixKey;
    private String prefixClasses;
    private String emptyBundle;
    private String emptyKey;
    private String subLayout;
    private String separator;
    private boolean indented;
    
    private Map<String,String> properties = new HashMap<String,String>();;
    
    
    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    private Map<String,String> getPropertiesMap() {
	return properties;
    }
    
    public void setSubProperty(String property, String value) {
	properties.put(property, value);
    }
    
    public String getSubProperty(String property) {
	return properties.get(property);
    }
    
    public String getEmptyBundle() {
        return emptyBundle;
    }

    public void setEmptyBundle(String emptyBundle) {
        this.emptyBundle = emptyBundle;
    }

    public String getEmptyKey() {
        return emptyKey;
    }

    public void setEmptyKey(String emptyKey) {
        this.emptyKey = emptyKey;
    }

    public String getPrefixBundle() {
        return prefixBundle;
    }

    public void setPrefixBundle(String prefixBundle) {
        this.prefixBundle = prefixBundle;
    }

    public String getPrefixKey() {
        return prefixKey;
    }

    public void setPrefixKey(String prefixKey) {
        this.prefixKey = prefixKey;
    }

    public String getPrefixClasses() {
        return prefixClasses;
    }

    public void setPrefixClasses(String prefixClasses) {
        this.prefixClasses = prefixClasses;
    }

    public String getSubLayout() {
        return subLayout;
    }
    
    public void setSubLayout(String subLayout) {
        this.subLayout = subLayout;
    }
    
    public boolean isIndented() {
	return indented;
    }
    
    public void setIndented(boolean indented) {
	this.indented = indented;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    private Properties getProperties() {
		Properties properties = new Properties();
		Map<String,String> map = getPropertiesMap(); 
		for(String property : map.keySet()) {
		    properties.put(property, map.get(property));
		}
		return properties;
	    }

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		OutputContext context = getOutputContext();
		
		context.setLayout(getSubLayout());
		context.setProperties(getProperties());
		context.getMetaObject().setProperties(getProperties());
		
		HtmlComponent component = possiblyNonEmptyPresentationObject(object) ? RenderKit.getInstance().render(context, object, type) :
		    new HtmlText(RenderUtils.getResourceString(getEmptyBundle(), getEmptyKey()),false);
		
		HtmlText text = new HtmlText((getPrefixBundle() != null) ? RenderUtils.getResourceString(
			getPrefixBundle(), getPrefixKey()) : getPrefixKey(), getPrefixBundle() == null);

		text.setClasses(getPrefixClasses());
		HtmlContainer container = new HtmlBlockContainer();
		container.addChild(text);
		container.addChild(new HtmlText(getSeparator(), false));
		container.addChild(component);
		container.setIndented(isIndented());
		return container;
	    }

	    private boolean possiblyNonEmptyPresentationObject(Object object) {
		if(object == null) {
		    return false;
		}
		if(object instanceof String) {
		    String theString = (String) object;
		    if(StringUtils.isEmpty(theString)) {
			return false;
		    }
		}
		if(object instanceof List) {
		    List theList = (List)object;
		    if(theList.isEmpty()) {
			return false;
		    }
		}
		return true;
	    }
	};
    }

}
