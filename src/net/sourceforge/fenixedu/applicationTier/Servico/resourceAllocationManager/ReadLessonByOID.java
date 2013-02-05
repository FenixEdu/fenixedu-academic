/*
 * Created on 2003/07/30
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadLessonByOID extends FenixService {

    @Service
    public static InfoLesson run(Integer oid) {
        final Lesson lesson = rootDomainObject.readLessonByOID(oid);
        return (lesson != null) ? InfoLesson.newInfoFromDomain(lesson) : null;

    }
}