package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.research.result.Publication;
import net.sourceforge.fenixedu.util.PublicationArea;

public class AddPublicationToTeacherInformationSheet extends Service {

    public void run(Integer teacherId, Integer publicationId, String publicationArea) throws Exception {
        Teacher teacher = (Teacher) persistentObject.readByOID(Teacher.class, teacherId);
        Publication publication = (Publication) persistentObject.readByOID(Publication.class, publicationId);
        teacher.addToTeacherInformationSheet(publication,PublicationArea.getEnum(publicationArea));
    }
}