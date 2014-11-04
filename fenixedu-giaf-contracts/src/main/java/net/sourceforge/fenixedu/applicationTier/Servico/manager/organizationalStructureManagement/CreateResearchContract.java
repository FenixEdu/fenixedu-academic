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
package org.fenixedu.academic.service.services.manager.organizationalStructureManagement;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.ResearchContract;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.webSiteManager.ResearchContractBean;
import pt.ist.fenixframework.Atomic;

public class CreateResearchContract {

    @Atomic
    public static void run(ResearchContractBean bean) throws FenixServiceException {
        Person person = bean.getPerson();
        if (person == null) {
            if (Person.readPersonByEmailAddress(bean.getEmail()) != null) {
                throw new FenixServiceException("error.email.already.in.use");
            }
            person =
                    Person.createExternalPerson(bean.getPersonNameString(), Gender.MALE, null, null, null, null, bean.getEmail(),
                            bean.getDocumentIDNumber(), bean.getDocumentType());
        }
        ResearchContract.createResearchContract(bean.getContractType(), person, bean.getBegin(), bean.getEnd(), bean.getUnit(),
                bean.getExternalPerson());
        if (!person.hasRole(RoleType.RESEARCHER)) {
            RoleType.grant(RoleType.RESEARCHER, person.getUser());
        }

        // At this point it is guaranteed that the person has a user
    }
}