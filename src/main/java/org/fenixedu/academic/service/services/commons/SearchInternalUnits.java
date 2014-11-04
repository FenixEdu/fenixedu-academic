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
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.PartyTypeEnum;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public class SearchInternalUnits extends AbstractSearchObjects<Unit> implements AutoCompleteProvider<Unit> {

    @Override
    public Collection<Unit> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        List<Unit> units = UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEPARTMENT);
        units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.DEGREE_UNIT));
        // units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SECTION));
        units.addAll(UnitUtils.readAllActiveUnitsByType(PartyTypeEnum.SCIENTIFIC_AREA));
        // units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.ASSOCIATED_LABORATORY));
        // units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.SCIENCE_INFRASTRUCTURE));
        // units.addAll(UnitUtils.readAllActiveUnitsByClassification(UnitClassification.RESEARCH_UNIT));
        for (Iterator<Unit> iterator = units.iterator(); iterator.hasNext();) {
            Unit unit = iterator.next();
            if (unit.getUnitName().getIsExternalUnit()) {
                iterator.remove();
            }
        }

        return super.process(units, value, maxCount, argsMap);
    }

}
