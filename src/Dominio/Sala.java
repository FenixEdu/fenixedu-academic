/*
 * Sala.java
 *
 * Created on 17 de Outubro de 2002, 22:56
 */

package Dominio;

/**
 * 
 * @author tfc130
 */
import java.util.List;

import Util.TipoSala;

public class Sala extends DomainObject implements ISala {

    protected String _nome;

    protected String _edificio;

    protected Integer _piso;

    protected Integer _capacidadeNormal;

    protected Integer _capacidadeExame;

    protected TipoSala _tipo;

    private List roomOccupations;

    private Integer keyBuilding;

    private IBuilding building;

    /** Construtor sem argumentos público requerido pela moldura de objectos OJB */
    public Sala() {
    }

    public Sala(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Sala(String nome, String edificio, Integer piso, TipoSala tipo, Integer capacidadeNormal,
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
        //return _edificio;
        return getBuilding().getName();
    }

    public void setEdificio(String edificio) {
        //_edificio = edificio;
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
        if (obj instanceof ISala) {
            ISala sala = (ISala) obj;
            resultado = (getNome().equals(sala.getNome()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[SALA";
        result += ", codInt=" + getIdInternal();
        result += ", nome=" + _nome;
        result += ", edificio=" + _edificio;
        result += ", piso=" + _piso;
        result += ", tipo=" + _tipo;
        result += ", capacidadeNormal=" + _capacidadeNormal;
        result += ", capacidadeExame=" + _capacidadeExame;
        result += "]";
        return result;
    }

    /**
     * @return Returns the roomOccupations.
     */
    public List getRoomOccupations() {
        return roomOccupations;
    }

    /**
     * @param roomOccupations
     *            The roomOccupations to set.
     */
    public void setRoomOccupations(List roomOccupations) {
        this.roomOccupations = roomOccupations;
    }

    public IBuilding getBuilding() {
        return building;
    }

    public void setBuilding(IBuilding building) {
        this.building = building;
        this._edificio = building.getName();
    }

    public Integer getKeyBuilding() {
        return keyBuilding;
    }

    public void setKeyBuilding(Integer keyBuilding) {
        this.keyBuilding = keyBuilding;
    }

}