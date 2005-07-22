/*
 * Created on Apr 8, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.IAttribute;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadAttributesByPublicationType implements IService {

    /**
     * @param args
     * @throws ExcepcaoPersistencia 
     */
    public HashMap run(Integer publicationTypeID) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentPublicationType persistentPublicationType = persistentSuport
                .getIPersistentPublicationType();
        IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                PublicationType.class, new Integer(publicationTypeID));

        List<IAttribute> requiredAttributes = publicationType.getRequiredAttributes();
        List<IAttribute> nonRequiredAttributes = publicationType.getNonRequiredAttributes();
        List<IAttribute> allAttributes = new ArrayList<IAttribute>(requiredAttributes);
        allAttributes.addAll(nonRequiredAttributes);
        
        HashMap result = new HashMap();
        for (Iterator iter = allAttributes.iterator(); iter.hasNext();) {
            IAttribute attribute = (IAttribute) iter.next();
            String attributeName = attribute.getAttributeType();
            result.put(attributeName, attributeName);
        }

        return result;

    }

}
