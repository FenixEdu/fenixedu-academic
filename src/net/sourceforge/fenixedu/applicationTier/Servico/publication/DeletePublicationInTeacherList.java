package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeletePublicationInTeacherList implements IService {

    public DeletePublicationInTeacherList() {
    }

    public void run(Integer teacherId, final Integer publicationId) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        IPersistentPublication persistentPublication = sp.getIPersistentPublication();
        IPersistentPublicationTeacher persistentPublicationTeacher = sp.getIPersistentPublicationTeacher();

        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherId);
        IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class,publicationId);
        persistentTeacher.simpleLockWrite(teacher);
        List publicationTeachers2 = (List) CollectionUtils.select(teacher.getTeacherPublications(), new Predicate() {
            public boolean evaluate(Object arg0) {
                IPublicationTeacher publicationTeacher = (IPublicationTeacher) arg0;
                IPublication publication = publicationTeacher.getPublication();
                return publication.getIdInternal().equals(publicationId);
            }
        });
        CollectionUtils.filter(teacher.getTeacherPublications(), new Predicate() {
            public boolean evaluate(Object arg0) {
                IPublicationTeacher publicationTeacher = (IPublicationTeacher) arg0;
                IPublication publication = publicationTeacher.getPublication();
                return !publication.getIdInternal().equals(publicationId);
            }
        });        

        IPublicationTeacher publicationTeacher = (IPublicationTeacher) publicationTeachers2.get(0);
        
        persistentPublication.simpleLockWrite(publication);
        publication.getPublicationTeachers().remove(publicationTeacher);
        
        persistentPublicationTeacher.deleteByOID(PublicationTeacher.class,publicationTeacher.getIdInternal());
    }
}