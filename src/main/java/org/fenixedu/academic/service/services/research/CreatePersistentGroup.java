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
package org.fenixedu.academic.service.services.research;

import java.util.List;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.PersistentGroupMembers;
import org.fenixedu.academic.domain.accessControl.PersistentGroupMembersType;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

public class CreatePersistentGroup {

    protected void run(Unit unit, String name, List<Person> people, PersistentGroupMembersType type) {

        PersistentGroupMembers members = new PersistentGroupMembers(name, type);
        members.setUnit(unit);
        for (Person person : people) {
            members.addPersons(person);
        }
    }

    // Service Invokers migrated from Berserk

    private static final CreatePersistentGroup serviceInstance = new CreatePersistentGroup();

    @Atomic
    public static void runCreatePersistentGroup(Unit unit, String name, List<Person> people, PersistentGroupMembersType type)
            throws NotAuthorizedException {
        serviceInstance.run(unit, name, people, type);
    }
}