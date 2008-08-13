/*
 * Created on 7/Mai/2005 - 16:05:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ReadAttendsByOID extends Service {
	
	public InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers run(Integer idInternal) {
		Attends attends = rootDomainObject.readAttendsByOID(idInternal);				
		return InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers.newInfoFromDomain(attends);

	}

}
