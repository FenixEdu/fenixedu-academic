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

import java.util.Collections;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.student.ManageStudentStatuteBean;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentExecutionPeriodsProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final ExecutionYear firstExecutionYear =
                getFirstRegistrationExecutionYear(((ManageStudentStatuteBean) source).getStudent());

        return ExecutionYear.readNotClosedExecutionYears().stream().filter(ey -> ey.isAfterOrEquals(firstExecutionYear))
                .flatMap(ey -> ey.getExecutionPeriodsSet().stream())
                .sorted(Collections.reverseOrder(ExecutionYear.COMPARATOR_BY_BEGIN_DATE)).collect(Collectors.toList());

    }

    private ExecutionYear getFirstRegistrationExecutionYear(final Student student) {

        ExecutionYear firstYear = null;
        for (Registration registration : student.getRegistrationsSet()) {
            if (firstYear == null) {
                firstYear = registration.getRegistrationYear();
                continue;
            }

            if (registration.getRegistrationYear().isBefore(firstYear)) {
                firstYear = registration.getRegistrationYear();
            }
        }
        return firstYear;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
