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
package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class TypeOfProgrammeList implements Serializable {
    private static EmptyTypeOfProgrammeList EMPTY = new EmptyTypeOfProgrammeList();

    public static class EmptyTypeOfProgrammeList extends TypeOfProgrammeList {
        /**
	 * 
	 */
        private static final long serialVersionUID = 1L;

        @Override
        public Set<TypeOfProgramme> getTypes() {
            return Collections.emptySet();
        }

        @Override
        public String toString() {
            return "";
        }
    }

    private final Set<TypeOfProgramme> types = new HashSet<TypeOfProgramme>();

    private TypeOfProgrammeList() {

    }

    private TypeOfProgrammeList(final String types) {
        super();
        this.types.addAll(convertToSet(types));
    }

    public TypeOfProgrammeList(Collection<TypeOfProgramme> types) {
        super();
        this.types.addAll(types);
    }

    public Set<TypeOfProgramme> getTypes() {
        return Collections.unmodifiableSet(types);
    }

    @Override
    public String toString() {
        return convertToString(this.types);
    }

    private String convertToString(Set<TypeOfProgramme> types) {
        final StringBuilder result = new StringBuilder();

        for (TypeOfProgramme each : types) {
            result.append(each.name()).append(",");
        }

        if (result.length() > 0 && result.charAt(result.length() - 1) == ',') {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }

    private Set<TypeOfProgramme> convertToSet(String types) {
        final Set<TypeOfProgramme> result = new HashSet<TypeOfProgramme>();

        for (final String each : types.split(",")) {
            String valueToParse = each.trim();
            if (!StringUtils.isEmpty(valueToParse)) {
                result.add(TypeOfProgramme.valueOf(valueToParse));
            }
        }

        return result;
    }

    static public TypeOfProgrammeList importFromString(String value) {
        return StringUtils.isEmpty(value) ? EMPTY : new TypeOfProgrammeList(value);
    }

}
