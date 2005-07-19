package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;


public class ReadPublicationsNotInTeacherSheet implements IService {

    public SiteView run(String user) throws ExcepcaoPersistencia {
        InfoSitePublications infoSitePublications = new InfoSitePublications();
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        
        ITeacher teacher = persistentTeacher.readTeacherByUsername(user);

        List<IPublication> publicationsInTeacherSheet = new ArrayList(teacher.getTeacherPublicationsCount());
        for(IPublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            publicationsInTeacherSheet.add(publicationTeacher.getPublication());
        }
        
        List<InfoPublication> infoPublications = new ArrayList();
        for(IAuthorship authorship : teacher.getPerson().getPersonAuthorships()) {
            IPublication publication = authorship.getPublication();
            if (!publicationsInTeacherSheet.contains(publication)) {
                infoPublications.add(InfoPublication.newInfoFromDomain(publication));
            }
        }
        
        InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher); 
        infoSitePublications.setInfoTeacher(infoTeacher);
        infoSitePublications.setInfoPublications(infoPublications);

        return new SiteView(infoSitePublications);
    }
}