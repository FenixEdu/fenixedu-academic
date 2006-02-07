package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class DegreeCurricularPlanTag extends UIComponentTag {
    private String dcp;
    private String onlyStructure;
    private String toEdit;
    private String organizeBy;
    private String showRules;

    public String getDcp() {
        return dcp;
    }

    public void setDcp(String dcp) {
        this.dcp = dcp;
    }

    public String getOnlyStructure() {
        return onlyStructure;
    }

    public void setOnlyStructure(String onlyStructure) {
        this.onlyStructure = onlyStructure;
    }
    
    public String getToEdit() {
        return toEdit;
    }

    public void setToEdit(String toEdit) {
        this.toEdit = toEdit;
    }

    public String getOrganizeBy() {
        return organizeBy;
    }

    public void setOrganizeBy(String structureBy) {
        this.organizeBy = structureBy;
    }

    public String getShowRules() {
        return showRules;
    }

    public void setShowRules(String showRules) {
        this.showRules = showRules;
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
        JsfTagUtils.setString(component,"onlyStructure",this.onlyStructure);
        JsfTagUtils.setString(component,"toEdit",this.toEdit);        
        JsfTagUtils.setString(component,"organizeBy",this.organizeBy);
        JsfTagUtils.setString(component,"showRules",this.showRules);
    }

    public void release() {
        super.release();
        dcp = null;
        toEdit = null;
        organizeBy = null;
        showRules = null;
    }

}
