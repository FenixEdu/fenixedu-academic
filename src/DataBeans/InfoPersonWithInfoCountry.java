/*
 * Created on Jun 9, 2004
 *  
 */
package DataBeans;

import Dominio.IPessoa;
import Dominio.Pessoa;

/**
 * @author João Mota
 *  
 */
public class InfoPersonWithInfoCountry extends InfoPerson {

    public void copyFromDomain(IPessoa person) {
        super.copyFromDomain(person);
        if (person != null) {
            setInfoPais(InfoCountry.newInfoFromDomain(person.getPais()));
        }
    }

    public static InfoPerson newInfoFromDomain(IPessoa person) {
        InfoPersonWithInfoCountry infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPersonWithInfoCountry();
            infoPerson.copyFromDomain(person);
        }
        return infoPerson;
    }

    public void copyToDomain(InfoPerson infoPerson, IPessoa person) {
        super.copyToDomain(infoPerson, person);
        person.setPais(InfoCountry.newDomainFromInfo(infoPerson.getInfoPais()));
    }

    public static IPessoa newDomainFromInfo(InfoPerson infoPerson) {
        IPessoa person = null;
        InfoPersonWithInfoCountry infoPersonWithInfoCountry = null;
        if (infoPerson != null) {
            person = new Pessoa();
            infoPersonWithInfoCountry = new InfoPersonWithInfoCountry();
            infoPersonWithInfoCountry.copyToDomain(infoPerson, person);
        }
        return person;
    }
}