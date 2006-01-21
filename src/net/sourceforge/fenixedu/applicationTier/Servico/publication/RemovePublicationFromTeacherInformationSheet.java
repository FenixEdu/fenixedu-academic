package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;

public class RemovePublicationFromTeacherInformationSheet extends Service {
    
    public void run(Integer teacherId, final Integer publicationId) throws ExcepcaoPersistencia, DomainException {
        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        IPersistentPublication persistentPublication = persistentSupport.getIPersistentPublication();

        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherId);
        Publication publication = (Publication) persistentPublication.readByOID(Publication.class, publicationId);
        
        teacher.removeFromTeacherInformationSheet(publication);
    }
    
}