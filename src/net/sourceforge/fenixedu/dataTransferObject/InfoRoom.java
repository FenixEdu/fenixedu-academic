/*
 * InfoRoom.java
 * 
 * Created on 31 de Outubro de 2002, 12:19
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author tfc130
 */
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.util.TipoSala;

public class InfoRoom extends InfoObject implements Comparable {

    private final OldRoom room;

    public InfoRoom(final OldRoom room) {
	this.room = room;
    }

    public String getNome() {
        return room.getName();
    }

    public String getEdificio() {
        return room.getBuilding().getName();
    }

    public Integer getPiso() {
        return room.getPiso();
    }

    public TipoSala getTipo() {
        return room.getTipo();
    }

    public Integer getCapacidadeNormal() {
        return room.getCapacidadeNormal();
    }

    public Integer getCapacidadeExame() {
        return room.getCapacidadeExame();
    }

    public boolean equals(Object obj) {
	return obj instanceof InfoRoom && room == ((InfoRoom) obj).room;
    }

    public String toString() {
	return room.toString();
    }

    public int compareTo(Object obj) {
        return getNome().compareTo(((InfoRoom) obj).getNome());
    }

    public static InfoRoom newInfoFromDomain(final OldRoom room) {
	return room == null ? null : new InfoRoom(room);
    }

    @Override
    public Integer getIdInternal() {
	return room.getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

    public OldRoom getRoom() {
        return room;
    }

}