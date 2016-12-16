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

import org.fenixedu.academic.domain.Grouping;

import pt.ist.fenixframework.dml.runtime.Relation;

public class PersistentGroupingGroup extends PersistentGroupingGroup_Base {
    protected PersistentGroupingGroup(Grouping grouping) {
        super();
        setGrouping(grouping);
    }

    @Override
    public GroupingGroup toGroup() {
        return GroupingGroup.get(getGrouping());
    }

    @Override
    protected Collection<Relation<?, ?>> getContextRelations() {
        return Collections.singleton(getRelationPersistentGroupingGroupGrouping());
    }

    public static PersistentGroupingGroup getInstance(Grouping grouping) {
        return singleton(() -> Optional.ofNullable(grouping.getGroupingGroup()), () -> new PersistentGroupingGroup(grouping));
    }
}
