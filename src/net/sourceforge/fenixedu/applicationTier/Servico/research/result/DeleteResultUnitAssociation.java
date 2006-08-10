package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;

public class DeleteResultUnitAssociation extends Service  {

    public void run(Integer associationId) {
        final ResultUnitAssociation association = rootDomainObject.readResultUnitAssociationByOID(associationId);
        final Result result = association.getResult();
        association.delete();
        result.setModificationDateAndAuthor();
    }
}
