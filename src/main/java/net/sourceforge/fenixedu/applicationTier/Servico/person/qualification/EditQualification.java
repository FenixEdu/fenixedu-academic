/*
 * Created on 11/Ago/2005 - 11:04:49
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;


import net.sourceforge.fenixedu.applicationTier.Filtro.person.QualificationManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.person.InfoQualification;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class EditQualification {

    protected void run(Integer qualificationId, InfoQualification infoQualification) throws FenixServiceException {
        Qualification qualification = AbstractDomainObject.fromExternalId(qualificationId);
        // If it doesn't exist in the database, a new one has to be created
        Country country = AbstractDomainObject.fromExternalId(infoQualification.getInfoCountry().getExternalId());
        if (qualification == null) {
            Person person = (Person) AbstractDomainObject.fromExternalId(infoQualification.getInfoPerson().getExternalId());
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