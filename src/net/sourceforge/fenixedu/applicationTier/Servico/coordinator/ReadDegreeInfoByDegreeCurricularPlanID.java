package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.beanutils.BeanComparator;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author  <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author  <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadDegreeInfoByDegreeCurricularPlanID implements IService {

    public InfoDegreeInfo run(Integer degreeCurricularPlanID) throws FenixServiceException {
        InfoDegreeInfo infoDegreeInfo = new InfoDegreeInfo();
        infoDegreeInfo.setIdInternal(new Integer(0));

        try {
            if (degreeCurricularPlanID == null) {
                throw new FenixServiceException("error.invalidDegreeCurricularPlan");
            }

            ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // Degree curricular plan
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) suportePersistente
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            degreeCurricularPlanID);

            if (degreeCurricularPlan == null) {
                throw new FenixServiceException("error.invalidDegreeCurricularPlan");
            }

            if (degreeCurricularPlan.getDegree() == null) {
                throw new FenixServiceException("error.invalidDegreeCurricularPlan");
            }

            //Degree
            IDegree degree = null;
            degree = degreeCurricularPlan.getDegree();

            if (degree == null) {
                throw new FenixServiceException("error.impossibleDegreeInfo");
            }

            //Read degree information
            IPersistentDegreeInfo persistentDegreeInfo = suportePersistente.getIPersistentDegreeInfo();
            List degreeInfoList = persistentDegreeInfo.readDegreeInfoByDegree(degree);

            //Last information about this degree
            if (degreeInfoList != null && degreeInfoList.size() > 0) {
                Collections.sort(degreeInfoList, new BeanComparator("lastModificationDate"));
                IDegreeInfo degreeInfo = (IDegreeInfo) degreeInfoList.get(degreeInfoList.size() - 1);

                infoDegreeInfo = InfoDegreeInfo.newInfoFromDomain(degreeInfo);

                infoDegreeInfo.recaptureNULLs(degreeInfo);

                //return the degree info with the last modification date
                //even if this degree info doesn't belong at execution period
                // used.
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return infoDegreeInfo;
    }
}