/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.renderers.providers;

import org.fenixedu.academic.domain.degree.DegreeType;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import java.util.stream.Collectors;

public class BolonhaDegreeTypesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        return DegreeType.all()
                .filter(type -> !type.isEmpty())
                .filter(type -> type.isBolonhaType() || type.getUnstructured())
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Converter getConverter() {
        return null;
    }

}
