package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteRoom implements IService {

	public void run(RoomKey keySala) throws FenixServiceException, ExcepcaoPersistencia {

		final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		final ISalaPersistente persistentRoom = persistentSupport.getISalaPersistente();

		final IRoom roomToDelete = (IRoom) persistentRoom.readByName(keySala.getNomeSala());
		if (roomToDelete == null)
			throw new InvalidArgumentsServiceException();

		roomToDelete.delete();
		persistentRoom.deleteByOID(Room.class, roomToDelete.getIdInternal());
	}

}
