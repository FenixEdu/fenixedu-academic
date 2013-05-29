package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteResultPublication {

    @Service
    public static void run(String oid) throws FenixServiceException {
        ResearchResultPublication publication = (ResearchResultPublication) AbstractDomainObject.fromExternalId(oid);
        if (publication == null) {
            throw new FenixServiceException();
        }
        publication.delete();
    }
}