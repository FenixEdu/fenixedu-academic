package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class ReadRoomByOID implements IService {

	public InfoRoom run(Integer oid) throws ExcepcaoPersistencia {
		final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		final ISalaPersistente roomDAO = sp.getISalaPersistente();

		final IRoom room = (IRoom) roomDAO.readByOID(Room.class, oid);

		return InfoRoom.newInfoFromDomain(room);
	}
}