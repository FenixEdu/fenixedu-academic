package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadTeacherPublicationsByArea extends Service {
    
    public SiteView run(String user, String publicationAreaString) throws ExcepcaoPersistencia {
        
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        final Teacher teacher = persistentTeacher.readTeacherByUsername(user);
        
        List<InfoPublication> infoPublications = new ArrayList<InfoPublication>(); 
        for(PublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            if (publicationTeacher.getPublicationArea().getName().equals(publicationAreaString)) {
                infoPublications.add(InfoPublication.newInfoFromDomain(publicationTeacher.getPublication()));
            }
        }
        
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
        
        InfoSitePublications bodyComponent = new InfoSitePublications();
        bodyComponent.setInfoPublications(infoPublications);
        bodyComponent.setInfoTeacher(infoTeacher);

        SiteView siteView = new SiteView(bodyComponent);
        return siteView;

    }
}