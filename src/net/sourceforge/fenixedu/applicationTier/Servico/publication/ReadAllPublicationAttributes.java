/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAttribute;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSiteAttributes;
import net.sourceforge.fenixedu.domain.publication.IAttribute;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

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