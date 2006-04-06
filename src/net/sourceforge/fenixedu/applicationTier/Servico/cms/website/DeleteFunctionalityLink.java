package net.sourceforge.fenixedu.applicationTier.Servico.cms.website;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.cms.FunctionalityLink;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteFunctionalityLink extends Service {

    public void run(Integer oid) throws ExcepcaoPersistencia {
        FunctionalityLink functionalityLink = (FunctionalityLink) rootDomainObject.readContentByOID(oid);
        functionalityLink.delete();
    }

}
