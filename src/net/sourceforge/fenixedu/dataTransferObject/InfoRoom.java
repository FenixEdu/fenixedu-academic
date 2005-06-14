/*
 * InfoRoom.java
 * 
 * Created on 31 de Outubro de 2002, 12:19
 */

package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author tfc130
 */
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.util.TipoSala;

public class InfoRoom extends InfoObject implements Comparable {

    protected String _nome;

    protected String _edificio;

    protected Integer _piso;

    protected Integer _capacidadeNormal;

    protected Integer _capacidadeExame;

    protected TipoSala _tipo;

    public InfoRoom() {
    }

    public InfoRoom(String nome, String edificio, Integer piso, TipoSala tipo, Integer capacidadeNormal,
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
        if (obj instanceof InfoRoom) {
            InfoRoom infoSala = (InfoRoom) obj;
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
        return getNome().compareTo(((InfoRoom) obj).getNome());
    }

    public void copyFromDomain(IRoom sala) {
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

    /**
     * @param sala
     * @return
     */
    public static InfoRoom newInfoFromDomain(IRoom sala) {
        InfoRoom infoRoom = null;
        if (sala != null) {
            infoRoom = new InfoRoom();
            infoRoom.copyFromDomain(sala);
        }
        return infoRoom;
    }
}