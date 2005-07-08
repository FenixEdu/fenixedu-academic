/*
 * Created on Jun 9, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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

    public void copyToDomain(InfoPerson infoPerson, IPerson person) throws ExcepcaoPersistencia {
        super.copyToDomain(infoPerson, person);

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ICountry country = (ICountry) sp.getIPersistentCountry().readByOID(Country.class,
                infoPerson.getInfoPais().getIdInternal());
        person.setPais(country);
    }

}
