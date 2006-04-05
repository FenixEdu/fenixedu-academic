package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResearchInterest extends Service {

    public void run(Integer oid) throws ExcepcaoPersistencia, FenixServiceException {
        ResearchInterest researchInterest = rootDomainObject.readResearchInterestByOID(oid);
        if(researchInterest == null){
            throw new FenixServiceException();
        }
        researchInterest.delete();        
    }
}
