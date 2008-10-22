/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.teacher.workTime.InstitutionWorkTimeDTO;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class EditTeacherInstitutionWorkTime extends FenixService {

    public void run(InstitutionWorkTimeDTO institutionWorkTimeDTO, RoleType roleType) {
	InstitutionWorkTime institutionWorkTime = (InstitutionWorkTime) rootDomainObject
		.readTeacherServiceItemByOID(institutionWorkTimeDTO.getIdInternal());

	institutionWorkTime.update(institutionWorkTimeDTO, roleType);
    }

}
