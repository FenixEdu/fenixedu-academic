package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz
 * 
 */
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Building;
import Dominio.IBuilding;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBuilding;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class CreateBuilding implements IService {

    public void run(String buildingName) throws ExcepcaoPersistencia, ExistingServiceException {
        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final IPersistentBuilding persistentBuilding = persistentSupport.getIPersistentBuilding();

        final List buildings = persistentBuilding.readAll();

        if (exists(buildings, buildingName)) {
            throw new ExistingServiceException();
        }

        final IBuilding building = new Building();
        persistentBuilding.simpleLockWrite(building);
        building.setName(buildingName);
    }

    protected boolean exists(final List buildings, final String buildingName) {
        final IBuilding building = (IBuilding) CollectionUtils.find(buildings, new Predicate() {

            public boolean evaluate(Object arg0) {
                IBuilding building = (IBuilding) arg0;
                return building.getName().equalsIgnoreCase(buildingName);
            }});

        return building != null;
    }

}