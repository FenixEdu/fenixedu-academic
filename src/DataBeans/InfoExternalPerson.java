/*
 * Created on Oct 14, 2003
 *  
 */
package DataBeans;

import Dominio.IExternalPerson;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *  
 */
public class InfoExternalPerson extends InfoObject {
    private InfoPerson infoPerson;

    private InfoWorkLocation infoWorkLocation;

    /**
     * @return Returns the infoPerson.
     */
    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    /**
     * @param infoPerson
     *            The infoPerson to set.
     */
    public void setInfoPerson(InfoPerson infoPerson) {
        this.infoPerson = infoPerson;
    }

    /**
     * @return Returns the infoWorkLocation.
     */
    public InfoWorkLocation getInfoWorkLocation() {
        return infoWorkLocation;
    }

    /**
     * @param infoWorkLocation
     *            The infoWorkLocation to set.
     */
    public void setInfoWorkLocation(InfoWorkLocation infoWorkLocation) {
        this.infoWorkLocation = infoWorkLocation;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof InfoExternalPerson) {
            InfoExternalPerson infoExternalPerson = (InfoExternalPerson) obj;
            result = this.getInfoPerson().equals(infoExternalPerson.getInfoPerson());
        }
        return result;
    }

    public static InfoExternalPerson newInfoFromDomain(IExternalPerson externalPerson) {
        InfoExternalPerson infoExternalPerson = null;
        if (externalPerson != null) {
            infoExternalPerson = new InfoExternalPerson();
            infoExternalPerson.copyFromDomain(externalPerson);
        }
        return infoExternalPerson;
    }
}