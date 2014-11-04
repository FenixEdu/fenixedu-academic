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
package org.fenixedu.academic.domain.studentCurricularPlan.equivalencyPlan;

import java.util.Comparator;

import org.fenixedu.academic.domain.EquivalencePlanEntry;

public class EquivalencyPlanEntryWrapper {

    public static final Comparator<EquivalencyPlanEntryWrapper> COMPARATOR = new Comparator<EquivalencyPlanEntryWrapper>() {

        @Override
        public int compare(EquivalencyPlanEntryWrapper o1, EquivalencyPlanEntryWrapper o2) {
            return EquivalencePlanEntry.COMPARATOR.compare(o1.equivalencePlanEntry, o2.equivalencePlanEntry);
        }

    };

    private final EquivalencePlanEntry equivalencePlanEntry;

    private final boolean isRemovalEntry;

    public EquivalencyPlanEntryWrapper(final EquivalencePlanEntry equivalencePlanEntry, final boolean isRemovalEntry) {
        this.equivalencePlanEntry = equivalencePlanEntry;
        this.isRemovalEntry = isRemovalEntry;
    }

    public EquivalencePlanEntry getEquivalencePlanEntry() {
        return equivalencePlanEntry;
    }

    public boolean isRemovalEntry() {
        return isRemovalEntry;
    }

}
