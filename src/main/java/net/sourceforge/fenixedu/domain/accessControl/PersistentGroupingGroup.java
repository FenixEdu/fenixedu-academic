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
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Optional;

import net.sourceforge.fenixedu.domain.Grouping;

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
    protected void gc() {
        setGrouping(null);
        super.gc();
    }

    public static PersistentGroupingGroup getInstance(Grouping grouping) {
        return singleton(() -> Optional.ofNullable(grouping.getGroupingGroup()), () -> new PersistentGroupingGroup(grouping));
    }
}
