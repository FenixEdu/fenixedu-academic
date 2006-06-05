package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadDegreeInfoByDegreeCurricularPlanID extends Service {

    public InfoDegreeInfo run(Integer degreeCurricularPlanID) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (degreeCurricularPlanID == null) {
            throw new FenixServiceException("error.invalidDegreeCurricularPlan");
        }

        DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);

        DegreeInfo mostRecentDegreeInfo = degreeCurricularPlan.getDegree().getMostRecentDegreeInfo();
        if (mostRecentDegreeInfo != null) {
            return InfoDegreeInfo.newInfoFromDomain(mostRecentDegreeInfo);
        }

        return null;
    }
    
}
