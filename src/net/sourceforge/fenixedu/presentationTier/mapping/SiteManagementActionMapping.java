/*
 * Created on 4/Jun/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.mapping;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author jpvl
 */
public class SiteManagementActionMapping extends ActionMapping {
    private String componentClassName;

    private String inputForwardName;

    /**
     * @return
     */
    public String getComponentClassName() {
        return componentClassName;
    }

    /**
     * @return
     */
    public String getInputForwardName() {
        return inputForwardName;
    }

    /**
     * @param _string
     */
    public void setComponentClassName(String _string) {
        componentClassName = _string;
    }

    /**
     * @param _string
     */
    public void setInputForwardName(String _string) {
        inputForwardName = _string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.ActionMapping#getInputForward()
     */
    public ActionForward getInputForward() {
        ActionForward forward = new ActionForward();
        forward.setContextRelative(false);
        forward.setPath(getInput());
        return forward;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.config.ActionConfig#getInput()
     */
    public String getInput() {
        return this.getPath() + ".do?method=validationError&page=0";
    }

}