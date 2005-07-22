package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSitePublications;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadAuthorships implements IService {
    
    public SiteView run(String user) throws FenixServiceException {
        try {
            

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            
            IPerson person = persistentPerson.lerPessoaPorUsername(user);
            ITeacher teacher = persistentTeacher.readTeacherByUsername(user);
            
            InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
            List<InfoPublication> infoPublications = new ArrayList<InfoPublication>(person.getPersonAuthorshipsCount());
            
            for (IAuthorship authorship : person.getPersonAuthorships()) {
                infoPublications.add(InfoPublication.newInfoFromDomain(authorship.getPublication()));
            }

            InfoSitePublications infoSitePublications = new InfoSitePublications();
            infoSitePublications.setInfoTeacher(infoTeacher);
            infoSitePublications.setInfoPublications(infoPublications);

            return new SiteView(infoSitePublications);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}