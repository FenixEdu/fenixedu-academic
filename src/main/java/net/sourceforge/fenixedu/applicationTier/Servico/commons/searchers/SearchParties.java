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

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public abstract class SearchParties<T> implements AutoCompleteProvider<T> {

    private static int DEFAULT_SIZE = 50;

    @Override
    public Collection<T> getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        int size = getSize(argsMap);
        return search(value, size);
    }

    protected abstract Collection<T> search(String value, int size);

    private int getSize(Map<String, String> arguments) {
        String size = arguments.get("size");

        if (size == null) {
            return DEFAULT_SIZE;
        } else {
            return Integer.parseInt(size);
        }
    }

}
