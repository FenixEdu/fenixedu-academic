package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz
 * 
 */
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.domain.Building;
import net.sourceforge.fenixedu.domain.IBuilding;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBuilding;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateBuilding implements IService {

    public void run(String buildingName) throws ExcepcaoPersistencia, ExistingServiceException {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
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