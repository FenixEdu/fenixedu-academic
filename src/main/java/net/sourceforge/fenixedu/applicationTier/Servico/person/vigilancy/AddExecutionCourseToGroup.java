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
package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import pt.ist.fenixframework.Atomic;

public class AddExecutionCourseToGroup {

    @Atomic
    public static List<ExecutionCourse> run(VigilantGroup group, List<ExecutionCourse> executionCourses) {

        List<ExecutionCourse> executionCoursesUnableToAdd = new ArrayList<ExecutionCourse>();
        for (ExecutionCourse course : executionCourses) {
            try {
                group.addExecutionCourses(course);

            } catch (DomainException e) {
                executionCoursesUnableToAdd.add(course);
            }
        }
        return executionCoursesUnableToAdd;
    }

}