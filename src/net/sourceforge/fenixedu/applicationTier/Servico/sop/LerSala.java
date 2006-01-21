package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerSala.
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.RoomKey;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class LerSala extends Service {

	public Object run(RoomKey keySala) throws ExcepcaoPersistencia {
		InfoRoom infoSala = null;

		Room sala = persistentSupport.getISalaPersistente().readByName(keySala.getNomeSala());
		if (sala != null)
			infoSala = new InfoRoom(sala.getNome(), sala.getBuilding().getName(), sala.getPiso(), sala
					.getTipo(), sala.getCapacidadeNormal(), sala.getCapacidadeExame());

		return infoSala;
	}

}