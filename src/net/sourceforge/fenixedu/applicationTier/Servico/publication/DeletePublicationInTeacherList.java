package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeletePublicationInTeacherList implements IService {

    public void run(Integer teacherId, final Integer publicationId) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();

        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherId);
        
        List<IPublicationTeacher> publicationTeachers = teacher.getTeacherPublications();
        
        for(Iterator<IPublicationTeacher> iter = publicationTeachers.iterator(); iter.hasNext(); ){
            IPublicationTeacher publicationTeacher = iter.next();
            if (publicationTeacher.getPublication().getIdInternal().equals(publicationId)) {
                iter.remove();
                publicationTeacher.delete();
            }
        }
    }
}