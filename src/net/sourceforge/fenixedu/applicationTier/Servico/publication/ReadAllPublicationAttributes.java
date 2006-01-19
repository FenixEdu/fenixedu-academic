package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.publication.InfoAttribute;
import net.sourceforge.fenixedu.dataTransferObject.publication.InfoSiteAttributes;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.applicationTier.Service;


public class ReadAllPublicationAttributes extends Service {

    public InfoSiteAttributes run(int publicationTypeId) throws ExcepcaoPersistencia {

        List<InfoAttribute> resultNonRequired = new ReadNonRequiredAttributes().run(publicationTypeId);
        
        List<InfoAttribute> resultRequired = new ReadRequiredAttributes().run(publicationTypeId);

        InfoSiteAttributes result = new InfoSiteAttributes();
        result.setInfoNonRequiredAttributes(resultNonRequired);
        result.setInfoRequiredAttributes(resultRequired);
        return result;
    }

}
