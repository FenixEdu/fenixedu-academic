package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomClassification;

public class InfoRoomEditor extends InfoObject implements Comparable {

    protected String _nome;

    protected String _edificio;

    protected Integer _pisoReference;

    protected DomainReference<RoomClassification> _tipoReference;

    protected Integer _capacidadeNormal;

    protected Integer _capacidadeExame;

    private DomainReference<AllocatableSpace> roomReference;

    public InfoRoomEditor() {
    }

    public InfoRoomEditor(String nome, String edificio, Integer piso, RoomClassification tipo, Integer capacidadeNormal,
	    Integer capacidadeExame) {
	setNome(nome);
	setEdificio(edificio);
	setPiso(piso);
	setTipoReference(new DomainReference<RoomClassification>(tipo));
	setCapacidadeNormal(capacidadeNormal);
	setCapacidadeExame(capacidadeExame);
    }

    public InfoRoomEditor(Integer capacidadeNormal, Integer capacidadeExame, AllocatableSpace room) {
	setCapacidadeNormal(capacidadeNormal);
	setCapacidadeExame(capacidadeExame);
	setRoomReference(new DomainReference<AllocatableSpace>(room));
    }

    public String getNome() {
	return _nome;
    }

    public void setNome(String nome) {
	_nome = nome;
    }

    public String getEdificio() {
	return _edificio;
    }

    public void setEdificio(String edificio) {
	_edificio = edificio;
    }

    public Integer getPiso() {
	return _pisoReference;
    }

    public void setPiso(Integer piso) {
	_pisoReference = piso;
    }

    public RoomClassification getTipo() {
	return this._tipoReference == null ? null : this._tipoReference.getObject();
    }

    public void setTipoReference(DomainReference<RoomClassification> tipo) {
	_tipoReference = tipo;
    }

    public AllocatableSpace getRoom() {
	return getRoomReference() == null ? null : getRoomReference().getObject();
    }

    public Integer getCapacidadeNormal() {
	return _capacidadeNormal;
    }

    public void setCapacidadeNormal(Integer capacidadeNormal) {
	_capacidadeNormal = capacidadeNormal;
    }

    public Integer getCapacidadeExame() {
	return _capacidadeExame;
    }

    public void setCapacidadeExame(Integer capacidadeExame) {
	_capacidadeExame = capacidadeExame;
    }

    public String toString() {
	String result = "[INFOROOM";
	result += ", capacidadeNormal=" + _capacidadeNormal;
	result += ", capacidadeExame=" + _capacidadeExame;
	result += "]";
	return result;
    }

    public void copyFromDomain(AllocatableSpace sala) {
	super.copyFromDomain(sala);
	if (sala != null) {
	    setCapacidadeNormal(sala.getCapacidadeNormal());
	    setCapacidadeExame(sala.getCapacidadeExame());
	    setRoomReference(new DomainReference<AllocatableSpace>(sala));
	}
    }

    public static InfoRoomEditor newInfoFromDomain(Room sala) {
	InfoRoomEditor infoRoom = null;
	if (sala != null) {
	    infoRoom = new InfoRoomEditor();
	    infoRoom.copyFromDomain(sala);
	}
	return infoRoom;
    }

    public boolean equals(Object obj) {
	return (obj instanceof InfoRoomEditor) ? getRoom().equals(((InfoRoomEditor) obj).getRoom()) : false;
    }

    public int compareTo(Object o) {
	return 0;
    }

    public DomainReference<AllocatableSpace> getRoomReference() {
	return roomReference;
    }

    public void setRoomReference(DomainReference<AllocatableSpace> roomReference) {
	this.roomReference = roomReference;
    }
}