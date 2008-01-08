package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class ContentLinkTag extends UIComponentTag {

    private String content;
    private String label;
        
    @Override
    public String getComponentType() {	
	return UIContentLink.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {	
	return null;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
        
    protected void setProperties(UIComponent component) {
        super.setProperties(component);        
        JsfTagUtils.setString(component, "content", this.content);       
        JsfTagUtils.setString(component, "label", this.label); 
    }

    public void release() {
        super.release();
        content = null;
        label = null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
