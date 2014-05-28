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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

public class ExternalUnitSearchService implements AutoCompleteProvider<Unit> {

    @Override
    public Collection<Unit> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final List<Unit> result = new ArrayList<Unit>();
        if (value != null && value.length() > 0) {
            final String[] nameValues = StringNormalizer.normalize(value).split("\\p{Space}+");

            for (final Party party : Bennu.getInstance().getExternalInstitutionUnit().getSubUnits()) {
                if (result.size() >= maxCount) {
                    break;
                }
                if (!party.isPerson()) {
                    final Unit unit = (Unit) party;
                    if (areNamesPresent(unit.getName(), nameValues)) {
                        result.add(unit);
                    }
                }
            }
            Collections.sort(result, Unit.COMPARATOR_BY_NAME_AND_ID);
        }
        return result;
    }

    private boolean areNamesPresent(String name, String[] searchNameParts) {
        String nameNormalized = StringNormalizer.normalize(name);
        for (String searchNamePart : searchNameParts) {
            String namePart = searchNamePart;
            if (!nameNormalized.contains(namePart)) {
                return false;
            }
        }
        return true;
    }
}
