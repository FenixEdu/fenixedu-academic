/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class UpdatePersonInformationFromCitizenCard {

    @Atomic
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