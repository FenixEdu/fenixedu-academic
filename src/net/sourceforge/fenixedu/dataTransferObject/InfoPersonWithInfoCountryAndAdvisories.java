/*
 * Created on Jun 9, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Person;

/**
 * @author João Mota
 *  
 */
public class InfoPersonWithInfoCountryAndAdvisories extends InfoPersonWithAdvisories {

    public void copyFromDomain(Person person) {
        super.copyFromDomain(person);
        if (person != null) {
            setInfoPais(InfoCountry.newInfoFromDomain(person.getPais()));
        }
    }

    public static InfoPerson newInfoFromDomain(Person person) {
        InfoPersonWithInfoCountryAndAdvisories infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPersonWithInfoCountryAndAdvisories();
            infoPerson.copyFromDomain(person);
        }
        return infoPerson;
    }
}