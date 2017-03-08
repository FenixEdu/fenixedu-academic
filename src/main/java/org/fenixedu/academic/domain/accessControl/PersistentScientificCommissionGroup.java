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
import java.util.Optional;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.dml.runtime.Relation;

public class PersistentScientificCommissionGroup extends PersistentScientificCommissionGroup_Base {
    protected PersistentScientificCommissionGroup(Degree degree) {
        super();
        setDegree(degree);
    }

    @Override
    public Group toGroup() {
        return ScientificCommissionGroup.get(getDegree());
    }

    @Override
    protected Collection<Relation<?, ?>> getContextRelations() {
        return Collections.singleton(getRelationPersistentScientificCommissionGroupDegree());
    }

    public static PersistentScientificCommissionGroup getInstance(Degree degree) {
        return singleton(() -> Optional.ofNullable(degree.getScientificCommissionGroup()),
                () -> new PersistentScientificCommissionGroup(degree));
    }
}
