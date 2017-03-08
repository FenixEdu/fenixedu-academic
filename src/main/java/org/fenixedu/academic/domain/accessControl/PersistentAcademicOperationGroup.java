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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType.Scope;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.dml.runtime.Relation;

import com.google.common.collect.Sets;

public class PersistentAcademicOperationGroup extends PersistentAcademicOperationGroup_Base {
    protected PersistentAcademicOperationGroup(AcademicOperationType operation, Set<AcademicProgram> programs,
            Set<AdministrativeOffice> offices, Scope scope) {
        super();
        setOperation(operation);
        if (programs != null) {
            getProgramSet().addAll(programs);
        }
        if (offices != null) {
            getOfficeSet().addAll(offices);
        }
        setScope(scope);
    }

    @Override
    public Group toGroup() {
        return AcademicAuthorizationGroup.get(getOperation(), getProgramSet(), getOfficeSet(), getScope());
    }

    @Override
    protected Collection<Relation<?, ?>> getContextRelations() {
        Set<Relation<?, ?>> set = new HashSet<>();
        set.add(getRelationAcademicAuthorizationGroupAcademicPrograms());
        set.add(getRelationAcademicAuthorizationGroupAdministrativeOffices());
        set.addAll(super.getContextRelations());
        return set;
    }

    public static PersistentAcademicOperationGroup getInstance(final AcademicOperationType operation, final Scope scope) {
        return getInstance(operation, null, null, scope);
    }

    public static PersistentAcademicOperationGroup getInstance(final AcademicOperationType operation,
            final Set<AcademicProgram> programs, final Set<AdministrativeOffice> offices, final Scope scope) {
        return singleton(() -> select(operation, programs, offices, scope), () -> new PersistentAcademicOperationGroup(operation,
                programs, offices, scope));
    }

    private static Optional<PersistentAcademicOperationGroup> select(final AcademicOperationType operation,
            final Set<AcademicProgram> programs, final Set<AdministrativeOffice> offices, final Scope scope) {
        return filter(PersistentAcademicOperationGroup.class).filter(
                group -> Objects.equals(group.getOperation(), operation) && collectionEquals(group.getProgramSet(), programs)
                        && collectionEquals(group.getOfficeSet(), offices) && Objects.equals(group.getScope(), scope)).findAny();
    }

    private static boolean collectionEquals(Set<?> one, Set<?> another) {
        //This could be made more efficient once issue #187 is fixed.
        if (one == null) {
            one = Collections.emptySet();
        }
        if (another == null) {
            another = Collections.emptySet();
        }
        return Sets.symmetricDifference(one, another).isEmpty();
    }
}
