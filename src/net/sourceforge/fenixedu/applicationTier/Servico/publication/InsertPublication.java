package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class InsertPublication implements IService {

    public void run(InfoPublication infoPublication) throws ExcepcaoPersistencia, ExistingServiceException {

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentPublicationType persistentPublicationType = sp.getIPersistentPublicationType();

        final List infoAuthorsList = infoPublication.getInfoPublicationAuthors();

        final List<IPerson> authors = new InsertInexistentAuthors().run(infoAuthorsList);

        final IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                PublicationType.class, infoPublication.getInfoPublicationType().getIdInternal());
        
        DomainFactory.makePublication(infoPublication,publicationType,authors);
    }
    
}