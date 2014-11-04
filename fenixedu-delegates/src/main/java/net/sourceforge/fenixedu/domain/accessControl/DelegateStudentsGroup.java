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
package org.fenixedu.academic.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.Coordinator;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.FunctionType;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.domain.student.Delegate;
import org.fenixedu.academic.domain.student.Student;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("delegateStudents")
public class DelegateStudentsGroup extends FenixGroup {
    private static final long serialVersionUID = -922683188265327080L;

    @GroupArgument
    private PersonFunction delegateFunction;

    @GroupArgument
    private FunctionType type;

    private DelegateStudentsGroup() {
        super();
    }

    private DelegateStudentsGroup(PersonFunction delegateFunction, FunctionType type) {
        this();
        this.delegateFunction = delegateFunction;
        this.type = type;
    }

    public static DelegateStudentsGroup get(PersonFunction delegateFunction, FunctionType type) {
        return new DelegateStudentsGroup(delegateFunction, type);
    }

    @Override
    public String getPresentationNameBundle() {
        return "resources.DelegateResources";
    }

    @Override
    public String getPresentationNameKey() {
        return "label." + getClass().getSimpleName() + "." + type.getName()
                + (delegateFunction.getPerson().getStudent() != null ? "" : ".coordinator");
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();

        Person delegate = delegateFunction.getPerson();
        if (delegate.getStudent() != null) {
            for (Student student : Delegate.getStudentsResponsibleForGivenFunctionType(delegate.getStudent(), type,
                    getExecutionYear())) {
                User user = student.getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        } else {
            for (Coordinator coordinator : delegate.getCoordinatorsSet()) {
                final Degree degree = coordinator.getExecutionDegree().getDegree();
                for (Student student : degree.getAllStudents()) {
                    User user = student.getPerson().getUser();
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        }

        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        return user != null && getMembers().contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public ExecutionYear getExecutionYear() {
        return ExecutionYear.getExecutionYearByDate(delegateFunction.getBeginDate());
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentDelegateStudentsGroup.getInstance(delegateFunction, type);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DelegateStudentsGroup) {
            DelegateStudentsGroup other = (DelegateStudentsGroup) object;
            return Objects.equal(delegateFunction, other.delegateFunction) && Objects.equal(type, other.type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(delegateFunction, type);
    }

}
