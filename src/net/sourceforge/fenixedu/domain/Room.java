/*
 * Room.java
 *
 * Created on 17 de Outubro de 2002, 22:56
 */

package net.sourceforge.fenixedu.domain;

/**
 * 
 * @author tfc130
 */
import net.sourceforge.fenixedu.util.TipoSala;

public class Room extends Room_Base {
    protected TipoSala _tipo;

    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    public Room() {
    }

    public Room(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Room(String nome, String edificio, Integer piso, TipoSala tipo, Integer capacidadeNormal,
            Integer capacidadeExame) {
        setNome(nome);
        setEdificio(edificio);
        setPiso(piso);
        setTipo(tipo);
        setCapacidadeNormal(capacidadeNormal);
        setCapacidadeExame(capacidadeExame);
    }

    public String getEdificio() {
        //return _edificio;
        return getBuilding().getName();
    }

    public void setEdificio(String edificio) {
        //_edificio = edificio;
    }

    public TipoSala getTipo() {
        return _tipo;
    }

    public void setTipo(TipoSala tipo) {
        _tipo = tipo;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IRoom) {
            IRoom sala = (IRoom) obj;
            resultado = (getNome().equals(sala.getNome()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[SALA";
        result += ", codInt=" + getIdInternal();
        result += ", nome=" + getNome();
        result += ", piso=" + getPiso();
        result += ", tipo=" + getTipo();
        result += ", capacidadeNormal=" + getCapacidadeNormal();
        result += ", capacidadeExame=" + getCapacidadeExame();
        result += "]";
        return result;
    }
}