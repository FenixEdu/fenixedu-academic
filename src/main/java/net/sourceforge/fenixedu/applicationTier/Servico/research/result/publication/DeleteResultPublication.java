package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteResultPublication {

    @Service
    public static void run(Integer oid) throws FenixServiceException {
        ResearchResultPublication publication = (ResearchResultPublication) RootDomainObject.getInstance().readResearchResultByOID(oid);
        if (publication == null) {
            throw new FenixServiceException();
        }
        publication.delete();
    }
}