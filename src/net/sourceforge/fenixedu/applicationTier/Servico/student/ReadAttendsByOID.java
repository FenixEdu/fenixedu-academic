/*
 * Created on 7/Mai/2005 - 16:05:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers;
import net.sourceforge.fenixedu.domain.Attends;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadAttendsByOID extends FenixService {

	@Checked("RolePredicates.STUDENT_PREDICATE")
	@Service
	public static InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers run(Integer idInternal) {
		Attends attends = rootDomainObject.readAttendsByOID(idInternal);
		return InfoAttendsWithProfessorshipTeachersAndNonAffiliatedTeachers.newInfoFromDomain(attends);

	}

}