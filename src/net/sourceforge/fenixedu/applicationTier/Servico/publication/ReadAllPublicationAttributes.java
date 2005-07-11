/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAttribute;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSiteAttributes;
import net.sourceforge.fenixedu.domain.publication.IAttribute;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Carlos Pereira
 * @author Francisco Passos
 * 
 */
public class ReadAllPublicationAttributes implements IService {

    public InfoSiteAttributes run(int publicationTypeId) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentPublicationType persistentPublicationType = persistentSuport
                .getIPersistentPublicationType();
        IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                PublicationType.class, new Integer(publicationTypeId));

        List nonRequiredAttributeList = publicationType.getNonRequiredAttributes();

        List resultNonRequired = (List) CollectionUtils.collect(nonRequiredAttributeList,
                new Transformer() {
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
    }

}
