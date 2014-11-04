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
package org.fenixedu.academic.ui.renderers.providers.lists;

import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.administrativeOffice.lists.ExecutionDegreeListBean;
import org.fenixedu.academic.ui.renderers.converters.AcademicIntervalConverter;

import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AcademicIntervalProviderForListsContextBean implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final SortedSet<AcademicInterval> academicIntervals =
                new TreeSet<AcademicInterval>(AcademicInterval.REVERSE_COMPARATOR_BY_BEGIN_DATE);

        final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) source;
        if (executionDegreeBean.getDegreeCurricularPlan() != null) {

            for (ExecutionDegree exeDegree : executionDegreeBean.getDegreeCurricularPlan().getExecutionDegreesSet()) {
                academicIntervals.add(exeDegree.getAcademicInterval());
            }
        }

        return academicIntervals;
    }

    @Override
    public Converter getConverter() {
        return new AcademicIntervalConverter();
    }
}
