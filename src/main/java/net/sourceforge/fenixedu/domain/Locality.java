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
package net.sourceforge.fenixedu.domain;

public class Locality extends Locality_Base {

    public Locality() {
        super();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Holiday> getHolidays() {
        return getHolidaysSet();
    }

    @Deprecated
    public boolean hasAnyHolidays() {
        return !getHolidaysSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<org.fenixedu.spaces.domain.Space> getCampusInformation() {
        return getCampusInformationSet();
    }

    @Deprecated
    public boolean hasAnyCampusInformation() {
        return !getCampusInformationSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
