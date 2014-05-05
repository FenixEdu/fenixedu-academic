/*
 * InfoRoom.java
 * 
 * Created on 31 de Outubro de 2002, 12:19
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.space.SpaceUtils;

import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.SpaceClassification;
import org.fenixedu.spaces.domain.UnavailableException;

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

    public String getEdificio() {
        Space building;
        try {
            building = SpaceUtils.getSpaceBuilding(getRoom());
            return building != null ? building.getName() : "";
        } catch (UnavailableException e1) {
            return "";
        }
    }

    public Integer getPiso() {
        try {
            return getRoom().getMetadata("level");
        } catch (UnavailableException e) {
            return null;
        }
    }

    public String getTipo() {
        SpaceClassification roomClassification;
        try {
            roomClassification = getRoom().getClassification();
            return roomClassification != null ? roomClassification.getName().getContent() : "";
        } catch (UnavailableException e) {
            return "";
        }
    }

    public Integer getCapacidadeNormal() {
        return getRoom().getAllocatableCapacity() == null ? Integer.valueOf(0) : getRoom().getAllocatableCapacity();
    }

    public Integer getCapacidadeExame() {
        try {
            return (Integer) (getRoom().getMetadata("examCapacity") == null ? Integer.valueOf(0) : getRoom().getMetadata(
                    "examCapacity"));
        } catch (UnavailableException e) {
            return null;
        }
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

}