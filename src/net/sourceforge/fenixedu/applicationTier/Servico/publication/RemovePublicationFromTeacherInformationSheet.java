package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class RemovePublicationFromTeacherInformationSheet implements IService {
    
    public void run(Integer teacherId, final Integer publicationId) throws ExcepcaoPersistencia, DomainException {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        IPersistentPublication persistentPublication = sp.getIPersistentPublication();

        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherId);
        Publication publication = (Publication) persistentPublication.readByOID(Publication.class, publicationId);
        
        teacher.removeFromTeacherInformationSheet(publication);
    }
    
}