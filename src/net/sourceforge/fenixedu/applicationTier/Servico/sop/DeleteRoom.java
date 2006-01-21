package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;

public class DeleteRoom extends Service {

	public void run(RoomKey keySala) throws FenixServiceException, ExcepcaoPersistencia {
		final ISalaPersistente persistentRoom = persistentSupport.getISalaPersistente();

		final Room roomToDelete = (Room) persistentRoom.readByName(keySala.getNomeSala());
		if (roomToDelete == null)
			throw new InvalidArgumentsServiceException();

		roomToDelete.delete();
		persistentRoom.deleteByOID(Room.class, roomToDelete.getIdInternal());
	}

}
