package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz
 * 
 */
import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Building;
import Dominio.IBuilding;
import ServidorAplicacao.Servico.exceptions.NotEmptyServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBuilding;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteBuilding implements IService {

    public void run(final Integer buildingId) throws ExcepcaoPersistencia, NotEmptyServiceException {
        final ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
        final IPersistentBuilding persistentBuilding = persistentSupport.getIPersistentBuilding();

        final IBuilding building = (IBuilding) persistentBuilding.readByOID(Building.class, buildingId);
        if (!building.getRooms().isEmpty()) {
            throw new NotEmptyServiceException();
        }
        persistentBuilding.delete(building);
    }

}