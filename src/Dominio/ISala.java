/*
 * ISala.java
 *
 * Created on 17 de Outubro de 2002, 16:45
 */

package Dominio;

/**
 * 
 * @author tfc130
 */
import java.io.Serializable;
import java.util.List;

import Util.TipoSala;

public interface ISala extends Serializable, IDomainObject {
    public String getNome();

    public String getEdificio();

    public Integer getPiso();

    public TipoSala getTipo();

    public Integer getCapacidadeNormal();

    public Integer getCapacidadeExame();

    public List getRoomOccupations();

    public void setNome(String nome);

    public void setEdificio(String edificio);

    public void setPiso(Integer piso);

    public void setTipo(TipoSala tipo);

    public void setCapacidadeNormal(Integer capacidadeNormal);

    public void setCapacidadeExame(Integer capacidadeExame);

    public void setRoomOccupations(List roomOccupations);

    public IBuilding getBuilding();

    public void setBuilding(IBuilding building);
}