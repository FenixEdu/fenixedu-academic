package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.util.PublicationArea;

public class AddPublicationToTeacherInformationSheet extends Service {

    public void run(Integer teacherId, Integer publicationId, String publicationArea) throws Exception {
        IPersistentTeacher persistentTeacher = persistentSupport.getIPersistentTeacher();
        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherId);

        IPersistentPublication persistentPublication = persistentSupport.getIPersistentPublication();
        Publication publication = (Publication) persistentPublication.readByOID(Publication.class,
                publicationId);
        
        teacher.addToTeacherInformationSheet(publication,PublicationArea.getEnum(publicationArea));
    }
}