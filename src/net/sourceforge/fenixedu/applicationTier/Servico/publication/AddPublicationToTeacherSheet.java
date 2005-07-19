package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;
import net.sourceforge.fenixedu.util.PublicationArea;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class AddPublicationToTeacherSheet implements IService {

    public void run(Integer teacherId, Integer publicationId, String publicationArea) throws Exception {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherId);

        IPersistentPublication persistentPublication = sp.getIPersistentPublication();
        IPublication publication = (IPublication) persistentPublication.readByOID(Publication.class,
                publicationId);
        
        teacher.addToTeacherSheet(publication,PublicationArea.getEnum(publicationArea));
    }
}