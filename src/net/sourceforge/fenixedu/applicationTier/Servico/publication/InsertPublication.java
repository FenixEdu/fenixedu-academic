package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoPublication;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class InsertPublication implements IService {

    public void run(InfoPublication infoPublication) throws ExcepcaoPersistencia, ExistingServiceException {


        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentPublicationType persistentPublicationType = sp.getIPersistentPublicationType();

        // Write the authors to the AUTHOR table
        List infoAuthorsList = infoPublication.getInfoPublicationAuthors();

        // Call the InsertAuthors Service to insert in the DB the
        // PublicationAuthors
        final InsertInexistentAuthors iia = new InsertInexistentAuthors();
        final List<IPerson> authors = iia.run(infoAuthorsList);

        IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                PublicationType.class, infoPublication.getInfoPublicationType().getIdInternal());
        
        // Create the Publication
        new Publication(infoPublication,publicationType,authors);

    }
    
}