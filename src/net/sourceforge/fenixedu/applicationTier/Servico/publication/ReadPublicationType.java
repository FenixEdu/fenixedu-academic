/*
 * Created on Jun 17, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author TJBF & PFON
 * 
 */
public class ReadPublicationType implements IService {

    /**
     *  
     */
    public ReadPublicationType() {
      
    }

    

    public IPublicationType run(Integer publicationTypeId) throws FenixServiceException {
        ISuportePersistente sp;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentPublicationType persistentPublicationType = sp.getIPersistentPublicationType();

            IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                    PublicationType.class, publicationTypeId);
            return publicationType;
        } catch (ExcepcaoPersistencia e) {
           
            throw new FenixServiceException(e);
        }
    }
    
    /*
    public IPublicationType run(String publicationTypeName) throws FenixServiceException {
        ISuportePersistente sp;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentPublicationType persistentPublicationType = sp.getIPersistentPublicationType();
 
            List list = (List) persistentPublicationType.readByPublicationsTypeId(
                    publicationTypeName);
            
            IPublicationType publicationType = null;
            
            if (!list.isEmpty()) {
            	publicationType = (IPublicationType)list.get(0);
            }
            
            return publicationType;
        } catch (ExcepcaoPersistencia e) {
           
            throw new FenixServiceException(e);
        }
    }
    */
}