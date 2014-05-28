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

import org.fenixedu.spaces.domain.Space;

/**
 * @author Tânia Pousão Create on 10/Nov/2003
 */
public class InfoCampus extends InfoObject {
    private String name;

    public InfoCampus() {
    }

    /**
     * @param integer
     */
    public InfoCampus(String campusId) {
        super(campusId);
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoCampus) {
            InfoCampus infoCampus = (InfoCampus) obj;
            result = getName().equals(infoCampus.getName());
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "[INFODEGREE_INFO:";
        result += " codigo interno= " + getExternalId();
        result += " name= " + getName();
        result += "]";
        return result;
    }

    public void copyFromDomain(Space campus) {
        super.copyFromDomain(campus);
        if (campus != null) {
            setName(campus.getName());
        }
    }

    public static InfoCampus newInfoFromDomain(Space campus) {
        InfoCampus infoCampus = null;
        if (campus != null) {
            infoCampus = new InfoCampus();
            infoCampus.copyFromDomain(campus);
        }
        return infoCampus;
    }
}