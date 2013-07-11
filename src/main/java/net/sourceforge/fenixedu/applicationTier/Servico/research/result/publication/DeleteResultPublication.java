package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteResultPublication {

    @Atomic
    public static void run(String oid) throws FenixServiceException {
        ResearchResultPublication publication = (ResearchResultPublication) FenixFramework.getDomainObject(oid);
        if (publication == null) {
            throw new FenixServiceException();
        }
        publication.delete();
    }
}