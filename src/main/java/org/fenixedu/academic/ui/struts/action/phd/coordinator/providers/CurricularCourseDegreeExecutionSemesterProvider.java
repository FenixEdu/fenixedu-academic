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
package org.fenixedu.academic.ui.struts.action.phd.coordinator.providers;

import java.util.Collection;
import java.util.TreeSet;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.phd.ManageEnrolmentsBean;
import org.fenixedu.academic.ui.renderers.providers.AbstractDomainObjectProvider;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurricularCourseDegreeExecutionSemesterProvider extends AbstractDomainObjectProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;

        final Collection<ExecutionSemester> result = new TreeSet<ExecutionSemester>(new ReverseComparator());

        for (final ExecutionYear executionYear : bean.getCurricularCourse().getDegreeCurricularPlan().getExecutionYears()) {
            result.addAll(executionYear.getExecutionPeriodsSet());
        }

        return result;
    }
}