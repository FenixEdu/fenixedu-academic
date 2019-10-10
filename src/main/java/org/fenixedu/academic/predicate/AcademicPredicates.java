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
package org.fenixedu.academic.predicate;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.bennu.core.security.Authenticate;

public class AcademicPredicates {

	public static final AccessControlPredicate<Object> MANAGE_DEGREE_CURRICULAR_PLANS = new AccessControlPredicate<Object>() {
		@Override
		public boolean evaluate(final Object degree) {
			Set<Degree> allowedDegrees = new HashSet<Degree>();
			allowedDegrees.addAll(AcademicAccessRule
					.getDegreesAccessibleToFunction(AcademicOperationType.MANAGE_DEGREE_CURRICULAR_PLANS,
							Authenticate.getUser())
					.collect(Collectors.toSet()));
			allowedDegrees
					.addAll(PermissionService.getDegrees("MANAGE_DEGREE_CURRICULAR_PLANS", Authenticate.getUser()));
			return allowedDegrees.contains(degree);
		};
	};

	public static final AccessControlPredicate<Object> MANAGE_EXECUTION_COURSES = new AccessControlPredicate<Object>() {
		@Override
		public boolean evaluate(final Object program) {
			return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.MANAGE_EXECUTION_COURSES,
					(AcademicProgram) program, Authenticate.getUser())
					|| PermissionService.isMember("MANAGE_EXECUTION_COURSES", (Degree) program, Authenticate.getUser());
		};
	};

	public static final AccessControlPredicate<Object> MANAGE_EXECUTION_COURSES_ADV = new AccessControlPredicate<Object>() {
		@Override
		public boolean evaluate(final Object program) {
			return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.MANAGE_EXECUTION_COURSES_ADV,
					(AcademicProgram) program, Authenticate.getUser())
					|| PermissionService.isMember("MANAGE_EXECUTION_COURSES_ADV", (Degree) program,
							Authenticate.getUser());
		};
	};

	public static final AccessControlPredicate<Object> VIEW_FULL_STUDENT_CURRICULUM = new AccessControlPredicate<Object>() {
		@Override
		public boolean evaluate(final Object unused) {
			return AcademicAuthorizationGroup.get(AcademicOperationType.VIEW_FULL_STUDENT_CURRICULUM)
					.isMember(Authenticate.getUser())
					|| PermissionService.isMember("VIEW_FULL_STUDENT_CURRICULUM", Authenticate.getUser());
		};
	};
}
