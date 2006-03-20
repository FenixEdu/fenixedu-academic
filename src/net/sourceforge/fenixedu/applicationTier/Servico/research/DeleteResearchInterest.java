package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResearchInterest extends Service {

    public void run(Integer oid) throws ExcepcaoPersistencia {
        ((ResearchInterest) persistentObject.readByOID(ResearchInterest.class, oid)).delete();
    }
}
