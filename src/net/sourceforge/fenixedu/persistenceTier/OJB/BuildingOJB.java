package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.Building;
import net.sourceforge.fenixedu.domain.IBuilding;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBuilding;

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