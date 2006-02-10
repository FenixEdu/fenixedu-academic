package net.sourceforge.fenixedu.applicationTier.Servico.cms.website;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.cms.FunctionalityLink;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteFunctionalityLink extends Service {

    public void run(Integer oid) throws ExcepcaoPersistencia {
        FunctionalityLink functionalityLink = (FunctionalityLink) this.persistentObject.readByOID(FunctionalityLink.class, oid);
        
        functionalityLink.delete();
    }

}
