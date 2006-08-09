package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadCPlanFromChosenMasterDegree extends Service {

    public List run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {
        Degree degree = rootDomainObject.readDegreeByOID(idInternal);

        List<InfoDegreeCurricularPlan> result = new ArrayList<InfoDegreeCurricularPlan>();
        for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
            if (!dcp.isBolonha()) {
                result.add(InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(dcp));
            }
        }

        return result;
    }

}
