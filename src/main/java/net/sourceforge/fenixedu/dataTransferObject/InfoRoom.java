/*
 * InfoRoom.java
 * 
 * Created on 31 de Outubro de 2002, 12:19
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author tfc130
 */
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.RoomClassification;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class InfoRoom extends InfoObject implements Comparable {

    private final AllocatableSpace room;

    public InfoRoom(final AllocatableSpace room) {
        this.room = room;
    }

    public String getNome() {
        return getRoom().getNome();
    }

    public String getEdificio() {
        Building building = getRoom().getBuilding();
        return building != null ? building.getName() : "";
    }

    public Integer getPiso() {
        return getRoom().getPiso();
    }

    public String getTipo() {
        RoomClassification roomClassification = getRoom().getRoomClassification();
        return roomClassification != null ? roomClassification.getName().getContent(Language.getLanguage()) : "";
    }

    public Integer getCapacidadeNormal() {
        return getRoom().getCapacidadeNormal() == null ? Integer.valueOf(0) : getRoom().getCapacidadeNormal();
    }

    public Integer getCapacidadeExame() {
        return getRoom().getCapacidadeExame() == null ? Integer.valueOf(0) : getRoom().getCapacidadeExame();
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

    public static InfoRoom newInfoFromDomain(final AllocatableSpace room) {
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

    public AllocatableSpace getRoom() {
        return room;
    }

}