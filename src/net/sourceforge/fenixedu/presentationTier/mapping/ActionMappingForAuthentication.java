/*
 * Created on 7/Mai/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.presentationTier.mapping;

import org.apache.struts.action.ActionMapping;

/**
 * @author jpvl
 */
public class ActionMappingForAuthentication extends ActionMapping {
    private String application = "";

    /**
     * @return
     */
    public String getApplication() {
        return application;
    }

    /**
     * @param string
     */
    public void setApplication(String application) {
        this.application = application;
    }

}