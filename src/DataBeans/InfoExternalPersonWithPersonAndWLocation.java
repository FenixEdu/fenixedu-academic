package DataBeans;

import Dominio.IExternalPerson;

/**
 * @author Fernanda Quitério Created on 6/Set/2004
 *  
 */
public class InfoExternalPersonWithPersonAndWLocation extends InfoExternalPersonWithPerson {

    public void copyFromDomain(IExternalPerson externalPerson) {
        super.copyFromDomain(externalPerson);
        if (externalPerson != null) {
            setInfoWorkLocation(InfoWorkLocation.newInfoFromDomain(externalPerson.getWorkLocation()));
        }
    }

    public static InfoExternalPerson newInfoFromDomain(IExternalPerson externalPerson) {
        InfoExternalPersonWithPersonAndWLocation infoExternalPersonWithPersonAndWLocation = null;
        if (externalPerson != null) {
            infoExternalPersonWithPersonAndWLocation = new InfoExternalPersonWithPersonAndWLocation();
            infoExternalPersonWithPersonAndWLocation.copyFromDomain(externalPerson);
        }
        return infoExternalPersonWithPersonAndWLocation;
    }
}