/*
 * Created on Jun 17, 2004
 *
 */
package ServidorAplicacao.Servico.publication;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.publication.IPublicationType;
import Dominio.publication.PublicationType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublicationType;

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
            sp = SuportePersistenteOJB.getInstance();

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
            sp = SuportePersistenteOJB.getInstance();

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