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
package net.sourceforge.fenixedu.presentationTier.renderers.providers;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DomainObjectUtil;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.MergeExecutionCourseDA.DegreesMergeBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DestinationExecutionCoursesProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final DegreesMergeBean degreeBean = (DegreesMergeBean) source;

        Degree destinationDegree = degreeBean.getDestinationDegree();

        List<ExecutionCourse> destinationExecutionCourses =
                destinationDegree.getExecutionCourses(degreeBean.getAcademicInterval());

        Collections.sort(destinationExecutionCourses, DomainObjectUtil.COMPARATOR_BY_ID);

        removeDuplicates(destinationExecutionCourses);

        Collections.sort(destinationExecutionCourses, ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME);

        return destinationExecutionCourses;
    }

    public static void removeDuplicates(final List<ExecutionCourse> beanList) {
        if (beanList.isEmpty()) {
            return;
        }

        final Iterator<ExecutionCourse> iter = beanList.iterator();
        ExecutionCourse prev = iter.next();
        while (iter.hasNext()) {
            final ExecutionCourse curr = iter.next();
            if (curr.equals(prev)) {
                iter.remove();
            }
            prev = curr;
        }
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
}