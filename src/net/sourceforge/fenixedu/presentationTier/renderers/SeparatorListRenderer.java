package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

public class SeparatorListRenderer extends OutputRenderer {

    private static String DEFAULT_SEPARATOR = " > ";

    private String separator;

    private String eachSchema;

    private String eachLayout;

    private String param;

    private String link;


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

		final List<Object> objects = (List<Object>) object;
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
		htmlLink.setTarget(HtmlLink.Target.BLANK);
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
