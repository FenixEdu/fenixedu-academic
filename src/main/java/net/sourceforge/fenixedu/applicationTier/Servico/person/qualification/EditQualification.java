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
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class EditQualification {

    protected void run(String qualificationId, InfoQualification infoQualification) throws FenixServiceException {
        Qualification qualification = FenixFramework.getDomainObject(qualificationId);
        // If it doesn't exist in the database, a new one has to be created
        Country country = FenixFramework.getDomainObject(infoQualification.getInfoCountry().getExternalId());
        if (qualification == null) {
            Person person = (Person) FenixFramework.getDomainObject(infoQualification.getInfoPerson().getExternalId());
            qualification = new Qualification(person, country, infoQualification);

        } else {
            qualification.edit(infoQualification, country);
        }

    }

    // Service Invokers migrated from Berserk

    private static final EditQualification serviceInstance = new EditQualification();

    @Atomic
    public static void runEditQualification(String qualificationId, InfoQualification infoQualification)
            throws FenixServiceException, NotAuthorizedException {
        QualificationManagerAuthorizationFilter.instance.execute(infoQualification);
        serviceInstance.run(qualificationId, infoQualification);
    }

}