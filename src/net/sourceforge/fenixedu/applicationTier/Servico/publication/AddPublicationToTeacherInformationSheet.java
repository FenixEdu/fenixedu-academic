package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.util.PublicationArea;
import net.sourceforge.fenixedu.applicationTier.Service;

public class AddPublicationToTeacherInformationSheet extends Service {

    public void run(Integer teacherId, Integer publicationId, String publicationArea) throws Exception {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherId);

        IPersistentPublication persistentPublication = sp.getIPersistentPublication();
        Publication publication = (Publication) persistentPublication.readByOID(Publication.class,
                publicationId);
        
        teacher.addToTeacherInformationSheet(publication,PublicationArea.getEnum(publicationArea));
    }
}