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
package org.fenixedu.academic.domain.accessControl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.student.registrationStates.RegistrationStateType;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("alumni")
public class AlumniGroup extends FenixGroup {
    private static final long serialVersionUID = 5431112068108722868L;

    @GroupArgument("")
    private Degree degree;

    @GroupArgument
    private Boolean registered;

    private AlumniGroup() {
        super();
    }

    private AlumniGroup(Degree degree) {
        this();
        this.degree = degree;
    }

    private AlumniGroup(Degree degree, Boolean registered) {
        this(degree);
        this.registered = registered;
    }

    private AlumniGroup(Boolean registered) {
        this();
        this.registered = registered;
    }

    public static AlumniGroup get() {
        return new AlumniGroup();
    }

    public static AlumniGroup get(Degree degree) {
        return new AlumniGroup(degree);
    }

    public static AlumniGroup get(Degree degree, Boolean registered) {
        return new AlumniGroup(degree, registered);
    }

    public static AlumniGroup get(Boolean registered) {
        return new AlumniGroup(registered);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { degree == null ? "" : BundleUtil.getString(Bundle.GROUP, "label.name.connector.default")
                + degree.getPresentationName() };
    }

    @Override
    public Set<User> getMembers() {
        return getMembers(DateTime.now());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        Set<Student> students = Bennu.getInstance().getStudentsSet();
        
        if (registered == null) {
            students = students.stream()
                    .filter(student -> student.getAlumni() != null
                            || hasConcludedRegistration(student)
                            || isAlumni(student))
                    .collect(Collectors.toSet());
        } else if (registered) {
            students = students.stream().filter(student -> student.getAlumni() != null && isAlumni(student))
                    .collect(Collectors.toSet());
        } else if (!registered) {
            students = students.stream()
                    .filter(student -> student.getAlumni() == null
                            && hasConcludedRegistration(student)
                            && !isAlumni(student))
                    .collect(Collectors.toSet());
        }
        
        // If degree is set, filter out the alumni that didn't conclude the specified degree
        if (degree != null) {
            students = students.stream().filter(student -> isDegreeConcluded(student)).collect(Collectors.toSet());
        }
        
        // Join students in a collection of users
        return students.stream().map(student -> student.getPerson().getUser()).collect(Collectors.toSet());
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user, DateTime.now());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return user != null
                && user.getPerson() != null
                && user.getPerson().getStudent() != null
                && (degree == null || isDegreeConcluded(user.getPerson().getStudent())) // If degree is set, check for its conclusion
                && (registered != null // If registered is not set, allow any
                        || (user.getPerson().getStudent().getAlumni() != null
                                || hasConcludedRegistration(user.getPerson().getStudent())
                                || isAlumni(user.getPerson().getStudent())))
                && ((registered != null && !registered) // If registered is set to true, only allow registered Alumni
                        || (user.getPerson().getStudent().getAlumni() != null && isAlumni(user.getPerson().getStudent())))
                && ((registered != null && registered) // If registered is set to false, only allow non registered Alumni
                        || (user.getPerson().getStudent().getAlumni() == null
                                && hasConcludedRegistration(user.getPerson().getStudent())
                                && !isAlumni(user.getPerson().getStudent())));
    }

    private boolean hasConcludedRegistration(Student student) {
        return student.hasAnyRegistrationInState(RegistrationStateType.CONCLUDED)
                || student.hasAnyRegistrationInState(RegistrationStateType.STUDYPLANCONCLUDED);
    }
    
    private boolean isDegreeConcluded(Student student) {
        for (Registration registration : student.getRegistrationsFor(degree)) {
            if (registration.isRegistrationConclusionProcessed()) {
                return true;
            }
        }
        return false;
    }

    private boolean isAlumni(Student student) {
        return student
                .getRegistrationsSet()
                .stream()
                .anyMatch(registration -> ProgramConclusion
                        .conclusionsFor(registration)
                        .filter(ProgramConclusion::isAlumniProvider)
                        .anyMatch(conclusion -> conclusion.groupFor(registration)
                                .map(CurriculumGroup::isConclusionProcessed).orElse(false)));

    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentAlumniGroup.getInstance(degree, registered);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AlumniGroup) {
            return Objects.equal(degree, ((AlumniGroup) object).degree);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(degree);
    }
}
