/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.person;

import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoQualificationWithPersonAndCountry extends InfoQualification {

    public void copyFromDomain(Qualification qualification) {
        super.copyFromDomain(qualification);
        if (qualification != null) {
            setInfoCountry(InfoCountry.newInfoFromDomain(qualification.getCountry()));
            setInfoPerson(InfoPersonWithInfoCountry.newInfoFromDomain(qualification.getPerson()));
        }
    }

    public static InfoQualification newInfoFromDomain(Qualification qualification) {
        InfoQualificationWithPersonAndCountry infoQualification = null;
        if (qualification != null) {
            infoQualification = new InfoQualificationWithPersonAndCountry();
            infoQualification.copyFromDomain(qualification);
        }
        return infoQualification;
    }

    public void copyToDomain(InfoQualification infoQualification, Qualification qualification)
            throws ExcepcaoPersistencia {
        super.copyToDomain(infoQualification, qualification);

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        Country country = (Country) sp.getIPersistentCountry().readByOID(Country.class,
                infoQualification.getInfoCountry().getIdInternal());
        qualification.setCountry(country);

        Person person = (Person) sp.getIPessoaPersistente().readByOID(Person.class,
                infoQualification.getInfoPerson().getIdInternal());
        qualification.setPerson(person);
    }

}
