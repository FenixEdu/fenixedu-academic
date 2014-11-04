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
package org.fenixedu.academic.dto.resourceAllocationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.LocalDate;

public class SearchOccupationEventsBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate start;
    private LocalDate end;
    private Space building;
    private List<OccupationType> types;
    private static List<OccupationType> ALL_TYPES = new ArrayList<OccupationType>();

    static {
        ALL_TYPES.addAll(Arrays.asList(OccupationType.values()));
    }

    public SearchOccupationEventsBean() {
        setTypes(ALL_TYPES);
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Space getBuilding() {
        return building;
    }

    public void setBuilding(Space building) {
        this.building = building;
    }

    public List<OccupationType> getTypes() {
        return types;
    }

    public void setTypes(List<OccupationType> types) {
        this.types = types;
    }
}
