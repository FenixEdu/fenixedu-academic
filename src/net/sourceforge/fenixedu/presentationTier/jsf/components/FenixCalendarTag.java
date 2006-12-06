package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class FenixCalendarTag extends UIComponentTag {
    
    private String begin;
    private String end;
    private String createLink;
    private String editLinkPage;
    private String editLinkParameters;
    private String extraLines;

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCreateLink() {
        return createLink;
    }

    public void setCreateLink(String createLink) {
        this.createLink = createLink;
    }

    public String getEditLinkPage() {
        return editLinkPage;
    }

    public void setEditLinkPage(String editLinkPage) {
        this.editLinkPage = editLinkPage;
    }

    public String getEditLinkParameters() {
        return editLinkParameters;
    }

    public void setEditLinkParameters(String editLinkParameters) {
        this.editLinkParameters = editLinkParameters;
    }

    public String getExtraLines() {
        return extraLines;
    }
    
    public void setExtraLines(String extraLines) {
        this.extraLines = extraLines;
    }

    public String getComponentType() {
        return UIFenixCalendar.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        
        JsfTagUtils.setString(component,"begin",this.begin);
        JsfTagUtils.setString(component,"end",this.end);
        JsfTagUtils.setString(component,"createLink",this.createLink);
        JsfTagUtils.setString(component,"editLinkPage",this.editLinkPage);
        JsfTagUtils.setString(component,"editLinkParameters",this.editLinkParameters);
        JsfTagUtils.setString(component,"extraLines",this.extraLines);
    }

    public void release() {
        super.release();
        begin = null;
        end = null;
        createLink = null;
        editLinkPage = null;
        editLinkParameters = null;
        extraLines = null;
    }


}
