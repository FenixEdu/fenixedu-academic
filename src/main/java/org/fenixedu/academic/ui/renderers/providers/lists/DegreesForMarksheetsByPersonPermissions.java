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

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DegreesForMarksheetsByPersonPermissions implements DataProvider {

	@Override
	public Object provide(Object source, Object currentValue) {

		Person person = AccessControl.getPerson();
		List<Degree> degreesForOperation = AcademicAccessRule
				.getDegreesAccessibleToFunction(AcademicOperationType.MANAGE_MARKSHEETS, person.getUser())
				.collect(Collectors.toList());
		degreesForOperation.addAll(PermissionService.getDegrees("MANAGE_MARKSHEETS", person.getUser()));
		degreesForOperation.stream().sorted(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID)
				.collect(Collectors.toList());

		if (!degreesForOperation.isEmpty()) {
			return degreesForOperation;
		}

		SortedSet<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
		result.addAll(Bennu.getInstance().getDegreesSet());
		return result;
	}

	@Override
	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

}
