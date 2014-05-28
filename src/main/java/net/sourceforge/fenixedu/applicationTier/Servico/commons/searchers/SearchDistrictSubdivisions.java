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
package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import java.util.Collection;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DistrictSubdivision;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public class SearchDistrictSubdivisions implements AutoCompleteProvider<DistrictSubdivision> {

    private static int DEFAULT_SIZE = 50;

    @Override
    public Collection<DistrictSubdivision> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        return DistrictSubdivision.findByName(value, getSize(argsMap));
    }

    private int getSize(Map<String, String> arguments) {
        String size = arguments.get("size");
        return size == null ? DEFAULT_SIZE : Integer.parseInt(size);
    }

}
