/*
 * Created on Jun 9, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author João Mota
 * 
 */
public class InfoPersonWithInfoCountry extends InfoPerson {

    public void copyFromDomain(Person person) {
        super.copyFromDomain(person);
        if (person != null) {
            setInfoPais(InfoCountry.newInfoFromDomain(person.getPais()));
        }
    }

    public static InfoPerson newInfoFromDomain(Person person) {
        InfoPersonWithInfoCountry infoPerson = null;
        if (person != null) {
            infoPerson = new InfoPersonWithInfoCountry();
            infoPerson.copyFromDomain(person);
        }
        return infoPerson;
    }

    public void copyToDomain(InfoPerson infoPerson, Person person) throws ExcepcaoPersistencia {
        super.copyToDomain(infoPerson, person);

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Country country = (Country) sp.getIPersistentCountry().readByOID(Country.class,
                infoPerson.getInfoPais().getIdInternal());
        person.setPais(country);
    }

}
