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
package org.fenixedu.academic.ui.renderers.providers.executionDegree;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.dto.administrativeOffice.ExecutionDegreeBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;

import com.google.common.collect.Sets;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesToCreateRegistration implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {

        final Set<Degree> allowed = AcademicAccessRule
                .getDegreesAccessibleToFunction(AcademicOperationType.CREATE_REGISTRATION, Authenticate.getUser())
                .collect(Collectors.toSet());
        allowed.addAll(PermissionService.getDegrees("ACADEMIC_OFFICE_REGISTRATION_CREATION", Authenticate.getUser()));

        final Set<Degree> executed;
        final ExecutionYear year =
                source instanceof ExecutionDegreeBean ? ((ExecutionDegreeBean) source).getExecutionYear() : null;
        if (year != null && !year.getExecutionDegreesSet().isEmpty()) {
            executed = ((ExecutionDegreeBean) source).getExecutionYear().getExecutionDegreesSet().stream().map(i -> i.getDegree())
                    .collect(Collectors.toSet());
        } else {
            executed = Bennu.getInstance().getExecutionDegreesSet().stream().map(i -> i.getDegree()).collect(Collectors.toSet());
        }

        return Sets.intersection(executed, allowed).stream().sorted(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID)
                .collect(Collectors.toList());
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
