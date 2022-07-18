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
/*
 * InfoRoom.java
 *
 * Created on 31 de Outubro de 2002, 12:19
 */

package org.fenixedu.academic.dto;

import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;

/**
 * @author tfc130
 */

public class InfoRoom extends InfoObject implements Comparable {

    private final Space room;

    public InfoRoom(final Space room) {
        this.room = room;
    }

    public String getNome() {
        return getRoom().getName();
    }

    public Space getSpaceBuilding() {
        Space building = SpaceUtils.getSpaceBuilding(getRoom());
        return building != null ? building : null;
    }

    public String getEdificio() {
        Space building = getSpaceBuilding();
        return building != null ? building.getName() : "";
    }

//    public Integer getPiso() {
//        Space spaceFloor = SpaceUtils.getSpaceFloor(getRoom());
//        return spaceFloor != null ? spaceFloor.<Integer> getMetadata("level").orElse(null) : null;
//    }

    public String getTipo() {
        return getRoom().getClassification().getName().getContent();
    }

    public SpaceClassification getClassification() {
        return getRoom().getClassification();
    }

    public Integer getCapacidadeNormal() {
        return getRoom().getAllocatableCapacity();
    }

    public Integer getCapacidadeExame() {
        return getRoom().<Integer> getMetadata("examCapacity").orElse(0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoRoom && getRoom() == ((InfoRoom) obj).getRoom();
    }

    @Override
    public String toString() {
        return getRoom().toString();
    }

    @Override
    public int compareTo(Object obj) {
        return getNome().compareToIgnoreCase(((InfoRoom) obj).getNome());
    }

    public static InfoRoom newInfoFromDomain(final Space room) {
        return room == null ? null : new InfoRoom(room);
    }

    @Override
    public String getExternalId() {
        return getRoom().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    public Space getRoom() {
        return room;
    }

    public String getName() {
        return getNome();
    }

}