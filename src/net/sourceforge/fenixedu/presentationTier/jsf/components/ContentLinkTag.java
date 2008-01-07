package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class ContentLinkTag extends UIComponentTag {

    private String contentIdInternal;
    private String label;
        
    @Override
    public String getComponentType() {	
	return UIContentLink.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {	
	return null;
    }

    public String getContentIdInternal() {
        return contentIdInternal;
    }

    public void setContentIdInternal(String contentIdInternal) {
        this.contentIdInternal = contentIdInternal;
    }
        
    protected void setProperties(UIComponent component) {
        super.setProperties(component);        
        JsfTagUtils.setInteger(component, "contentIdInternal", this.contentIdInternal.toString());       
        JsfTagUtils.setString(component, "label", this.label); 
    }

    public void release() {
        super.release();
        contentIdInternal = null;
        label = null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
