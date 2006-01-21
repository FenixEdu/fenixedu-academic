package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;

public class ReadRoomByOID extends Service {

	public InfoRoom run(Integer oid) throws ExcepcaoPersistencia {
		final ISalaPersistente roomDAO = persistentSupport.getISalaPersistente();

		final Room room = (Room) roomDAO.readByOID(Room.class, oid);

		return InfoRoom.newInfoFromDomain(room);
	}
}