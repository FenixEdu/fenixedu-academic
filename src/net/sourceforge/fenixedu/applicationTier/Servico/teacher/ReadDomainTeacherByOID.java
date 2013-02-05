/**
 * Nov 24, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDomainTeacherByOID extends FenixService {

    @Service
    public static Teacher run(Integer teacherID) {
        return rootDomainObject.readTeacherByOID(teacherID);
    }
}