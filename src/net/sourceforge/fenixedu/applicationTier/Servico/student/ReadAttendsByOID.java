/*
 * Created on 7/Mai/2005 - 16:05:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ReadAttendsByOID extends Service {

	
	public InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers run(Integer idInternal) throws ExcepcaoPersistencia {
		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IFrequentaPersistente persistentAttend = persistentSupport.getIFrequentaPersistente();
		
		Attends attends = (Attends)persistentAttend.readByOID(Attends.class, idInternal);
				
		return InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers.newInfoFromDomain(attends);

	}

    

}
