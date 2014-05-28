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
package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ManageUnitPersistentGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PersistentGroupMembers;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import pt.ist.fenixframework.Atomic;

public class EditPersistentGroup {

    protected void run(PersistentGroupMembers group, String name, List<Person> people, Unit unit) {

        group.setName(name);
        group.setUnit(unit);
        group.getPersons().clear();
        for (Person person : people) {
            group.addPersons(person);
        }
    }

    // Service Invokers migrated from Berserk

    private static final EditPersistentGroup serviceInstance = new EditPersistentGroup();

    @Atomic
    public static void runEditPersistentGroup(PersistentGroupMembers group, String name, List<Person> people, Unit unit)
            throws NotAuthorizedException {
        ManageUnitPersistentGroup.instance.execute(group.getUnit());
        serviceInstance.run(group, name, people, unit);
    }

}