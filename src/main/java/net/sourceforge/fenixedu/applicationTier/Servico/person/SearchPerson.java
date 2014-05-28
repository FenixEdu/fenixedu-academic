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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.accounting.PaymentCode;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.StringNormalizer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.CollectionPager;

public class SearchPerson implements Serializable {

    public static class SearchParameters implements Serializable {

        private String email, username, documentIdNumber;

        private IDDocumentType idDocumentType;

        private String name;

        private String[] nameWords;

        private Role role;

        private Degree degree;

        private Department department;

        private DegreeType degreeType;

        private Boolean activePersons;

        private Boolean externalPersons;

        private Boolean showOnlySearchableResearchers;

        private Integer studentNumber;

        private Integer mechanoGraphicalNumber;

        private String paymentCode;

        public SearchParameters() {
        }

        public SearchParameters(String name, String email, String username, String documentIdNumber, String idDocumentType,
                String roleType, String degreeTypeString, String degreeId, String departmentId, Boolean activePersons,
                Integer studentNumber, Boolean externalPersons, Boolean showOnlySearchableResearchers) {

            this(name, email, username, documentIdNumber, idDocumentType, roleType, degreeTypeString, degreeId, departmentId,
                    activePersons, studentNumber, externalPersons, (String) null);
            setShowOnlySearchableResearchers(showOnlySearchableResearchers);
        }

        public SearchParameters(String name, String email, String username, String documentIdNumber, String idDocumentType,
                String roleType, String degreeTypeString, String degreeId, String departmentId, Boolean activePersons,
                Integer studentNumber, Boolean externalPersons, String paymentCode) {
            this();

            setActivePersons(activePersons);
            setName(name);
            setEmail(email);
            setUsername(username);
            setDocumentIdNumber(documentIdNumber);
            if (!StringUtils.isEmpty(idDocumentType)) {
                setIdDocumentType(IDDocumentType.valueOf(idDocumentType));
            }
            setStudentNumber(studentNumber);
            setExternalPersons(externalPersons);
            setPaymentCode(paymentCode);

            if (roleType != null && roleType.length() > 0) {
                role = Role.getRoleByRoleType(RoleType.valueOf(roleType));
            }

            if (!StringUtils.isEmpty(degreeId)) {
                degree = FenixFramework.getDomainObject(degreeId);
            }

            if (degreeTypeString != null && degreeTypeString.length() > 0) {
                degreeType = DegreeType.valueOf(degreeTypeString);
            }

            if (!StringUtils.isEmpty(departmentId)) {
                department = FenixFramework.getDomainObject(departmentId);
            }
        }

        public boolean emptyParameters() {
            return StringUtils.isEmpty(this.email) && StringUtils.isEmpty(this.username)
                    && StringUtils.isEmpty(this.documentIdNumber) && this.role == null && this.degree == null
                    && this.department == null && this.degreeType == null && this.nameWords == null && this.studentNumber == null
                    && this.mechanoGraphicalNumber == null && this.idDocumentType == null
                    && this.showOnlySearchableResearchers == null && StringUtils.isEmpty(this.getPaymentCode());
        }

        private static String[] getNameWords(String name) {
            String[] nameWords = null;
            if (name != null && !StringUtils.isEmpty(name.trim())) {
                nameWords = StringNormalizer.normalize(name).trim().split(" ");
            }
            return nameWords;
        }

        public Degree getDegree() {
            return degree;
        }

        public DegreeType getDegreeType() {
            return degreeType;
        }

        public Department getDepartment() {
            return department;
        }

        public String getDocumentIdNumber() {
            return documentIdNumber;
        }

        public IDDocumentType getIdDocumentType() {
            return idDocumentType;
        }

        public String getEmail() {
            return email;
        }

        public String[] getNameWords() {
            return nameWords;
        }

        public String getName() {
            return name;
        }

        public Role getRole() {
            return role;
        }

        public String getUsername() {
            return username;
        }

        public Boolean getActivePersons() {
            return activePersons;
        }

        public Integer getStudentNumber() {
            return studentNumber;
        }

        public Boolean getExternalPersons() {
            return externalPersons;
        }

        public Integer getMechanoGraphicalNumber() {
            return mechanoGraphicalNumber;
        }

        public void setEmail(String email) {
            this.email = (email != null && !email.equals("")) ? StringNormalizer.normalize(email.trim()) : null;
        }

        public void setUsername(String username) {
            this.username = (username != null && !username.equals("")) ? StringNormalizer.normalize(username.trim()) : null;
        }

        public void setDocumentIdNumber(String documentIdNumber) {
            this.documentIdNumber = (documentIdNumber != null && !documentIdNumber.equals("")) ? documentIdNumber.trim() : null;
        }

        public void setIdDocumentType(IDDocumentType idDocumentType) {
            this.idDocumentType = idDocumentType;
        }

        public void setName(String name) {
            this.name = (name != null && !name.equals("")) ? name : null;
            this.nameWords = (name != null && !name.equals("")) ? getNameWords(name) : null;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public void setDegree(Degree degree) {
            this.degree = degree;
        }

        public void setDepartment(Department department) {
            this.department = department;
        }

        public void setDegreeType(DegreeType degreeType) {
            this.degreeType = degreeType;
        }

        public void setActivePersons(Boolean activePersons) {
            this.activePersons = activePersons;
        }

        public void setExternalPersons(Boolean externalPersons) {
            this.externalPersons = externalPersons;
        }

        public void setStudentNumber(Integer studentNumber) {
            this.studentNumber = studentNumber;
        }

        public void setMechanoGraphicalNumber(Integer mechanoGraphicalNumber) {
            this.mechanoGraphicalNumber = mechanoGraphicalNumber;
        }

        public Boolean getShowOnlySearchableResearchers() {
            return showOnlySearchableResearchers;
        }

        public void setShowOnlySearchableResearchers(Boolean showOnlySearchableResearchers) {
            this.showOnlySearchableResearchers = showOnlySearchableResearchers;
        }

        public String getPaymentCode() {
            return this.paymentCode;
        }

        public void setPaymentCode(final String paymentCode) {
            this.paymentCode = paymentCode;
        }
    }

    public CollectionPager<Person> run(SearchParameters searchParameters, Predicate predicate) {

        if (searchParameters.emptyParameters()) {
            return new CollectionPager<Person>(new ArrayList<Person>(), 25);
        }

        final Collection<Person> persons;

        if (searchParameters.getUsername() != null && searchParameters.getUsername().length() > 0) {

            final Person person = Person.readPersonByUsername(searchParameters.getUsername());
            persons = new ArrayList<Person>();
            if (person != null) {
                persons.add(person);
            }

        } else if (searchParameters.getDocumentIdNumber() != null && searchParameters.getDocumentIdNumber().length() > 0) {
            persons = Person.findPersonByDocumentID(searchParameters.getDocumentIdNumber());

        } else if (searchParameters.getStudentNumber() != null) {

            final Student student = Student.readStudentByNumber(searchParameters.getStudentNumber());
            persons = new ArrayList<Person>();
            if (student != null) {
                persons.add(student.getPerson());
            }

        } else if (searchParameters.getEmail() != null && searchParameters.getEmail().length() > 0) {

            final Person person = Person.readPersonByEmailAddress(searchParameters.getEmail());
            persons = new ArrayList<Person>();
            if (person != null) {
                persons.add(person);
            }

        } else if (searchParameters.getMechanoGraphicalNumber() != null) {
            final Employee employee = Employee.readByNumber(searchParameters.getMechanoGraphicalNumber());
            final Student student = Student.readStudentByNumber(searchParameters.getMechanoGraphicalNumber());
            persons = new TreeSet<Person>();
            if (employee != null) {
                persons.add(employee.getPerson());
            }
            if (student != null) {
                persons.add(student.getPerson());
            }

        } else if (searchParameters.getName() != null) {

            persons = new ArrayList<Person>();

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

        } else if (!StringUtils.isEmpty(searchParameters.getPaymentCode())) {
            persons = new ArrayList<Person>();

            PaymentCode paymentCode = PaymentCode.readByCode(searchParameters.getPaymentCode());

            if (paymentCode != null && paymentCode.getPerson() != null) {
                persons.add(paymentCode.getPerson());
            }
        } else {
            persons = new ArrayList<Person>(0);
        }

        TreeSet<Person> result = new TreeSet<Person>(Person.COMPARATOR_BY_NAME_AND_ID);
        result.addAll(CollectionUtils.select(persons, predicate));
        return new CollectionPager<Person>(result, 25);
    }

    public static class SearchPersonPredicate implements Predicate {

        private final SearchParameters searchParameters;

        public SearchPersonPredicate(SearchParameters searchParameters) {
            this.searchParameters = searchParameters;
        }

        @Override
        public boolean evaluate(Object arg0) {
            Person person = (Person) arg0;

            return verifyActiveState(searchParameters.getActivePersons(), person)
                    && verifySimpleParameter(person.getDocumentIdNumber(), searchParameters.getDocumentIdNumber())
                    && verifyUsernameEquality(searchParameters.getUsername(), person)
                    && verifyNameEquality(searchParameters.getNameWords(), person)
                    && verifyAnyEmailAddress(searchParameters.getEmail(), person)
                    && verifyDegreeType(searchParameters.getDegree(), searchParameters.getDegreeType(), person)
                    && verifyStudentNumber(searchParameters.getStudentNumber(), person)
                    && verifyMechanoGraphicalNumber(searchParameters.getMechanoGraphicalNumber(), person)
                    && verifyPaymentCodes(searchParameters.getPaymentCode(), person)
                    && verifyShowOnlySearchableResearchers(searchParameters.showOnlySearchableResearchers, person);
        }

        protected boolean verifyAnyEmailAddress(final String email, final Person person) {
            return email == null || email.trim().isEmpty() || person.hasEmailAddress(email);
        }

        protected boolean verifyIdDocumentType(IDDocumentType idDocumentType, Person person) {
            return (idDocumentType == null || person.getIdDocumentType() == idDocumentType);
        }

        protected boolean verifyStudentNumber(Integer studentNumber, Person person) {
            return (studentNumber == null || (person.hasStudent() && person.getStudent().getNumber().equals(studentNumber)));
        }

        protected boolean verifyMechanoGraphicalNumber(Integer mechanoGraphicalNumber, Person person) {
            return (mechanoGraphicalNumber == null
                    || (person.hasStudent() && person.getStudent().getNumber().equals(mechanoGraphicalNumber)) || (person
                    .hasEmployee() && person.getEmployee().getEmployeeNumber().equals(mechanoGraphicalNumber)));
        }

        protected boolean verifyActiveState(Boolean activePersons, Person person) {
            return (activePersons == null || person.hasRole(RoleType.PERSON).equals(activePersons));
        }

        protected boolean verifyUsernameEquality(String usernameToSearch, Person person) {
            if (usernameToSearch == null) {
                return true;
            }
            String normalizedUsername = StringNormalizer.normalize(usernameToSearch.trim());
            String normalizedAlias = StringNormalizer.normalize(person.getUsername());
            return normalizedAlias.indexOf(normalizedUsername) != 1;
        }

        protected boolean verifyDegreeType(final Degree degree, final DegreeType degreeType, final Person person) {
            return degreeType == null || verifyDegreeType(degree, person.getStudentByType(degreeType));
        }

        private boolean verifyDegreeType(final Degree degree, final Registration registrationByType) {
            return registrationByType != null && (degree == null || verifyDegree(degree, registrationByType));
        }

        private boolean verifyDegree(final Degree degree, final Registration registrationByType) {
            final StudentCurricularPlan studentCurricularPlan = registrationByType.getActiveStudentCurricularPlan();
            return (studentCurricularPlan != null && degree == studentCurricularPlan.getDegreeCurricularPlan().getDegree());
        }

        protected boolean verifySimpleParameter(String parameter, String searchParameter) {
            return (searchParameter == null) || (parameter != null && simpleNnormalizeAndCompare(parameter, searchParameter));
        }

        private boolean verifyParameter(String parameter, String searchParameter) {
            return (searchParameter == null) || (parameter != null && normalizeAndCompare(parameter, searchParameter));
        }

        private boolean simpleNnormalizeAndCompare(String parameter, String searchParameter) {
            String personParameter = parameter;
            return (personParameter.indexOf(searchParameter) == -1) ? false : true;
        }

        private boolean normalizeAndCompare(String parameter, String searchParameter) {
            String personParameter = StringNormalizer.normalize(parameter.trim());
            return (personParameter.indexOf(searchParameter) == -1) ? false : true;
        }

        protected static boolean verifyNameEquality(String[] nameWords, Person person) {
            return person.verifyNameEquality(nameWords);
        }

        protected static boolean verifyShowOnlySearchableResearchers(Boolean showOnlySearchableResearchers, final Person person) {
            return showOnlySearchableResearchers == null || showOnlySearchableResearchers && person.hasResearcher()
                    && person.getResearcher().getAllowsToBeSearched();
        }

        protected static boolean verifyPaymentCodes(String paymentCode, final Person person) {
            return StringUtils.isEmpty(paymentCode) || person.getPaymentCodeBy(paymentCode) != null;
        }

        public SearchParameters getSearchParameters() {
            return searchParameters;
        }
    }

    // Service Invokers migrated from Berserk

    private static final SearchPerson serviceInstance = new SearchPerson();

    @Atomic
    public static CollectionPager<Person> runSearchPerson(SearchParameters searchParameters, Predicate predicate) {
        return serviceInstance.run(searchParameters, predicate);
    }

}
