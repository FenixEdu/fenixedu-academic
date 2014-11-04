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
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.University;

/**
 * @author dcs-rjao
 * 
 *         24/Mar/2003
 */

public class InfoUniversity extends InfoObject {

    private final University university;

    public InfoUniversity(final University university) {
        this.university = university;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoUniversity && university == ((InfoUniversity) obj).university;
    }

    @Override
    public String toString() {
        return getUniversity().toString();
    }

    public String getCode() {
        return getUniversity().getCode();
    }

    public String getName() {
        return getUniversity().getName();
    }

    @Override
    public String getExternalId() {
        return getUniversity().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    private University getUniversity() {
        return university;
    }

}