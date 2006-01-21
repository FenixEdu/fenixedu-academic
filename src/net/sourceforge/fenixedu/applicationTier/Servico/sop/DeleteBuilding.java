package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz
 * 
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotEmptyServiceException;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteBuilding extends Service {

    public void run(final Integer buildingId) throws ExcepcaoPersistencia, NotEmptyServiceException {
        final Building building = (Building) persistentSupport.getIPersistentObject().readByOID(Building.class, buildingId);
        if (!building.getRooms().isEmpty()) {
            throw new NotEmptyServiceException();
        }
        persistentSupport.getIPersistentObject().deleteByOID(Building.class,buildingId);
    }

}