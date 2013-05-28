/*
 * Created on 2003/07/30
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.Lesson;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadLessonByOID {

    @Service
    public static InfoLesson run(Integer oid) {
        final Lesson lesson = AbstractDomainObject.fromExternalId(oid);
        return (lesson != null) ? InfoLesson.newInfoFromDomain(lesson) : null;

    }
}