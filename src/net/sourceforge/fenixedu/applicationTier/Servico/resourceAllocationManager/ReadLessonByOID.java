/*
 * Created on 2003/07/30
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadLessonByOID extends FenixService {

    public InfoLesson run(Integer oid) {
	final Lesson lesson = rootDomainObject.readLessonByOID(oid);
	return (lesson != null) ? InfoLesson.newInfoFromDomain(lesson) : null;

    }
}