/*
 * Created on Jun 26, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.person;

import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonWithInfoCountry;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Pica
 * @author Barbosa
 */
public class InfoQualificationWithPersonAndCountry extends InfoQualification {

    public void copyFromDomain(IQualification qualification) {
        super.copyFromDomain(qualification);
        if (qualification != null) {
            setInfoCountry(InfoCountry.newInfoFromDomain(qualification.getCountry()));
            setInfoPerson(InfoPersonWithInfoCountry.newInfoFromDomain(qualification.getPerson()));
        }
    }

    public static InfoQualification newInfoFromDomain(IQualification qualification) {
        InfoQualificationWithPersonAndCountry infoQualification = null;
        if (qualification != null) {
            infoQualification = new InfoQualificationWithPersonAndCountry();
            infoQualification.copyFromDomain(qualification);
        }
        return infoQualification;
    }

    public void copyToDomain(InfoQualification infoQualification, IQualification qualification)
            throws ExcepcaoPersistencia {
        super.copyToDomain(infoQualification, qualification);

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        ICountry country = (ICountry) sp.getIPersistentCountry().readByOID(Country.class,
                infoQualification.getInfoCountry().getIdInternal());
        qualification.setCountry(country);

        IPerson person = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class,
                infoQualification.getInfoPerson().getIdInternal());
        qualification.setPerson(person);
    }

}
