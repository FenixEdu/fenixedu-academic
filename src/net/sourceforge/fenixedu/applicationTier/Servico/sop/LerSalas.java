package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerSalas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class LerSalas extends Service {

	public Object run() throws ExcepcaoPersistencia {
		List salas = null;
		List infoSalas = null;

		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		salas = persistentSupport.getISalaPersistente().readAll();

		Iterator iterator = salas.iterator();
		infoSalas = new ArrayList();
		while (iterator.hasNext()) {
			Room elem = (Room) iterator.next();
			infoSalas.add(new InfoRoom(elem.getNome(), elem.getBuilding().getName(), elem.getPiso(),
					elem.getTipo(), elem.getCapacidadeNormal(), elem.getCapacidadeExame()));
		}

		return infoSalas;
	}

}