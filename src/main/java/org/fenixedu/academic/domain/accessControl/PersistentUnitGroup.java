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

import java.util.Objects;

import org.fenixedu.academic.domain.organizationalStructure.AccountabilityTypeEnum;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.groups.Group;

public class PersistentUnitGroup extends PersistentUnitGroup_Base {
    protected PersistentUnitGroup(Unit unit, AccountabilityTypeEnum relationType, boolean includeSubUnits) {
        super();
        setUnit(unit);
        setRelationType(relationType);
        setIncludeSubUnits(includeSubUnits);
    }

    @Override
    public Group toGroup() {
        return UnitGroup.get(getUnit(), getRelationType(), getIncludeSubUnits());
    }

    @Override
    protected void gc() {
        setUnit(null);
        super.gc();
    }

    public static PersistentUnitGroup getInstance(final Unit unit, final AccountabilityTypeEnum relationType,
            final Boolean includeSubUnits) {
        return singleton(
                () -> unit
                        .getUnitGroupSet()
                        .stream()
                        .filter(group -> Objects.equals(group.getRelationType(), relationType)
                                && Objects.equals(group.getIncludeSubUnits(), includeSubUnits)).findAny(),
                () -> new PersistentUnitGroup(unit, relationType, includeSubUnits));
    }
}
