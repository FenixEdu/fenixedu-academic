/*
 * Created on Jun 9, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IPerson;

/**
 * @author João Mota
 *  
 */
public class InfoPersonWithInfoCountryAndAdvisories extends InfoPersonWithAdvisories {

    public void copyFromDomain(IPerson person) {
        super.copyFromDomain(person);
        if (person != null) {
            setInfoPais(InfoCountry.newInfoFromDomain(person.getPais()));
        }
    }

    public static InfoPerson newInfoFromDomain(IPerson person) {
        InfoPersonWithInfoCountryAndAdvisories infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPersonWithInfoCountryAndAdvisories();
            infoPerson.copyFromDomain(person);
        }
        return infoPerson;
    }
}