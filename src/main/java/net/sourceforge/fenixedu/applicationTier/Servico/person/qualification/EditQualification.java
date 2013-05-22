/*
 * Created on 11/Ago/2005 - 11:04:49
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Filtro.person.QualificationManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class EditQualification extends FenixService {

    protected void run(Integer qualificationId, InfoQualification infoQualification) throws FenixServiceException {
        Qualification qualification = rootDomainObject.readQualificationByOID(qualificationId);
        // If it doesn't exist in the database, a new one has to be created
        Country country = rootDomainObject.readCountryByOID(infoQualification.getInfoCountry().getIdInternal());
        if (qualification == null) {
            Person person = (Person) rootDomainObject.readPartyByOID(infoQualification.getInfoPerson().getIdInternal());
            qualification = new Qualification(person, country, infoQualification);

        } else {
            qualification.edit(infoQualification, country);
        }

    }

    // Service Invokers migrated from Berserk

    private static final EditQualification serviceInstance = new EditQualification();

    @Service
    public static void runEditQualification(Integer qualificationId, InfoQualification infoQualification) throws FenixServiceException  , NotAuthorizedException {
        QualificationManagerAuthorizationFilter.instance.execute(qualificationId, infoQualification);
        serviceInstance.run(qualificationId, infoQualification);
    }

}