package ServidorPersistente.OJB;

import java.util.List;

import Dominio.Building;
import Dominio.IBuilding;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBuilding;

/**
 * @author Luis Cruz
 * 
 */
public class BuildingOJB extends PersistentObjectOJB implements IPersistentBuilding {

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Building.class, null);
    }

    public void delete(final IBuilding building) throws ExcepcaoPersistencia {
        super.delete(building);
    }

}