package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteRoom extends Service {

	public void run(RoomKey keySala) throws FenixServiceException, ExcepcaoPersistencia {
		final OldRoom roomToDelete = OldRoom.findOldRoomByName(keySala.getNomeSala());
		if (roomToDelete == null)
			throw new InvalidArgumentsServiceException();
		roomToDelete.delete();
	}

}
