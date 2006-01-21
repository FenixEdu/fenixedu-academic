/*
 * Created on 7/Mai/2005 - 16:05:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ReadAttendsByOID extends Service {
	
	public InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers run(Integer idInternal) throws ExcepcaoPersistencia {
        IFrequentaPersistente persistentAttend = persistentSupport.getIFrequentaPersistente();
		
		Attends attends = (Attends)persistentAttend.readByOID(Attends.class, idInternal);
				
		return InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers.newInfoFromDomain(attends);

	}

    

}
