/*
 * Created on Jun 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorAplicacao.Servico.publication;

import Dominio.publication.IPublicationType;
import Dominio.publication.PublicationType;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublicationType;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ReadPublicationType implements IServico {

    /**
     *  
     */
    public ReadPublicationType() {
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        // TODO Auto-generated method stub
        return "ReadPublicationType";
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
            // TODO Auto-generated catch block
            throw new FenixServiceException(e);
        }
    }
}