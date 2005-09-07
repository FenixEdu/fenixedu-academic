package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ReadDegreeInfoByDegreeCurricularPlanID implements IService {

    public InfoDegreeInfo run(Integer degreeCurricularPlanID) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (degreeCurricularPlanID == null) {
            throw new FenixServiceException("error.invalidDegreeCurricularPlan");
        }

        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        // Degree curricular plan
        IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) suportePersistente
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        // Read degree information
        List<IDegreeInfo> degreeInfoList = degreeCurricularPlan.getDegree().getDegreeInfos();
        InfoDegreeInfo infoDegreeInfo = null;

        // Last information about this degree
        if (degreeInfoList != null && !degreeInfoList.isEmpty()) {
            IDegreeInfo degreeInfo = (IDegreeInfo) Collections.max(degreeInfoList, new BeanComparator(
                    "lastModificationDate"));

            infoDegreeInfo = InfoDegreeInfo.newInfoFromDomain(degreeInfo);
        }

        return infoDegreeInfo;
    }
}