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

import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.CurricularCourseFunctor;
import org.fenixedu.academic.domain.interfaces.HasExecutionYear;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExecutionDegreesWithDissertationByExecutionYearProvider implements DataProvider {

    private static class HasDissertationPredicate implements CurricularCourseFunctor {

        boolean hasDissertation = false;

        @Override
        public void doWith(final CurricularCourse curricularCourse) {
            if (curricularCourse.isDissertation()) {
                hasDissertation = true;
            }
        }

        @Override
        public boolean keepDoing() {
            return !hasDissertation;
        }

    }

    @Override
    public Object provide(Object source, Object currentValue) {
        final HasExecutionYear hasExecutionYear = (HasExecutionYear) source;
        final Set<ExecutionDegree> executionDegrees =
                new TreeSet<ExecutionDegree>(ExecutionDegree.EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
        final ExecutionYear executionYear = hasExecutionYear.getExecutionYear();
        if (executionYear != null) {
            for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
                if (!executionDegree.getDegreeCurricularPlan().isEmpty() && hasDissertation(executionDegree)) {
                    executionDegrees.add(executionDegree);
                }
            }
        }
        return executionDegrees;
    }

    private boolean hasDissertation(final ExecutionDegree executionDegree) {
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
        final HasDissertationPredicate hasDissertationPredicate = new HasDissertationPredicate();
        degreeCurricularPlan.doForAllCurricularCourses(hasDissertationPredicate);
        return hasDissertationPredicate.hasDissertation;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
