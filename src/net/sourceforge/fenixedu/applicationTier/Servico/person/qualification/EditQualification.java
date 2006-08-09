/*
 * Created on 11/Ago/2005 - 11:04:49
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class EditQualification extends Service {

    public void run(Integer qualificationId, InfoQualification infoQualification) throws FenixServiceException, ExcepcaoPersistencia {
		Qualification qualification = rootDomainObject.readQualificationByOID(qualificationId);
		//If it doesn't exist in the database, a new one has to be created
		Country country = rootDomainObject.readCountryByOID(infoQualification.getInfoCountry().getIdInternal());
		if(qualification == null) {
			Person person = (Person) rootDomainObject.readPartyByOID(infoQualification.getInfoPerson().getIdInternal());
			qualification = new Qualification(person, country, infoQualification);
		
		} else {
			qualification.edit(infoQualification, country);
		}
		
		
    }

}
