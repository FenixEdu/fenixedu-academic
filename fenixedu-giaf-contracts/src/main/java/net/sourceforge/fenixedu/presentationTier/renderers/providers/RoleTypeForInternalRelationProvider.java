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
package org.fenixedu.academic.ui.renderers.providers;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.person.RoleType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RoleTypeForInternalRelationProvider implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final List<RoleType> roleTypes = new ArrayList<RoleType>();
        roleTypes.add(RoleType.TEACHER);
        roleTypes.add(RoleType.RESEARCHER);
        roleTypes.add(RoleType.EMPLOYEE);
        roleTypes.add(RoleType.GRANT_OWNER);
        return roleTypes;
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
