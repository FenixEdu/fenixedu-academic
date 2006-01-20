/*
 * Created on 11/Ago/2005 - 19:07:51
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.qualification;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentQualification;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class DeleteQualification extends Service {
	
	public void run(Integer qualificationId) throws ExcepcaoPersistencia {
		IPersistentQualification pq = persistentSupport.getIPersistentQualification();
		
		Qualification qualification = (Qualification) pq.readByOID(Qualification.class, qualificationId);
		qualification.delete();
		
	}

}
