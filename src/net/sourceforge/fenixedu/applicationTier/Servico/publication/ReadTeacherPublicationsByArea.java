package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadTeacherPublicationsByArea implements IService {
    
    public SiteView run(String user, String publicationAreaString) throws ExcepcaoPersistencia {
        
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        final ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
        
        List<InfoPublication> infoPublications = new ArrayList(); 
        for(IPublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
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