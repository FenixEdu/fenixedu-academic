/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * 
 * 23/Jul/2003 fenix-head DataBeans
 *  
 */
public class InfoSiteSCDegrees extends DataTranferObject implements ISiteComponent {

    private List degrees;

    /**
     * @return
     */
    public List getDegrees() {
        return degrees;
    }

    /**
     * @param degrees
     */
    public void setDegrees(List degrees) {
        this.degrees = degrees;
    }

    /**
     *  
     */
    public InfoSiteSCDegrees() {
    }

}