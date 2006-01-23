package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadRoomByOID extends Service {

	public InfoRoom run(Integer oid) throws ExcepcaoPersistencia {
		final Room room = (Room) persistentObject.readByOID(Room.class, oid);
		return InfoRoom.newInfoFromDomain(room);
	}

}