/*
 * InfoRoom.java
 * 
 * Created on 31 de Outubro de 2002, 12:19
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author tfc130
 */
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.util.TipoSala;

public class InfoRoom extends InfoObject implements Comparable {

    private final DomainReference<OldRoom> room;

    public InfoRoom(final OldRoom room) {
	this.room = new DomainReference<OldRoom>(room);
    }

    public String getNome() {
        return getRoom().getName();
    }

    public String getEdificio() {
        return getRoom().getBuilding().getName();
    }

    public Integer getPiso() {
        return getRoom().getPiso();
    }

    public TipoSala getTipo() {
        return getRoom().getTipo();
    }

    public Integer getCapacidadeNormal() {
        return getRoom().getCapacidadeNormal();
    }

    public Integer getCapacidadeExame() {
        return getRoom().getCapacidadeExame();
    }

    public boolean equals(Object obj) {
	return obj instanceof InfoRoom && getRoom() == ((InfoRoom) obj).getRoom();
    }

    public String toString() {
	return getRoom().toString();
    }

    public int compareTo(Object obj) {
        return getNome().compareTo(((InfoRoom) obj).getNome());
    }

    public static InfoRoom newInfoFromDomain(final OldRoom room) {
	return room == null ? null : new InfoRoom(room);
    }

    @Override
    public Integer getIdInternal() {
	return getRoom().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
	throw new Error("Method should not be called!");
    }

    public OldRoom getRoom() {
        return room == null ? null : room.getObject();
    }

}