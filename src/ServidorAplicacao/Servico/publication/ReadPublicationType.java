/*
 * Created on Jun 17, 2004
 *
 */
package ServidorAplicacao.Servico.publication;

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
}