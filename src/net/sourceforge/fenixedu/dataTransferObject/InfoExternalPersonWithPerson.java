package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IExternalPerson;

/**
 * @author Fernanda Quitério Created on 6/Set/2004
 *  
 */
public class InfoExternalPersonWithPerson extends InfoExternalPerson {

    public void copyFromDomain(IExternalPerson externalPerson) {
        super.copyFromDomain(externalPerson);
        if (externalPerson != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(externalPerson.getPerson()));
        }
    }

    public static InfoExternalPerson newInfoFromDomain(IExternalPerson externalPerson) {
        InfoExternalPersonWithPerson infoExternalPersonWithPerson = null;
        if (externalPerson != null) {
            infoExternalPersonWithPerson = new InfoExternalPersonWithPerson();
            infoExternalPersonWithPerson.copyFromDomain(externalPerson);
        }
        return infoExternalPersonWithPerson;
    }
}