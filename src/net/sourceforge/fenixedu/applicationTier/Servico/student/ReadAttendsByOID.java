/*
 * Created on 7/Mai/2005 - 16:05:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ReadAttendsByOID implements IService {

	public InfoFrequenta run(final Integer idInternal) throws ExcepcaoPersistencia {
		final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();
		
		final IAttends attends = (IAttends) persistentAttend.readByOID(Attends.class, idInternal);

		return InfoFrequenta.newInfoFromDomain(attends);
	}

}
