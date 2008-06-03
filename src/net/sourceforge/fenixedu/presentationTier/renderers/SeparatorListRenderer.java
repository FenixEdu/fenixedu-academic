package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Collection;
import java.util.Iterator;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

public class SeparatorListRenderer extends OutputRenderer {

    private static String DEFAULT_SEPARATOR = " > ";

    private String separator;

    private String eachSchema;

    private String eachLayout;

    private String param;

    private String link;
    
    private String emptyLabel;

    private Boolean targetBlank;
  
    
    
    public Boolean getTargetBlank() {
        return targetBlank;
    }

    public void setTargetBlank(Boolean targetBlank) {
        this.targetBlank = targetBlank;
    }

    public String getEmptyLabel() {
        return emptyLabel;
    }

    public void setEmptyLabel(String emptyLabel) {
        this.emptyLabel = emptyLabel;
    }

    public String getSeparator() {
	return hasSeparator() ? this.separator : DEFAULT_SEPARATOR;
    }

    public void setSeparator(String separator) {
	this.separator = separator;
    }

    private boolean hasSeparator() {
	return !StringUtils.isEmpty(this.separator);
    }

    public String getEachSchema() {
	return eachSchema;
    }

    public void setEachSchema(String eachSchema) {
	this.eachSchema = eachSchema;
    }

    public String getEachLayout() {
	return eachLayout;
    }

    public void setEachLayout(String eachLayout) {
	this.eachLayout = eachLayout;
    }

    public String getParam() {
	return param;
    }

    public void setParam(String param) {
	this.param = param;
    }

    public String getLink() {
	return link;
    }

    public void setLink(String link) {
	this.link = link;
    }

 
    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {

		final HtmlBlockContainer blockContainer = new HtmlBlockContainer();

		final Collection<Object> objects = (Collection<Object>) object;
		
		final Iterator<Object> iterator = objects.iterator();
		while(iterator.hasNext()) {

		    final Object each = iterator.next();
		    Schema schema = RenderKit.getInstance().findSchema(getEachSchema());		    		    
		    HtmlComponent htmlComponent = renderValue(each, each.getClass(), schema, getEachLayout(), null);

		    if(htmlComponent != null && getLink() != null && !StringUtils.isEmpty(getLink().trim())) {
			if(getParam() != null && !StringUtils.isEmpty(getParam().trim())) {
			    htmlComponent = getHtmlLink(each, htmlComponent);			      			   			    
			} 		
		    }

		    blockContainer.addChild(htmlComponent);
		    blockContainer.addChild(iterator.hasNext() ? new HtmlText(getSeparator()) : new HtmlText());
		}
		
		if(objects.isEmpty() && getEmptyLabel() != null && !StringUtils.isEmpty(getEmptyLabel())) {
		    blockContainer.addChild(new HtmlText(getEmptyLabel()));
		}

		return blockContainer;
	    }

	    private HtmlLink getHtmlLink(Object object, HtmlComponent htmlComponent) {

		String[] split = getParam().trim().split("/");
		String slotName = split[0];
		String paramName = split[1];
		String slotValue = getSlotValue(object, slotName);

		HtmlLink htmlLink = new HtmlLink();
		htmlLink.setUrl(getLink().trim());			
		htmlLink.setModuleRelative(false);
		
		if(getTargetBlank() != null && getTargetBlank()) {
		    htmlLink.setTarget(HtmlLink.Target.BLANK);
		}
		
		htmlLink.setBody(htmlComponent);

		if(slotValue != null) {
		    htmlLink.setParameter(paramName, slotValue);
		}
		
		return htmlLink;
	    }

	    private String getSlotValue(Object object, String slotName) {
		
		String slotValue = null;		
		
		try {
		    slotValue = String.valueOf(PropertyUtils.getProperty(object, slotName));

		} catch (Exception e) {
		    throw new RuntimeException("could not set param name by reading property '" + slotName + "' from object " + object, e);
		}
		
		return slotValue;
	    }
	};  
    }
}
