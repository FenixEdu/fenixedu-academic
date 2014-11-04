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
package org.fenixedu.academic.domain.phd.access;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.phd.PhdElementsList;

public class PhdProcessAccessTypeList extends PhdElementsList<PhdProcessAccessType> {

    static private final long serialVersionUID = 1L;

    protected PhdProcessAccessTypeList() {
        super();
    }

    protected PhdProcessAccessTypeList(final String types) {
        super(types);
    }

    public PhdProcessAccessTypeList(Collection<PhdProcessAccessType> types) {
        super(types);
    }

    @Override
    protected PhdProcessAccessType convertElementToSet(String valueToParse) {
        return PhdProcessAccessType.valueOf(valueToParse);
    }

    @Override
    protected String convertElementToString(PhdProcessAccessType element) {
        return element.name();
    }

    @Override
    protected PhdProcessAccessTypeList createNewInstance() {
        return new PhdProcessAccessTypeList();
    }

    @Override
    public PhdProcessAccessTypeList addAccessTypes(PhdProcessAccessType... types) {
        return (PhdProcessAccessTypeList) super.addAccessTypes(types);
    }

    static public PhdProcessAccessTypeList importFromString(String value) {
        return StringUtils.isEmpty(value) ? EMPTY : new PhdProcessAccessTypeList(value);
    }

    final static public PhdProcessAccessTypeList EMPTY = new PhdProcessAccessTypeList() {

        static private final long serialVersionUID = 1L;

        @Override
        public Set<PhdProcessAccessType> getTypes() {
            return Collections.emptySet();
        }

        @Override
        public String toString() {
            return "";
        }
    };

}
