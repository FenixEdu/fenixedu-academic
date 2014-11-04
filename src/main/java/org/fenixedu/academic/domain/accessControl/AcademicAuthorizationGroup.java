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

import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType.Scope;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("academic")
public class AcademicAuthorizationGroup extends FenixGroup {
    private static final long serialVersionUID = -3178215706386635516L;

    @GroupArgument("")
    private AcademicOperationType operation;

    @GroupArgument
    private Set<AcademicProgram> programs;

    @GroupArgument
    private Set<AdministrativeOffice> offices;

    @GroupArgument
    private Scope scope;

    private AcademicAuthorizationGroup() {
        super();
    }

    private AcademicAuthorizationGroup(AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, Scope scope) {
        this();
        this.operation = operation;
        this.programs = programs;
        this.offices = offices;
        this.scope = scope;
    }

    public static AcademicAuthorizationGroup get() {
        return new AcademicAuthorizationGroup(null, null, null, null);
    }

    public static AcademicAuthorizationGroup get(Scope scope) {
        return new AcademicAuthorizationGroup(null, null, null, scope);
    }

    public static AcademicAuthorizationGroup get(AcademicOperationType operation) {
        return new AcademicAuthorizationGroup(operation, null, null, null);
    }

    public static AcademicAuthorizationGroup get(AcademicOperationType operation, AcademicProgram program) {
        return new AcademicAuthorizationGroup(operation, Collections.singleton(program), null, null);
    }

    public static AcademicAuthorizationGroup get(AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, Scope scope) {
        return new AcademicAuthorizationGroup(operation, programs, offices, scope);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { operation.getLocalizedName() };
    }

    @Override
    public Set<User> getMembers() {
        if (scope != null) {
            return AcademicAccessRule.getMembers(r -> scope.contains(r.getOperation()));
        }
        return AcademicAccessRule.getMembers(operation, programs, offices);
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        if (scope != null) {
            return AcademicAccessRule.getMembers(r -> scope.contains(r.getOperation()), when);
        }
        return AcademicAccessRule.getMembers(operation, programs, offices, when);
    }

    @Override
    public boolean isMember(User user) {
        if (scope != null) {
            return AcademicAccessRule.isMember(user, r -> scope.contains(r.getOperation()));
        }
        return AcademicAccessRule.isMember(user, operation, programs, offices);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        if (scope != null) {
            return AcademicAccessRule.isMember(user, r -> scope.contains(r.getOperation()), when);
        }
        return AcademicAccessRule.isMember(user, operation, programs, offices, when);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentAcademicOperationGroup.getInstance(operation, programs, offices, scope);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AcademicAuthorizationGroup) {
            AcademicAuthorizationGroup other = (AcademicAuthorizationGroup) object;
            return Objects.equal(operation, other.operation) && Objects.equal(programs, other.programs)
                    && Objects.equal(offices, other.offices) && Objects.equal(scope, other.scope);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(operation, programs, offices, scope);
    }
}
