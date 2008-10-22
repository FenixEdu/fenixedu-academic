/**
 * Nov 30, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.services;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.teacher.OtherService;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadOtherServiceByOID extends FenixService {

    @Service
    public static OtherService run(Integer otherServiceID) {
	return (OtherService) rootDomainObject.readTeacherServiceItemByOID(otherServiceID);
    }

}