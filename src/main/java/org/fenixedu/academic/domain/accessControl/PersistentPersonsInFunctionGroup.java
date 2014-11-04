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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.organizationalStructure.Function;

import org.fenixedu.bennu.core.groups.Group;

public class PersistentPersonsInFunctionGroup extends PersistentPersonsInFunctionGroup_Base {
    protected PersistentPersonsInFunctionGroup(Function function) {
        super();
        setFunction(function);
    }

    @Override
    public Group toGroup() {
        return PersonsInFunctionGroup.get(getFunction());
    }

    @Override
    protected void gc() {
        setFunction(null);
        super.gc();
    }

    public static PersistentPersonsInFunctionGroup getInstance(Function function) {
        return singleton(() -> Optional.ofNullable(function.getPersonsInFunctionGroup()),
                () -> new PersistentPersonsInFunctionGroup(function));
    }
}
