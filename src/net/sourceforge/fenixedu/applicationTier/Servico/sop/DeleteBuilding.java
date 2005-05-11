package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz
 * 
 */
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotEmptyServiceException;
import net.sourceforge.fenixedu.domain.Building;
import net.sourceforge.fenixedu.domain.IBuilding;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentBuilding;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteBuilding implements IService {

    public void run(final Integer buildingId) throws ExcepcaoPersistencia, NotEmptyServiceException {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentBuilding persistentBuilding = persistentSupport.getIPersistentBuilding();

        final IBuilding building = (IBuilding) persistentBuilding.readByOID(Building.class, buildingId);
        if (!building.getRooms().isEmpty()) {
            throw new NotEmptyServiceException();
        }
        persistentBuilding.deleteByOID(Building.class,buildingId);
    }

}