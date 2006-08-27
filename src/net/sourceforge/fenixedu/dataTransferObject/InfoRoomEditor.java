package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.util.TipoSala;

public class InfoRoomEditor extends InfoObject implements Comparable {

    protected String _nome;

    protected String _edificio;

    protected Integer _piso;

    protected Integer _capacidadeNormal;

    protected Integer _capacidadeExame;

    protected TipoSala _tipo;

    public InfoRoomEditor() {
    }

    public InfoRoomEditor(String nome, String edificio, Integer piso, TipoSala tipo, Integer capacidadeNormal,
            Integer capacidadeExame) {
        setNome(nome);
        setEdificio(edificio);
        setPiso(piso);
        setTipo(tipo);
        setCapacidadeNormal(capacidadeNormal);
        setCapacidadeExame(capacidadeExame);
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
        return _piso;
    }

    public void setPiso(Integer piso) {
        _piso = piso;
    }

    public TipoSala getTipo() {
        return _tipo;
    }

    public void setTipo(TipoSala tipo) {
        _tipo = tipo;
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

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoRoomEditor) {
            InfoRoomEditor infoSala = (InfoRoomEditor) obj;
            resultado = (getNome().equals(infoSala.getNome()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[INFOROOM";
        result += ", nome=" + _nome;
        result += ", edificio=" + _edificio;
        result += ", piso=" + _piso;
        result += ", tipo=" + _tipo;
        result += ", capacidadeNormal=" + _capacidadeNormal;
        result += ", capacidadeExame=" + _capacidadeExame;
        result += "]";
        return result;
    }

    public int compareTo(Object obj) {
        return getNome().compareTo(((InfoRoomEditor) obj).getNome());
    }

    public void copyFromDomain(OldRoom sala) {
        super.copyFromDomain(sala);
        if (sala != null) {
            setNome(sala.getNome());
            setEdificio(sala.getBuilding().getName());
            setPiso(sala.getPiso());
            setTipo(sala.getTipo());
            setCapacidadeNormal(sala.getCapacidadeNormal());
            setCapacidadeExame(sala.getCapacidadeExame());
        }
    }

    public static InfoRoomEditor newInfoFromDomain(OldRoom sala) {
        InfoRoomEditor infoRoom = null;
        if (sala != null) {
            infoRoom = new InfoRoomEditor();
            infoRoom.copyFromDomain(sala);
        }
        return infoRoom;
    }
}