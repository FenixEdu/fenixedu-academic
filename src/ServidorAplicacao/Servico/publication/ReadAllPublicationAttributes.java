/*
 * Created on 13/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.publication;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.publication.InfoAttribute;
import DataBeans.publication.InfoSiteAttributes;
import Dominio.publication.IAttribute;
import Dominio.publication.PublicationType;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.publication.IPersistentPublicationType;

/**
 * @author Carlos Pereira
 * @author Francisco Passos
 *  
 */
public class ReadAllPublicationAttributes implements IServico {
    private static ReadAllPublicationAttributes service = new ReadAllPublicationAttributes();
    /**
     *  
     */
    private ReadAllPublicationAttributes() {

    }

    public static ReadAllPublicationAttributes getService() {

        return service;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadAllPublicationAttributes";
    }

    public InfoSiteAttributes run(int publicationTypeId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();


            IPersistentPublicationType persistentPublicationType = persistentSuport
                    .getIPersistentPublicationType();
            PublicationType publicationType = (PublicationType) persistentPublicationType.readByOID(
                    PublicationType.class, new Integer(publicationTypeId));

            List nonRequiredAttributeList = publicationType.getNonRequiredAttributes();

            List resultNonRequired = (List) CollectionUtils.collect(nonRequiredAttributeList, new Transformer() {
                public Object transform(Object o) {
                    IAttribute publicationAttribute = (IAttribute) o;
                    return InfoAttribute.newInfoFromDomain(publicationAttribute);
                }
            });
            
		    List requiredAttributeList = publicationType.getRequiredAttributes();
		
		    List resultRequired = (List) CollectionUtils.collect(requiredAttributeList, new Transformer() {
		        public Object transform(Object o) {
		            IAttribute publicationAttribute = (IAttribute) o;
		            return InfoAttribute.newInfoFromDomain(publicationAttribute);
		        }
		    });
		    
		    InfoSiteAttributes result = new InfoSiteAttributes();
		    result.setInfoNonRequiredAttributes(resultNonRequired);
		    result.setInfoRequiredAttributes(resultRequired);
            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}