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

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesWithDissertationProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        SortedSet<Degree> degrees = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);

        for (Degree degree : getDegrees(source)) {
            if (degree.getDegreeType().isPreBolonhaDegree() || degree.getDegreeType().isPreBolonhaMasterDegree()
                    || degree.getDegreeType().isBolonhaMasterDegree() || degree.getDegreeType().isIntegratedMasterDegree()) {
                // Do Nothing
            } else {
                continue;
            }

            planLoop: for (DegreeCurricularPlan plan : degree.getDegreeCurricularPlansSet()) {
                if (!plan.isActive()) {
                    continue;
                }

                for (CurricularCourse course : plan.getCurricularCoursesSet()) {
                    if (course.isDissertation()) {
                        degrees.add(degree);
                        break planLoop;
                    }
                }
            }
        }

        return degrees;
    }

    protected Collection<Degree> getDegrees(@SuppressWarnings("unused") Object source) {
        return Degree.readNotEmptyDegrees();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
