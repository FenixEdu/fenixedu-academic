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
package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.fenix.tools.util.CollectionPager;

public class SearchPersonMatchingAnyParameter extends SearchPerson {

    public static CollectionPager<Person> run(String name, String email, String username, String documentIdNumber,
            IDDocumentType idDocumentType, String roleType, String degreeTypeString, String degreeId, String departmentId,
            Boolean activePersons, Integer studentNumber, Boolean externalPersons) {

        SearchParameters searchParameters =
                new SearchPersonMatchingAnyParameter.SearchParameters(name, email, username, documentIdNumber,
                        idDocumentType == null ? null : idDocumentType.name(), roleType, degreeTypeString, degreeId,
                        departmentId, activePersons, studentNumber, externalPersons, (String) null);

        if (searchParameters.emptyParameters()) {
            return new CollectionPager<Person>(new HashSet<Person>(), 25);
        }

        final Collection<Person> persons = new HashSet<Person>();

        if (searchParameters.getUsername() != null && searchParameters.getUsername().length() > 0) {

            final Person person = Person.readPersonByUsername(searchParameters.getUsername());
            if (person != null) {
                persons.add(person);
            }

        } else {

            if (searchParameters.getDocumentIdNumber() != null && searchParameters.getDocumentIdNumber().length() > 0) {
                persons.addAll(Person.findPersonByDocumentID(searchParameters.getDocumentIdNumber()));
            }
            if (searchParameters.getStudentNumber() != null) {
                final Student student = Student.readStudentByNumber(searchParameters.getStudentNumber());
                if (student != null) {
                    persons.add(student.getPerson());
                }

            }
            if (searchParameters.getEmail() != null && searchParameters.getEmail().length() > 0) {
                final Person person = Person.readPersonByEmailAddress(searchParameters.getEmail());
                if (person != null) {
                    persons.add(person);
                }

            }
            if (searchParameters.getMechanoGraphicalNumber() != null) {
                final Employee employee = Employee.readByNumber(searchParameters.getMechanoGraphicalNumber());
                final Student student = Student.readStudentByNumber(searchParameters.getMechanoGraphicalNumber());
                if (employee != null) {
                    persons.add(employee.getPerson());
                }
                if (student != null) {
                    persons.add(student.getPerson());
                }

            }
            if (searchParameters.getName() != null) {
                if (searchParameters.getExternalPersons() == null || !searchParameters.getExternalPersons()) {
                    persons.addAll(Person.findInternalPerson(searchParameters.getName()));
                    final Role roleBd = searchParameters.getRole();
                    if (roleBd != null) {
                        for (final Iterator<Person> peopleIterator = persons.iterator(); peopleIterator.hasNext();) {
                            final Person person = peopleIterator.next();
                            if (!person.hasPersonRoles(roleBd)) {
                                peopleIterator.remove();
                            }
                        }
                    }
                    final Department department = searchParameters.getDepartment();
                    if (department != null) {
                        for (final Iterator<Person> peopleIterator = persons.iterator(); peopleIterator.hasNext();) {
                            final Person person = peopleIterator.next();
                            final Teacher teacher = person.getTeacher();
                            if (teacher == null || teacher.getCurrentWorkingDepartment() != department) {
                                peopleIterator.remove();
                            }
                        }
                    }
                }

                if (searchParameters.getExternalPersons() == null || searchParameters.getExternalPersons()) {
                    persons.addAll(Person.findExternalPerson(searchParameters.getName()));
                }
            }
        }

        SearchPersonPredicate predicate = new SearchPersonMatchingAnyParameterPredicate(searchParameters);
        TreeSet<Person> result = new TreeSet<Person>(Person.COMPARATOR_BY_NAME_AND_ID);
        result.addAll(CollectionUtils.select(persons, predicate));
        return new CollectionPager<Person>(result, 25);
    }

    public static class SearchPersonMatchingAnyParameterPredicate extends SearchPersonPredicate {

        public SearchPersonMatchingAnyParameterPredicate(SearchParameters searchParameters) {
            super(searchParameters);
        }

        @Override
        public boolean evaluate(Object arg0) {
            Person person = (Person) arg0;
            return verifyActiveState(getSearchParameters().getActivePersons(), person)
                    || verifySimpleParameter(person.getDocumentIdNumber(), getSearchParameters().getDocumentIdNumber())
                    || verifyIdDocumentType(getSearchParameters().getIdDocumentType(), person)
                    || verifyUsernameEquality(getSearchParameters().getUsername(), person)
                    || verifyNameEquality(getSearchParameters().getNameWords(), person)
                    || verifyAnyEmailAddress(getSearchParameters().getEmail(), person)
                    || verifyDegreeType(getSearchParameters().getDegree(), getSearchParameters().getDegreeType(), person)
                    || verifyStudentNumber(getSearchParameters().getStudentNumber(), person)
                    || verifyMechanoGraphicalNumber(getSearchParameters().getMechanoGraphicalNumber(), person)
                    || verifyShowOnlySearchableResearchers(getSearchParameters().getShowOnlySearchableResearchers(), person);
        }
    }

}
