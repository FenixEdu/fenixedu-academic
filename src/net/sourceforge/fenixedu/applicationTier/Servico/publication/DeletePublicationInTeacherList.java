/*
 * Created on Jun 11, 2004
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author TJBF & PFON
 *  
 */
public class DeletePublicationInTeacherList implements IService {

    public DeletePublicationInTeacherList() {
    }

    public void run(Integer teacherId, final Integer publicationId) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

        ITeacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherId);
        persistentTeacher.simpleLockWrite(teacher);
        CollectionUtils.filter(teacher.getTeacherPublications(), new Predicate() {
            public boolean evaluate(Object arg0) {
                IPublication publication = (IPublication) arg0;
                return !publication.getIdInternal().equals(publicationId);
            }
        });
    }
}