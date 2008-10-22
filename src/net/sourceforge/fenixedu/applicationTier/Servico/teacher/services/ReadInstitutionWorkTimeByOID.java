/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacher.InstitutionWorkTime;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadInstitutionWorkTimeByOID extends FenixService {

    @Service
    public static InstitutionWorkTime run(Integer institutionWorkTimeID) {
	return (InstitutionWorkTime) rootDomainObject.readTeacherServiceItemByOID(institutionWorkTimeID);
    }

}