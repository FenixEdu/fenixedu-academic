package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;

public class DeleteResultEventAssociation extends Service  {

    public void run(Integer oid) {
        final ResultEventAssociation association = ResultEventAssociation.readByOid(oid);
        association.getResult().removeEventAssociation(association);
    }
}
