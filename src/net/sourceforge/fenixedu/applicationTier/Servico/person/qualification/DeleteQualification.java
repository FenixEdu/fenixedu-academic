/*
 * Created on 11/Ago/2005 - 19:07:51
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.domain.IQualification;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQualification;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class DeleteQualification implements IService {
	
	public void run(Integer qualificationId) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IPersistentQualification pq = sp.getIPersistentQualification();
		
		IQualification qualification = (IQualification) pq.readByOID(Qualification.class, qualificationId);
		qualification.delete();
		
	}

}
