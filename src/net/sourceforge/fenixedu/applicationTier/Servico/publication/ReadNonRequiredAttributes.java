/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
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
 * @author TJBF
 * @author PFON
 *  
 */
public class ReadNonRequiredAttributes implements IService {

    public List run(String user, int publicationTypeId) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentPublicationType persistentPublicationType = persistentSuport
                .getIPersistentPublicationType();
        IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                PublicationType.class, new Integer(publicationTypeId));

        List nonRequiredAttributeList = publicationType.getNonRequiredAttributes();

        List result = (List) CollectionUtils.collect(nonRequiredAttributeList, new Transformer() {
            public Object transform(Object o) {
                IAttribute publicationAttribute = (IAttribute) o;
                return Cloner.copyIAttribute2InfoAttribute(publicationAttribute);
            }
        });

        return result;
    }

}
