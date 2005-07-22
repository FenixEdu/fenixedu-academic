/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAttribute;
import net.sourceforge.fenixedu.domain.publication.IAttribute;
import net.sourceforge.fenixedu.domain.publication.IPublicationType;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadRequiredAttributes implements IService {

    public List<InfoAttribute> run(int publicationTypeId) throws ExcepcaoPersistencia {
           
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentPublicationType persistentPublicationType = persistentSuport.getIPersistentPublicationType();
       
        
        IPublicationType publicationType = (IPublicationType) persistentPublicationType.readByOID(
                PublicationType.class, new Integer(publicationTypeId));

        List<InfoAttribute> infoAttributes = new ArrayList<InfoAttribute>(); 
        for (IAttribute attribute : (List<IAttribute>)publicationType.getRequiredAttributes()) {
            infoAttributes.add(InfoAttribute.newInfoFromDomain(attribute));
        }

        return infoAttributes;
    }
}