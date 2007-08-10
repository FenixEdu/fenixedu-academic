package net.sourceforge.fenixedu.renderers;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.contexts.InputContext;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

/**
 * This renderer allows a comment to be added to any
 * layout. 
 * 
 *  
 * @author pcma
 */

public class GenericInputWithComment extends InputRenderer {

    private String bundle;
    private String comment;
    private String commentClasses;
    private String subLayout;
    private Map<String,String> properties = new HashMap<String,String>();;
    
    private Map<String,String> getPropertiesMap() {
	return properties;
    }
    
    public void setSubProperty(String property, String value) {
	properties.put(property, value);
    }
    
    public String getSubProperty(String property) {
	return properties.get(property);
    }
    
    public String getBundle() {
        return bundle;
    }
    
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getCommentClasses() {
        return commentClasses;
    }
    
    public void setCommentClasses(String commentClasses) {
        this.commentClasses = commentClasses;
    }
    
    public String getSubLayout() {
        return subLayout;
    }
    
    public void setSubLayout(String subLayout) {
        this.subLayout = subLayout;
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
		InputContext context = getInputContext();
		
		context.setLayout(getSubLayout());
		context.setProperties(getProperties());
		context.getMetaObject().setProperties(getProperties());
		
		HtmlComponent component = RenderKit.getInstance().render(context, object, type);
		
		HtmlText text = new HtmlText((getBundle() != null) ? RenderUtils.getResourceString(
			getBundle(), getComment()) : getComment(), getBundle() == null);

		text.setClasses(getCommentClasses());
		HtmlContainer container = new HtmlBlockContainer();
		container.addChild(component);
		container.addChild(new HtmlText("<br/>", false));
		container.addChild(text);
		
		return container;
	        
	    }
	    
	};
	
    }

    

}
