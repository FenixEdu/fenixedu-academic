package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomClassification;

public class InfoRoomEditor extends InfoObject implements Comparable {

	protected String _nome;

	protected String _edificio;

	protected Integer _pisoReference;

	protected RoomClassification _tipoReference;

	protected Integer _capacidadeNormal;

	protected Integer _capacidadeExame;

	private AllocatableSpace roomReference;

	public InfoRoomEditor() {
	}

	public InfoRoomEditor(String nome, String edificio, Integer piso, RoomClassification tipo, Integer capacidadeNormal,
			Integer capacidadeExame) {
		setNome(nome);
		setEdificio(edificio);
		setPiso(piso);
		setTipoReference(tipo);
		setCapacidadeNormal(capacidadeNormal);
		setCapacidadeExame(capacidadeExame);
	}

	public InfoRoomEditor(Integer capacidadeNormal, Integer capacidadeExame, AllocatableSpace room) {
		setCapacidadeNormal(capacidadeNormal);
		setCapacidadeExame(capacidadeExame);
		setRoomReference(room);
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
		return this._tipoReference;
	}

	public void setTipoReference(RoomClassification tipo) {
		_tipoReference = tipo;
	}

	public AllocatableSpace getRoom() {
		return getRoomReference() == null ? null : getRoomReference();
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

	@Override
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
			setRoomReference(sala);
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

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof InfoRoomEditor) ? getRoom().equals(((InfoRoomEditor) obj).getRoom()) : false;
	}

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	public AllocatableSpace getRoomReference() {
		return roomReference;
	}

	public void setRoomReference(AllocatableSpace roomReference) {
		this.roomReference = roomReference;
	}
}