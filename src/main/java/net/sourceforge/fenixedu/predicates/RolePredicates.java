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
package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

public class RolePredicates {

    public static class PartyContactPredicate implements AccessControlPredicate<PartyContact> {

        private static boolean isSelfPerson(Party person) {
            final User userView = Authenticate.getUser();
            return userView.getPerson() != null && userView.getPerson().equals(person);
        }

        public static boolean eval(Person contactPerson) {
            if (hasRole(RoleType.OPERATOR) || hasRole(RoleType.MANAGER) || isSelfPerson(contactPerson)
                    || hasRole(RoleType.PERSONNEL_SECTION)) {
                return true;
            }

            if (contactPerson.hasStudent() && !contactPerson.hasRole(RoleType.GRANT_OWNER)
                    && !contactPerson.hasRole(RoleType.EMPLOYEE)) {
                return AcademicAuthorizationGroup.get(AcademicOperationType.EDIT_STUDENT_PERSONAL_DATA).isMember(Authenticate.getUser());
            }

            return false;
        }

        @Override
        public boolean evaluate(PartyContact contact) {
            final Person contactPerson = (Person) contact.getParty();
            return eval(contactPerson);
        };
    }

    public static final PartyContactPredicate PARTY_CONTACT_PREDICATE = new PartyContactPredicate();

    public static final AccessControlPredicate<PartyContactBean> PARTY_CONTACT_BEAN_PREDICATE =
            new AccessControlPredicate<PartyContactBean>() {

                @Override
                public boolean evaluate(PartyContactBean contactBean) {
                    return PartyContactPredicate.eval((Person) contactBean.getParty());
                }
            };

    public static final AccessControlPredicate<Object> ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE);
                };
            };

    public static final AccessControlPredicate<Object> ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE_AND_GRI =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE) || hasRole(RoleType.INTERNATIONAL_RELATION_OFFICE);
                };
            };

    public static final AccessControlPredicate<Object> BOLONHA_MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.BOLONHA_MANAGER);
        };
    };

    public static final AccessControlPredicate<Object> COORDINATOR_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.COORDINATOR);
        };
    };

    public static final AccessControlPredicate<Object> CREDITS_MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.CREDITS_MANAGER);
        };
    };

    public static final AccessControlPredicate<Object> DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return hasRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE);
                };
            };

    public static final AccessControlPredicate<Object> DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return hasRole(RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER);
                };
            };

    public static final AccessControlPredicate<Object> DELEGATE_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.DELEGATE);
        };
    };

    public static final AccessControlPredicate<Object> DEPARTMENT_ADMINISTRATIVE_OFFICE_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return hasRole(RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
                };
            };

    public static final AccessControlPredicate<Object> DEPARTMENT_CREDITS_MANAGER_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return hasRole(RoleType.DEPARTMENT_CREDITS_MANAGER);
                };
            };

    public static final AccessControlPredicate<Object> DEPARTMENT_MEMBER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.DEPARTMENT_MEMBER);
        };
    };

    public static final AccessControlPredicate<Object> DIRECTIVE_COUNCIL_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object object) {
            return hasRole(RoleType.DIRECTIVE_COUNCIL);

        };
    };

    public static final AccessControlPredicate<Object> EMPLOYEE_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.EMPLOYEE);
        };
    };

    public static final AccessControlPredicate<Object> GEP_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.GEP);
        };
    };

    public static final AccessControlPredicate<Object> LIBRARY_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.LIBRARY);
        };
    };

    public static final AccessControlPredicate<Object> MANAGER_OR_ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return MANAGER_PREDICATE.evaluate(domainObject)
                            || ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE.evaluate(domainObject);
                };
            };

    public static final AccessControlPredicate<Object> MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.MANAGER);
        };
    };

    public static final AccessControlPredicate<Object> MANAGER_OR_OPERATOR_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return MANAGER_PREDICATE.evaluate(domainObject) || OPERATOR_PREDICATE.evaluate(domainObject);
        };
    };

    public static final AccessControlPredicate<Object> MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return hasRole(RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE);
                };
            };

    public static final AccessControlPredicate<Object> OPERATOR_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.OPERATOR);
        };
    };

    public static final AccessControlPredicate<Object> PEDAGOGICAL_COUNCIL_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.PEDAGOGICAL_COUNCIL);
        };
    };

    public static final AccessControlPredicate<Object> PERSON_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.PERSON);
        };
    };

    public static final AccessControlPredicate<Object> PERSONNEL_SECTION_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.PERSONNEL_SECTION);
        };
    };

    public static final AccessControlPredicate<Object> RESOURCE_ALLOCATION_MANAGER_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER);
                };
            };

    public static final AccessControlPredicate<Object> RESOURCE_MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.RESOURCE_MANAGER);
        };
    };

    public static final AccessControlPredicate<Object> SCIENTIFIC_COUNCIL_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.SCIENTIFIC_COUNCIL);
        };
    };

    public static final AccessControlPredicate<Object> SPACE_MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.SPACE_MANAGER);
        };
    };

    public static final AccessControlPredicate<Object> STUDENT_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.STUDENT);
        };
    };

    public static final AccessControlPredicate<Object> TEACHER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return isTeacher();
        };
    };

    public static final AccessControlPredicate<Object> STUDENT_AND_TEACHER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return isTeacher() || hasRole(RoleType.STUDENT);
        };
    };

    public static final AccessControlPredicate<Object> WEBSITE_MANAGER_PREDICATE = new AccessControlPredicate<Object>() {
        @Override
        public boolean evaluate(Object domainObject) {
            return hasRole(RoleType.WEBSITE_MANAGER);
        };
    };

    public static final AccessControlPredicate<Object> BOLOGNA_OR_DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER_OR_MANAGER_OR_OPERATOR_PREDICATE =
            new AccessControlPredicate<Object>() {
                @Override
                public boolean evaluate(Object domainObject) {
                    return BOLONHA_MANAGER_PREDICATE.evaluate(domainObject)
                            || DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER_PREDICATE.evaluate(domainObject)
                            || MANAGER_PREDICATE.evaluate(domainObject) || OPERATOR_PREDICATE.evaluate(domainObject);
                };
            };

    private static boolean hasRole(final RoleType roleType) {
        final Person person = AccessControl.getPerson();
        return person != null && person.hasRole(roleType);
    }

    private static boolean isTeacher() {
        return hasRole(RoleType.TEACHER) || hasActiveProfessorship();
    }

    private static boolean hasActiveProfessorship() {
        final ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
        final Person person = AccessControl.getPerson();
        for (final Professorship professorship : person.getProfessorshipsSet()) {
            if (professorship.getExecutionCourse().getExecutionPeriod() == executionSemester) {
                return true;
            }
        }
        return false;
    }

}
