/*
 * Created on Oct 14, 2003
 *  
 */
package DataBeans;

import Dominio.IWorkLocation;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *  
 */
public class InfoWorkLocation extends InfoObject {
    private String name;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param workLocation
     * @return
     */
    public static InfoWorkLocation newInfoFromDomain(IWorkLocation workLocation) {
        InfoWorkLocation infoWorkLocation = null;
        if (workLocation != null) {
            infoWorkLocation = new InfoWorkLocation();
            infoWorkLocation.copyFromDomain(workLocation);
        }

        return infoWorkLocation;
    }

    public void copyFromDomain(IWorkLocation workLocation) {
        super.copyFromDomain(workLocation);
        if (workLocation != null) {
            setName(workLocation.getName());
        }
    }

}