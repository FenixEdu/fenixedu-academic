/**
 * Nov 29, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.TeacherAdviseService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DeleteTeacherAdviseServiceByOID extends Service {

    public void run(Integer teacherAdviseServiceID, RoleType roleType) {
	TeacherAdviseService teacherAdviseService = (TeacherAdviseService) rootDomainObject
		.readTeacherServiceItemByOID(teacherAdviseServiceID);
	teacherAdviseService.delete(roleType);
    }

}
