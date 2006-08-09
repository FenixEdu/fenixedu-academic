/*
 * Created on 2003/07/30
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadLessonByOID extends Service {

	public InfoLesson run(Integer oid) {
		final Lesson lesson = rootDomainObject.readLessonByOID(oid);
		return (lesson != null) ? InfoLesson.newInfoFromDomain(lesson) : null; 

	}
}