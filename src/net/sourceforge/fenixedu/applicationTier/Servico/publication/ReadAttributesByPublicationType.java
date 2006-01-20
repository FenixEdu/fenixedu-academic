/*
 * Created on Apr 8, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.Attribute;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadAttributesByPublicationType extends Service {

    /**
     * @param args
     * @throws ExcepcaoPersistencia 
     */
    public HashMap run(Integer publicationTypeID) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentPublicationType persistentPublicationType = persistentSupport
                .getIPersistentPublicationType();
        PublicationType publicationType = (PublicationType) persistentPublicationType.readByOID(
                PublicationType.class, new Integer(publicationTypeID));

        HashMap result = new HashMap();
        if (publicationType != null) {
        List<Attribute> requiredAttributes = publicationType.getRequiredAttributes();
            List<Attribute> nonRequiredAttributes = publicationType.getNonRequiredAttributes();
            List<Attribute> allAttributes = new ArrayList<Attribute>(requiredAttributes);
            allAttributes.addAll(nonRequiredAttributes);

            for (Iterator iter = allAttributes.iterator(); iter.hasNext();) {
                Attribute attribute = (Attribute) iter.next();
                String attributeName = attribute.getAttributeType();
                result.put(attributeName, attributeName);
            }
        }

        return result;

    }

}
