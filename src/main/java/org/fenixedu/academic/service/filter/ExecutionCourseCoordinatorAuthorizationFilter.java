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
/*
 * Created on 19/Mai/2003
 */
package org.fenixedu.academic.service.filter;

import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.service.filter.coordinator.CoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 */
public class ExecutionCourseCoordinatorAuthorizationFilter extends CoordinatorAuthorizationFilter {

    public static final ExecutionCourseCoordinatorAuthorizationFilter instance =
            new ExecutionCourseCoordinatorAuthorizationFilter();

    protected ExecutionYear getSpecificExecutionYear(String executionCourseID) {
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

        return (executionCourse == null) ? null : executionCourse.getExecutionYear();
    }

    public void execute(String executionCourseID) throws NotAuthorizedException {
        Person person = Authenticate.getUser().getPerson();

        if (!RoleType.COORDINATOR.isMember(person.getUser())) {
            deny();
        }

        SortedSet<Coordinator> coordinators = new TreeSet<Coordinator>(new CoordinatorByExecutionDegreeComparator());
        coordinators.addAll(person.getCoordinatorsSet());

        if (coordinators.isEmpty()) {
            deny();
        }

        ExecutionYear executionYear = getSpecificExecutionYear(executionCourseID);

        Coordinator coordinator = coordinators.first();
        ExecutionYear coordinatorExecutionYear = coordinator.getExecutionDegree().getExecutionYear();

        if (executionYear == null || coordinatorExecutionYear.compareTo(executionYear) < 0) {
            deny();
        }
    }

    public void deny() throws NotAuthorizedException {
        throw new NotAuthorizedException();
    }

}
