package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;

public class DeleteResultEventAssociation extends Service  {

    public void run(Integer associationId) {
        final ResultEventAssociation association = rootDomainObject.readResultEventAssociationByOID(associationId);
        final Result result = association.getResult();
        association.delete();
        result.setModificationDateAndAuthor();
    }
}
