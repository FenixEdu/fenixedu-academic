/*
 * Created on Jun 9, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;

/**
 * @author João Mota
 *  
 */
public class InfoPersonWithInfoCountry extends InfoPerson {

    public void copyFromDomain(IPerson person) {
        super.copyFromDomain(person);
        if (person != null) {
            setInfoPais(InfoCountry.newInfoFromDomain(person.getPais()));
        }
    }

    public static InfoPerson newInfoFromDomain(IPerson person) {
        InfoPersonWithInfoCountry infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPersonWithInfoCountry();
            infoPerson.copyFromDomain(person);
        }
        return infoPerson;
    }

    public void copyToDomain(InfoPerson infoPerson, IPerson person) {
        super.copyToDomain(infoPerson, person);
        person.setPais(InfoCountry.newDomainFromInfo(infoPerson.getInfoPais()));
    }

    public static IPerson newDomainFromInfo(InfoPerson infoPerson) {
        IPerson person = null;
        InfoPersonWithInfoCountry infoPersonWithInfoCountry = null;
        if (infoPerson != null) {
            person = new Person();
            infoPersonWithInfoCountry = new InfoPersonWithInfoCountry();
            infoPersonWithInfoCountry.copyToDomain(infoPerson, person);
        }
        return person;
    }
}