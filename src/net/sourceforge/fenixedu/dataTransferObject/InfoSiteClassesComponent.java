/*
 * Created on 30/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * 
 * 30/Jun/2003 fenix-branch DataBeans
 *  
 */
public class InfoSiteClassesComponent extends DataTranferObject implements ISiteComponent {

    private List infoClasses;

    /**
     * @return
     */
    public List getInfoClasses() {
        return infoClasses;
    }

    /**
     * @param infoClasses
     */
    public void setInfoClasses(List infoClasses) {
        this.infoClasses = infoClasses;
    }

    /**
     *  
     */
    public InfoSiteClassesComponent() {
    }

    public InfoSiteClassesComponent(List infoClasses) {
        setInfoClasses(infoClasses);
    }
}