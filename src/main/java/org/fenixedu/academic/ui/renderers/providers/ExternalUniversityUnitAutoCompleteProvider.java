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
package org.fenixedu.academic.ui.renderers.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Predicate;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.organizationalStructure.UnitName;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.joda.time.YearMonthDay;

public class ExternalUniversityUnitAutoCompleteProvider implements AutoCompleteProvider<Unit> {

    @Override
    public Collection<Unit> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final List<Unit> result = new ArrayList<Unit>();
        final YearMonthDay today = new YearMonthDay();
        final Predicate predicate = new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                final UnitName unitName = (UnitName) arg0;
                final Unit unit = unitName.getUnit();
                return unit.isUniversityUnit() && unit.isActive(today);
            }
        };
        for (final UnitName unitName : UnitName.findExternalUnit(value, maxCount, predicate)) {
            final Unit unit = unitName.getUnit();
            result.add(unit);
        }
        return result;
    }

}
