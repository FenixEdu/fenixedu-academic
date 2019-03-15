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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.student.RegistrationSelectExecutionYearBean;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class StudentExecutionYearsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        List<ExecutionYear> result = new ArrayList(
                getEnrolmentsExecutionYears(((RegistrationSelectExecutionYearBean) source).getRegistration().getStudent()));
        Collections.sort(result, new BeanComparator("year"));
        return result;
    }

    private Collection<ExecutionYear> getEnrolmentsExecutionYears(final Student student) {
        Set<ExecutionYear> executionYears = new HashSet<>();
        for (final Registration registration : student.getRegistrationsSet()) {
            executionYears.addAll(registration.getEnrolmentsExecutionYears());
        }
        return executionYears;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
