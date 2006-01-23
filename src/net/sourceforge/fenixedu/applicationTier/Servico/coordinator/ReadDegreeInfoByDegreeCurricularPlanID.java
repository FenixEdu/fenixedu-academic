package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;

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

        // Degree curricular plan
        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) persistentObject.readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        // Read degree information
        List<DegreeInfo> degreeInfoList = degreeCurricularPlan.getDegree().getDegreeInfos();
        InfoDegreeInfo infoDegreeInfo = null;

        // Last information about this degree
        if (degreeInfoList != null && !degreeInfoList.isEmpty()) {
            DegreeInfo degreeInfo = (DegreeInfo) Collections.max(degreeInfoList, new BeanComparator(
                    "lastModificationDate"));

            infoDegreeInfo = InfoDegreeInfo.newInfoFromDomain(degreeInfo);
        }

        return infoDegreeInfo;
    }
}