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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.interfaces.HasDegreeType;
import org.fenixedu.academic.domain.interfaces.HasExecutionYear;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreeForExecutionYearAndDegreeTypeProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();

        final HasExecutionYear hasExecutionYear = (HasExecutionYear) source;
        final HasDegreeType hasDegreeType = (HasDegreeType) source;
        final ExecutionYear executionYear = hasExecutionYear.getExecutionYear();
        if (executionYear != null) {
            final DegreeType degreeType = hasDegreeType.getDegreeType();
            for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
                if (degreeType == null || match(degreeType, executionDegree)) {
                    executionDegrees.add(executionDegree);
                }
            }
        }

        Collections.sort(executionDegrees, ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);

        return executionDegrees;
    }

    private boolean match(final DegreeType degreeType, final ExecutionDegree executionDegree) {
        return executionDegree.getDegreeType() == degreeType;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
