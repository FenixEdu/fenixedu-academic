/*
 * Created on 5/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * @author João Mota
 * 
 *  
 */
public class InfoSiteClasses extends DataTranferObject implements ISiteComponent {

    private List classes;

    /**
     *  
     */
    public InfoSiteClasses() {
    }

    /**
     * @return
     */
    public List getClasses() {
        return classes;
    }

    /**
     * @param list
     */
    public void setClasses(List list) {
        classes = list;
    }

}