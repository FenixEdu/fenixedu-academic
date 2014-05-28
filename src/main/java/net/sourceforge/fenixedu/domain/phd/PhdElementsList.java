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
package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

abstract public class PhdElementsList<T> implements Serializable {

    static private final long serialVersionUID = 1L;

    private final Set<T> types = new TreeSet<T>();

    protected PhdElementsList() {
    }

    protected PhdElementsList(final String types) {
        super();
        this.types.addAll(convertToSet(types));
    }

    protected PhdElementsList(Collection<T> types) {
        super();
        this.types.addAll(types);
    }

    private Set<T> convertToSet(String types) {
        final Set<T> result = new HashSet<T>();

        for (final String each : types.split(",")) {
            String valueToParse = each.trim();
            if (!StringUtils.isEmpty(valueToParse)) {
                result.add(convertElementToSet(valueToParse));
            }
        }

        return result;
    }

    abstract protected T convertElementToSet(String valueToParse);

    private String convertToString(Set<T> types) {
        final StringBuilder result = new StringBuilder();

        for (T each : types) {
            result.append(convertElementToString(each)).append(",");
        }

        if (result.length() > 0 && result.charAt(result.length() - 1) == ',') {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }

    abstract protected String convertElementToString(T element);

    public Set<T> getTypes() {
        return Collections.unmodifiableSet(types);
    }

    public Set<T> getSortedTypes() {
        return Collections.unmodifiableSet(new TreeSet<T>(types));
    }

    @Override
    public String toString() {
        return convertToString(this.types);
    }

    public PhdElementsList<T> addAccessTypes(T... types) {
        final PhdElementsList<T> result = createNewInstance();
        result.types.addAll(getTypes());
        result.types.addAll(Arrays.asList(types));
        return result;
    }

    abstract protected PhdElementsList<T> createNewInstance();

}