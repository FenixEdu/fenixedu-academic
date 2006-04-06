/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAttribute;
import net.sourceforge.fenixedu.domain.research.result.Attribute;
import net.sourceforge.fenixedu.domain.research.result.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadRequiredAttributes extends Service {

    public List<InfoAttribute> run(int publicationTypeId) throws ExcepcaoPersistencia {
        PublicationType publicationType = rootDomainObject.readPublicationTypeByOID(new Integer(publicationTypeId));

        List<InfoAttribute> infoAttributes = new ArrayList<InfoAttribute>();
        if (publicationType != null) {
            for (Attribute attribute : (List<Attribute>) publicationType.getRequiredAttributes()) {
                infoAttributes.add(InfoAttribute.newInfoFromDomain(attribute));
            }
        }

        return infoAttributes;
    }
    
}
