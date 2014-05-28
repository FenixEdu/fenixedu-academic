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
package net.sourceforge.fenixedu.dataTransferObject.residenceManagement;

import java.io.Serializable;
import java.util.List;

public class ResidentListsHolderBean implements Serializable {

    List<ResidenceEventBean> successfulEvents;
    List<ResidenceEventBean> unsuccessfulEvents;

    public List<ResidenceEventBean> getSuccessfulEvents() {
        return successfulEvents;
    }

    public List<ResidenceEventBean> getUnsuccessfulEvents() {
        return unsuccessfulEvents;
    }

    public ResidentListsHolderBean(List<ResidenceEventBean> sucessful, List<ResidenceEventBean> unsucessful) {
        this.successfulEvents = sucessful;
        this.unsuccessfulEvents = unsucessful;
    }

    public Integer getNumberOfImports() {
        return successfulEvents.size() + unsuccessfulEvents.size();
    }
}
