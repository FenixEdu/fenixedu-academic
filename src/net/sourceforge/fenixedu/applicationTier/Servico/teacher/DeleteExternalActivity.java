/*
 * Created on 11/Ago/2005 - 19:12:31
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class DeleteExternalActivity extends Service {

	public void run(Integer externalActivityId) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentExternalActivity pea = persistentSupport.getIPersistentExternalActivity();
		
		ExternalActivity externalActivity = (ExternalActivity) pea.readByOID(ExternalActivity.class, externalActivityId);
		externalActivity.delete();
		
	}
}
