package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class DegreeCurricularPlanTag extends UIComponentTag {
    private String dcp;
    private String toEdit;

    public String getDcp() {
        return dcp;
    }

    public void setDcp(String dcp) {
        this.dcp = dcp;
    }

    public String getToEdit() {
        return toEdit;
    }

    public void setToEdit(String toEdit) {
        this.toEdit = toEdit;
    }
    
    public String getComponentType() {
        return UIDegreeCurricularPlan.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        
        JsfTagUtils.setString(component,"dcp",this.dcp);
        JsfTagUtils.setString(component,"toEdit",this.toEdit);
    }

    public void release() {
        super.release();
        dcp = null;
        toEdit = null;
    }

}
