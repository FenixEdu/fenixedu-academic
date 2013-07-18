/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.text.ParseException;
import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.PersonInformationFromUniqueCardDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class UpdatePersonInformationFromCitizenCard {

    @Service
    public static void run(PersonInformationFromUniqueCardDTO personDTO) {
        Collection<Person> persons = Person.readByDocumentIdNumber(personDTO.getDocumentIdNumber());
        if (persons.isEmpty() || persons.size() > 1) {
            throw new DomainException("UpdatePersonInformationFromCitizenCard.error.personNotFound");
        }

        Person person = persons.iterator().next();
        if (person.getIdDocumentType() != IDDocumentType.IDENTITY_CARD) {
            throw new DomainException("UpdatePersonInformationFromCitizenCard.error.personIdTypeNotIdentityCard");
        }

        try {
            person.editFromBean(personDTO);
        } catch (ParseException e) {
            throw new DomainException("UpdatePersonInformationFromCitizenCard.error.dateParseError", e);
        }
    }

}