/*
 * Created on 2003/07/30
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadLessonByOID {

    @Atomic
    public static InfoLesson run(String oid) {
        final Lesson lesson = FenixFramework.getDomainObject(oid);
        return (lesson != null) ? InfoLesson.newInfoFromDomain(lesson) : null;

    }
}