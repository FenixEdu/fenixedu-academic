/*
 * Created on 11/Ago/2005 - 11:04:49
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class EditQualification implements IService {

    public void run(Integer qualificationId, InfoQualification infoQualification) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentObject po = sp.getIPersistentObject();
		
		IQualification qualification = (IQualification) po.readByOID(Qualification.class, qualificationId);
		//If it doesn't exist in the database, a new one has to be created
		ICountry country = (ICountry) po.readByOID(Country.class, infoQualification.getInfoCountry().getIdInternal());
		if(qualification == null) {
			IPerson person = (IPerson) po.readByOID(Person.class, infoQualification.getInfoPerson().getIdInternal());
			qualification = DomainFactory.makeQualification(person, country, infoQualification);
		
		} else {
			qualification.edit(infoQualification, country);
		}
		
		
    }

}
