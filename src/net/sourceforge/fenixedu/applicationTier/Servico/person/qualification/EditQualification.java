/*
 * Created on 07/11/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Barbosa
 * @author Pica
 */

public class EditQualification extends EditDomainObjectService {

    @Override
    protected void copyInformationFromIntoToDomain(ISuportePersistente sp, InfoObject infoObject,
            IDomainObject domainObject) throws ExcepcaoPersistencia {
        InfoQualification infoQualification = (InfoQualification) infoObject;
        IQualification qualification = (Qualification) domainObject;

        ICountry country = (ICountry) sp.getIPersistentCountry().readByOID(Country.class,
                infoQualification.getInfoCountry().getIdInternal());

        IPerson person = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class,
                infoQualification.getInfoPerson().getIdInternal());

        qualification.setBranch(infoQualification.getBranch());
        qualification.setCountry(country);
        qualification.setCountryKey(country.getIdInternal());
        qualification.setDate(infoQualification.getDate());
        qualification.setDegree(infoQualification.getDegree());
        qualification.setDegreeRecognition(infoQualification.getDegreeRecognition());
        qualification.setEquivalenceDate(infoQualification.getEquivalenceDate());
        qualification.setEquivalenceSchool(infoQualification.getEquivalenceSchool());

        // qualification.setLastModificationDate();
        qualification.setMark(infoQualification.getMark());
        qualification.setPerson(person);
        qualification.setPersonKey(person.getIdInternal());
        qualification.setSchool(infoQualification.getSchool());
        qualification.setSpecializationArea(infoQualification.getSpecializationArea());
        qualification.setTitle(infoQualification.getTitle());
    }

    @Override
    protected IDomainObject createNewDomainObject(InfoObject infoObject) {
        return new Qualification();
    }

    @Override
    protected Class getDomainObjectClass() {
        return Qualification.class;
    }

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        return persistentSuport.getIPersistentQualification();
    }

}
