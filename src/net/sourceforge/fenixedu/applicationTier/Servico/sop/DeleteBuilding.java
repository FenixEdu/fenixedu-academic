package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz
 * 
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotEmptyServiceException;
import net.sourceforge.fenixedu.domain.space.OldBuilding;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteBuilding extends Service {

    public void run(final Integer buildingId) throws ExcepcaoPersistencia, NotEmptyServiceException {
        final OldBuilding building = (OldBuilding) rootDomainObject.readResourceByOID(buildingId);
        if (building.hasAnyRooms()) {
            throw new NotEmptyServiceException();
        }
        building.delete();
    }

}
