/**
 * Nov 22, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.SupportLesson;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDomainSupportLessonByOID extends FenixService {

    @Service
    public static SupportLesson run(Integer supportLessonID) {
        return rootDomainObject.readSupportLessonByOID(supportLessonID);
    }
}